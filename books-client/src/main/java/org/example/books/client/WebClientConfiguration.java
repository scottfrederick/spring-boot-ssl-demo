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
	@Profile("!default")
	public WebClient sslWebClient(WebClient.Builder builder, WebClientSsl ssl, AppProperties app) {
		return builder.baseUrl(app.getServerBaseUrl()).apply(ssl.fromBundle(app.getSsl().getBundle())).build();
	}

}
