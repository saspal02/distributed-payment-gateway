package com.saswat.razorpay.vault_service.repository;

import com.saswat.razorpay.vault_service.entity.CardToken;
import com.saswat.razorpay.vault_service.entity.VaultCard;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface CardTokenRepository extends JpaRepository<CardToken, UUID> {
    Optional<CardToken> findByTokenAndRevokedAtIsNull(String token);
}
