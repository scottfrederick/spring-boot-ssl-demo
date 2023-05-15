package org.example.books.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

	@Bean
	@Profile("default")
	public WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}

	@Bean
	@Profile("ssl")
	public WebClient sslWebClient(WebClient.Builder builder, WebClientSsl ssl,
								  @Value("${app.ssl.bundle}") String sslBundle) {
		return builder.apply(ssl.fromBundle(sslBundle)).build();
	}

}
