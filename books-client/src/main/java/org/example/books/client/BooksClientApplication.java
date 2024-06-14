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

import java.util.List;

import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BooksClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate, RestClient restClient, WebClient webClient) {
		return args -> {
			retrieveWithRestTemplate(restTemplate);
			retrieveWithRestClient(restClient);
			retrieveWithWebClient(webClient);
		};
	}

	private void retrieveWithRestTemplate(RestTemplate restTemplate) {
		System.out.println();
		System.out.println("Attempting to connect to server with RestTemplate");
		try {
			ResponseEntity<List<Book>> response = restTemplate.exchange("/api/books",
					HttpMethod.GET, null, new ParameterizedTypeReference<>() {
					});
			List<Book> books = response.getBody();
			System.out.println();
			System.out.println("Successfully connected to server with RestTemplate and received response:");
			System.out.println(StringUtils.collectionToDelimitedString(books, "\n"));
		} catch (Exception ex) {
			System.out.println();
			System.out.println("Error connecting to server with RestTemplate: " + ex.getMessage());
		}
	}

	private void retrieveWithRestClient(RestClient webClient) {
		System.out.println();
		System.out.println("Attempting to connect to server with RestClient");
		try {
			List<Book> books = webClient.get()
					.uri("/api/books")
					.retrieve().body(new ParameterizedTypeReference<>() {
					});
			System.out.println();
			System.out.println("Successfully connected to server with RestClient and received response:");
			System.out.println(StringUtils.collectionToDelimitedString(books, "\n"));
		} catch (Exception ex) {
			System.out.println();
			System.out.println("Error connecting to server with RestClient: " + ex.getMessage());
		}

	}

	private void retrieveWithWebClient(WebClient webClient) {
		System.out.println();
		System.out.println("Attempting to connect to server with WebClient");
		try {
			Mono<List<Book>> result = webClient.get()
					.uri("/api/books")
					.exchangeToMono((response) -> {
						if (response.statusCode().equals(HttpStatus.OK)) {
							return response.bodyToMono(new ParameterizedTypeReference<>() {
							});
						} else {
							return response.createError();
						}
					});
			List<Book> books = result.block();
			System.out.println();
			System.out.println("Successfully connected to server with WebClient and received response:");
			System.out.println(StringUtils.collectionToDelimitedString(books, "\n"));
		} catch (Exception ex) {
			System.out.println();
			System.out.println("Error connecting to server with WebClient: " + ex.getMessage());
		}

	}

}
