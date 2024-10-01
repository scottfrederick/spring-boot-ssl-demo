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
package org.example.books.client

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.util.StringUtils
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class BooksClientApplication {
    @Bean
    fun run(restTemplate: RestTemplate, restClient: RestClient, webClient: WebClient): CommandLineRunner {
        return CommandLineRunner { _ ->
            retrieveWithRestTemplate(restTemplate)
            retrieveWithRestClient(restClient)
            retrieveWithWebClient(webClient)
        }
    }

    private fun retrieveWithRestTemplate(restTemplate: RestTemplate) {
        println()
        println("Attempting to connect to server with RestTemplate")
        try {
            val response = restTemplate.getForEntity<List<Book>>("/api/books")
            val books = response.body
            println()
            println("Successfully connected to server with RestTemplate and received response:")
            println(StringUtils.collectionToDelimitedString(books, "\n"))
        } catch (ex: Exception) {
            println()
            println("Error connecting to server with RestTemplate: " + ex.message)
        }
    }

    private fun retrieveWithRestClient(webClient: RestClient) {
        println()
        println("Attempting to connect to server with RestClient")
        try {
            val books = webClient.get()
                .uri("/api/books")
                .retrieve().body(typeReference<List<Book?>?>())
            println()
            println("Successfully connected to server with RestClient and received response:")
            println(StringUtils.collectionToDelimitedString(books, "\n"))
        } catch (ex: Exception) {
            println()
            println("Error connecting to server with RestClient: " + ex.message)
        }
    }

    private fun retrieveWithWebClient(webClient: WebClient) {
        println()
        println("Attempting to connect to server with WebClient")
        try {
            val books = webClient.get()
                .uri("/api/books")
                .retrieve()
                .toEntityList(Book::class.java)
                .block()
                ?.body
            println()
            println("Successfully connected to server with WebClient and received response:")
            println(StringUtils.collectionToDelimitedString(books, "\n"))
        } catch (ex: Exception) {
            println()
            println("Error connecting to server with WebClient: " + ex.message)
        }
    }

    private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

}

fun main(args: Array<String>) {
    runApplication<BooksClientApplication>(*args)
}
