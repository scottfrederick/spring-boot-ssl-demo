package org.example.books.client;

import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class BooksClientApplication {

	@Value("${client.server-base-url}")
	private String baseUrl;

	public static void main(String[] args) {
		SpringApplication.run(BooksClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate, WebClient webClient) {
		return args -> {
			retrieveWithRestTemplate(restTemplate);
			retrieveWithWebClient(webClient);
		};
	}

	private void retrieveWithRestTemplate(RestTemplate restTemplate) {
		System.out.println();
		System.out.println("Attempting to connect to server with RestTemplate");
		try {
			ResponseEntity<List<Book>> response = restTemplate.exchange(baseUrl + "/api/books",
					HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
			List<Book> books = response.getBody();
			System.out.println();
			System.out.println("Successfully connected to server with RestTemplate and received response:");
			System.out.println(StringUtils.collectionToDelimitedString(books, "\n"));
		} catch (Exception ex) {
			System.out.println();
			System.out.println("Error connecting to server: " + ex.getMessage());
		}
	}

	private void retrieveWithWebClient(WebClient webClient) {
		System.out.println();
		System.out.println("Attempting to connect to server with WebClient");
		try {
			Mono<List<Book>> result = webClient.get()
					.uri(baseUrl + "/api/books")
					.exchangeToMono((response) -> {
						if (response.statusCode().equals(HttpStatus.OK)) {
							return response.bodyToMono(new ParameterizedTypeReference<>() {});
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
			System.out.println("Error connecting to server: " + ex.getMessage());
		}

	}

}
