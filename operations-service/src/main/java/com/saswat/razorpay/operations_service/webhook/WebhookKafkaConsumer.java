package com.saswat.razorpay.operations_service.webhook;

import com.saswat.razorpay.common_lib.dto.WebhookTarget;
import com.saswat.razorpay.common_lib.enums.WebhookEventStatus;
import com.saswat.razorpay.common_lib.util.SignerUtil;
import com.saswat.razorpay.operations_service.client.MerchantServiceClient;
import com.saswat.razorpay.operations_service.entity.WebhookEvent;
import com.saswat.razorpay.operations_service.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookKafkaConsumer {

    private final MerchantServiceClient merchantServiceClient;
    private final ObjectMapper objectMapper;
    private final SignerUtil signerUtil;
    private final WebhookEventRepository webhookEventRepository;
    private final WebhookRetryQueue retryQueue;
    private final WebhookDlqRecorder dlqRecorder;

    @KafkaListener(topics = {
            "${app.kafka.topics.payments:payments.events}",
            "${app.kafka.topics.orders:orders.events}",
            "${app.kafka.topics.refunds:refunds.events}",
            "${app.kafka.topics.settlements:settlements.events}"
    })
    public void onWebhookEvent(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) {
        try {
            Map<String, Object> envelope = record.value();
            Map<String, Object> data = (Map<String, Object>) envelope.get("data");
            String eventType = (String) envelope.get("eventType");

            Object merchantIdRaw = data.get("merchantId");
            if (merchantIdRaw == null) {
                log.warn("No merchantId was found, skipping event: {}", eventType);
                ack.acknowledge();
                return;
            }

            UUID merchantId = UUID.fromString(merchantIdRaw.toString());

            List<WebhookTarget> targets = merchantServiceClient.getActiveConfigsForEvent(merchantId, eventType);
            if (targets.isEmpty()) {
                log.debug("No merchantId was found, skipping event: {}", eventType);
                ack.acknowledge();
                return;
            }
            
            Map<String, Object> signatureData = Map.of("event", eventType, "payload", data);
            String signatureJson = objectMapper.writeValueAsString(signatureData);
            
            for (WebhookTarget target : targets) {
                String signature = signerUtil.sign(signatureJson, target.webhookSecret());

                WebhookEvent webhookEvent = WebhookEvent.builder()
                        .merchantId(merchantId)
                        .eventType(eventType)
                        .payload(data)
                        .targetUrl(target.targetUrl())
                        .signature(signature)
                        .status(WebhookEventStatus.PENDING)
                        .nextRetryAt(LocalDateTime.now())
                        .build();

                webhookEvent = webhookEventRepository.save(webhookEvent);

                retryQueue.enqueue(webhookEvent.getId(), webhookEvent.getNextRetryAt());
                log.info("Created a webhook event with id: {}", webhookEvent.getId());
            }

            ack.acknowledge();
            
         } catch (DataAccessException | CannotCreateTransactionException dbDown) {
            log.error("Webhook consumer failed due to DB down, Could not process the record, offset: {}", record.offset(), dbDown);

         } catch (Exception logicError) {
            log.error("Webhook consumer failed due to logical error, Could not process the record, offset: {}", record.offset(), logicError);
            dlqRecorder.recordConsumerFailed(record, logicError.getMessage());
            ack.acknowledge();
        }
    }
}
