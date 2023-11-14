package org.example.books.gcp.resource;

import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class GoogleCloudSecretManagerProtocolResolver implements ProtocolResolver {

    private static final String GCP_SECRET_PREFIX = "gcp:";

    private final BeanFactory beanFactory;

    GoogleCloudSecretManagerProtocolResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (location.startsWith(GCP_SECRET_PREFIX)) {
            String secretId = location.substring(GCP_SECRET_PREFIX.length());
            SecretManagerTemplate secretManagerTemplate = this.beanFactory.getBean(SecretManagerTemplate.class);
            String secretValue = secretManagerTemplate.getSecretString(secretId);
            if (secretValue != null) {
                return new ByteArrayResource(secretValue.getBytes());
            }
            throw new IllegalArgumentException("Resource '" + location +
                    "' can not be resolved to a Google Cloud Secret Manager entry");
        }
        return null;
    }

}
