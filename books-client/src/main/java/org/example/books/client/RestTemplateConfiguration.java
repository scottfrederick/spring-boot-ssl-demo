package org.example.books.client;

import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

	@Bean
	@Profile("default")
	public RestTemplate restTemplate(RestTemplateBuilder builder, AppProperties app) {
		return builder.rootUri(app.getServerBaseUrl()).build();
	}

	@Bean
	@Profile("ssl")
	public RestTemplate sslRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles, AppProperties app) {
		SslBundle sslBundle = sslBundles.getBundle(app.getSsl().getBundle());
		return builder.rootUri(app.getServerBaseUrl()).setSslBundle(sslBundle).build();
	}

}
