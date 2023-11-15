package org.example.books.vault.resource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

public class VaultKeyValueProtocolResolver implements ProtocolResolver {

    private static final String VAULT_SECRET_PREFIX = "vault-kv:";

    private final BeanFactory beanFactory;

    VaultKeyValueProtocolResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (location.startsWith(VAULT_SECRET_PREFIX)) {
            SecretReference secret = SecretReference.from(location.substring(VAULT_SECRET_PREFIX.length()));
            VaultResponse response = getVaultSecret(secret);
            if (response != null && response.getRequiredData().containsKey(secret.key())) {
                Object value = response.getRequiredData().get(secret.key());
                return new ByteArrayResource(value.toString().getBytes());
            }
            throw new IllegalArgumentException("Resource '" + location + "' can not be resolved to a Vault key-value entry");
        }
        return null;
    }

    private VaultResponse getVaultSecret(SecretReference secret) {
        VaultTemplate template = this.beanFactory.getBean(VaultTemplate.class);
        VaultKeyValueOperations keyValueOperations = template.opsForKeyValue(secret.path(),
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_1);
        return keyValueOperations.get(secret.name());
    }

    record SecretReference(String path, String name, String key) {

        static SecretReference from(String reference) {
            String[] parts = reference.split(":");
            return new SecretReference(parts[0], parts[1], parts[2]);
        }

    }

}
