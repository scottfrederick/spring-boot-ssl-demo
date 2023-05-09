package org.example.books.client;

import java.util.List;

public record Book(String id, String title, List<String> authors, String publisher) {

	@Override
	public String toString() {
		return "Book[" +
				"title=" + title +
				", authors=" + authors +
				", publisher=" + publisher +
				']';
	}

}
