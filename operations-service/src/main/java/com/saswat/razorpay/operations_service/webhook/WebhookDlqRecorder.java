package com.saswat.razorpay.operations_service.webhook;

import com.saswat.razorpay.common_lib.enums.WebhookEventStatus;
import com.saswat.razorpay.operations_service.entity.DlqEvent;
import com.saswat.razorpay.operations_service.entity.WebhookEvent;
import com.saswat.razorpay.operations_service.repository.DlqEventRepository;
import com.saswat.razorpay.operations_service.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookDlqRecorder {

    private final WebhookEventRepository webhookEventRepository;
    private final DlqEventRepository dlqEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordAfterAttemptsExhausted(WebhookEvent webhookEvent, String finalError) {
        log.debug("Recording the dlq event with webhookEventId: {}", webhookEvent.getId());

        webhookEvent.setStatus(WebhookEventStatus.DEAD);
        webhookEventRepository.save(webhookEvent);

        DlqEvent dlqEvent = DlqEvent.builder()
                .webhookEvent(webhookEvent)
                .merchantId(webhookEvent.getMerchantId())
                .finalError(finalError)
                .payload(webhookEvent.getPayload())
                .build();
        dlqEventRepository.save(dlqEvent);
    }

    public void recordConsumerFailed(ConsumerRecord<String, Map<String, Object>> record, String error) {
        Map<String, Object> envelope = record.value();

        UUID merchantId = null;

        try {
            Map<String, Object> data = (Map<String, Object>) envelope.get("data");
            Object merchantIdRaw = data != null ? data.get("merchantid") : null;
            if (merchantIdRaw != null) {
                merchantId = UUID.fromString(merchantIdRaw.toString());
            }
        } catch (Exception ignored) {
            log.error("Error while processing the record, offset: {}", record.offset());
        }

        log.debug("Recording the dlq because consumer failed event with merchantId: {}", merchantId);
        DlqEvent dlqEvent = DlqEvent.builder()
                .webhookEvent(null)
                .merchantId(merchantId)
                .finalError(error)
                .payload(envelope != null ? envelope: Map.of())
                .build();
        dlqEventRepository.save(dlqEvent);
    }


}
