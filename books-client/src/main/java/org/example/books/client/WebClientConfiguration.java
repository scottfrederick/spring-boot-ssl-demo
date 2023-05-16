package org.example.books.client;

import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

	@Bean
	@Profile("default")
	public WebClient webClient(WebClient.Builder builder, AppProperties app) {
		return builder.baseUrl(app.getServerBaseUrl()).build();
	}

	@Bean
	@Profile("ssl")
	public WebClient sslWebClient(WebClient.Builder builder, WebClientSsl ssl, AppProperties app) {
		return builder.baseUrl(app.getServerBaseUrl()).apply(ssl.fromBundle(app.getSsl().getBundle())).build();
	}

}
