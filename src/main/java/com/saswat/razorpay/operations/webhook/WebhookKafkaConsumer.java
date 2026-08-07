package com.saswat.razorpay.operations.webhook;

import com.saswat.razorpay.common.dto.WebhookTarget;
import com.saswat.razorpay.common.enums.WebhookEventStatus;
import com.saswat.razorpay.common.util.SignerUtil;
import com.saswat.razorpay.merchant.api.MerchantWebhookApi;
import com.saswat.razorpay.operations.entity.WebhookEvent;
import com.saswat.razorpay.operations.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookKafkaConsumer {

    private final MerchantWebhookApi merchantWebhookApi;
    private final ObjectMapper objectMapper;
    private final SignerUtil signerUtil;
    private final WebhookEventRepository webhookEventRepository;
    private final WebhookRetryQueue retryQueue;

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

            Object merchantIdRaw = data.get("merchant_id");
            if (merchantIdRaw == null) {
                log.warn("No merchantId was found, skipping event: {}", eventType);
                ack.acknowledge();
                return;
            }

            UUID merchantId = UUID.fromString(merchantIdRaw.toString());

            List<WebhookTarget> targets = merchantWebhookApi.getActiveConfigsForEvent(merchantId, eventType);
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
            }

            ack.acknowledge();
            
         } catch (Exception e) {
            log.error("Webhook consumer failed to process the record, offset: {}", record.offset());
            //            TODO: check exception for acknowledging

        }
    }
}
