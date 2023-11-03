/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	@Profile("!default")
	public RestTemplate sslRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles, AppProperties app) {
		SslBundle sslBundle = sslBundles.getBundle(app.getSsl().getBundle());
		return builder.rootUri(app.getServerBaseUrl()).setSslBundle(sslBundle).build();
	}

}
