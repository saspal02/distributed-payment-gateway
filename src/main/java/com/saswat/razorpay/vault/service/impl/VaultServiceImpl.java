package com.saswat.razorpay.vault.service.impl;

import com.saswat.razorpay.common.entity.Money;
import com.saswat.razorpay.common.enums.CardBrand;
import com.saswat.razorpay.common.exception.ResourceNotFoundException;
import com.saswat.razorpay.common.util.RandomizerUtil;
import com.saswat.razorpay.payment.processor.PaymentProcessorRouter;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.saswat.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.saswat.razorpay.vault.config.VaultEncryptionConfig;
import com.saswat.razorpay.vault.dto.request.TokenizeRequest;
import com.saswat.razorpay.vault.dto.response.TokenizeResponse;
import com.saswat.razorpay.vault.entity.CardToken;
import com.saswat.razorpay.vault.entity.VaultCard;
import com.saswat.razorpay.vault.repository.CardTokenRepository;
import com.saswat.razorpay.vault.repository.VaultCardRepository;
import com.saswat.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {

        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0,6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
        byte[] encryptedDek = dekEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder()
                .brand(cardBrand)
                .expiryYear(request.expiryYear().toString())
                .expiryMonth(request.expiryMonth().toString())
                .bin(bin)
                .lastFour(lastFour)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .cardHolderName(request.cardHolderName())
                .build();

        vaultCard = vaultCardRepository.save(vaultCard);

        String token = "tok_"+ RandomizerUtil.randomBase64(32);

        CardToken cardToken = CardToken.builder()
                .vaultCard(vaultCard)
                .token(token)
                .customer(request.customerId())
                .merchant(merchantId)
                .build();

        cardToken = cardTokenRepository.save(cardToken);

        return new TokenizeResponse(token, lastFour, cardBrand,
                request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {
        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;

        try {
            byte[] dek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panBytes, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpiryMonth()+"/"+vaultCard.getExpiryYear();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest
                    .card(paymentId, pan, expiry, amount, methodDetails);

            PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);
            log.info("Vault charge registered, token{}****", token.substring(0,4));

            Arrays.fill(panBytes, (byte) 0);

            return response;
        } catch (Exception e) {
            log.warn("Vault charge failed, token={}****", token.substring(0,4));
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED", e.getMessage());

        } finally {
            if (panBytes != null) Arrays.fill(panBytes, (byte) 0);
        }



    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;

        return CardBrand.RUPAY;

    }
}
