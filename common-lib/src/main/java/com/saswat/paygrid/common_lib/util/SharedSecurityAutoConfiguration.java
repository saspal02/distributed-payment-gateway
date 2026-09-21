package com.saswat.paygrid.common_lib.util;

import com.saswat.paygrid.common_lib.config.AesEncryptionConfig;
import com.saswat.paygrid.common_lib.context.MerchantContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.web.context.annotation.RequestScope;

@AutoConfiguration
public class SharedSecurityAutoConfiguration {

    @Bean
    public SignerUtil signerUtil() {
        return new SignerUtil();
    }

    @Bean
    @ConditionalOnProperty(name = "vault.master-key")
    public BytesEncryptor masterKeyEncryptor(@Value("${vault.master-key}") String masterKey) {
        return new AesEncryptionConfig().masterKeyEncryptor(masterKey);
    }

    @Bean
    @ConditionalOnProperty(name = "webhook.secret-encryption-key")
    public BytesEncryptor webhookSecretEncryptor(@Value("${webhook.secret-encryption-key}") String masterKey) {
        return new AesEncryptionConfig().masterKeyEncryptor(masterKey);
    }

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MerchantContext merchantContext() {
        return new MerchantContext();
    }

}
