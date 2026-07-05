package com.saswat.razorpay.merchant.service.impl;

import com.saswat.razorpay.common.enums.MerchantStatus;
import com.saswat.razorpay.common.enums.UserRole;
import com.saswat.razorpay.common.exception.DuplicateResourceException;
import com.saswat.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.saswat.razorpay.merchant.dto.response.MerchantResponse;
import com.saswat.razorpay.merchant.entity.AppUser;
import com.saswat.razorpay.merchant.entity.Merchant;
import com.saswat.razorpay.merchant.repository.AppUserRepository;
import com.saswat.razorpay.merchant.repository.MerchantRepository;
import com.saswat.razorpay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;

    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public MerchantResponse signUp(MerchantSignUpRequest request) {
        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with email already exists: " + request.email());
        }

        Merchant merchant = Merchant.builder()
                .businessName(request.name())
                .businessType(request.businessType())
                .name(request.name())
                .email(request.email())
                .status(MerchantStatus.PENDING_KYC)
                .build();
         merchantRepository.save(merchant);
         
        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) // TODO: encrypt using Bcrypt
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);

        return new MerchantResponse(merchant.getId(), merchant.getName(),
                merchant.getEmail(), merchant.getBusinessName(),
                merchant.getBusinessType(), merchant.getStatus());
    }
}
