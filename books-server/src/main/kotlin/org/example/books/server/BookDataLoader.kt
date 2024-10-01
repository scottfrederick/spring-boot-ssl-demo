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
package org.example.books.server

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
internal class BookDataLoader(private val repository: BookRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        if (repository.count() > 0) {
            return
        }

        val books = listOf(
            Book("Spring in Action", listOf("Craig Walls"), "Manning"),
            Book("Spring Boot in Action", listOf("Craig Walls"), "Manning"),
            Book("Spring Microservices in Action", listOf("John Carnell"), "Manning"),
            Book("Reactive Spring", listOf("Josh Long"), "Josh Long"),
            Book(
                "Spring 5",
                listOf("Iuliana Cosmina", "Rob Harrop", "Chris Schaefer", "Clarence Ho"), "Apress"
            ),
            Book(
                "Spring 5 Recipes: A Problem-Solution Approach",
                listOf("Marten Deinum", "Daniel Rubio", "Josh Long"), "Apress"
            ),
            Book("Spring Boot: Up and Running", listOf("Mark Heckler"), "O'Reilly"),
            Book("Spring Security in Action", listOf("Laurentiu Spilca"), "Manning"),
            Book(
                "Spring Data: Modern Data Access for Enterprise Java",
                listOf("Jonathan L. Brisbin", "Oliver Gierke", "Thomas Risberg", "Mark Pollack", "Michael Hunger"),
                "O'Reilly"
            ),
            Book(
                "Spring Integration in Action",
                listOf("Mark Fisher", "Jonas Partner", "Marius Bogoevici", "Iwein Fuld"), "Manning"
            )

        )
        repository.saveAll(books)
    }
}
