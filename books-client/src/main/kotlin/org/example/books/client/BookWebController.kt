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

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Controller
class BookWebController(private val restTemplate: RestTemplate) {
    @GetMapping("/")
    fun index(model: Model): String {
        val response: ResponseEntity<List<Book>> = restTemplate.getForEntity<List<Book>>("/api/books",
            typeReference<List<Book?>?>())
        model.addAttribute("books", response.body)
        return "books"
    }

    private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}
}
