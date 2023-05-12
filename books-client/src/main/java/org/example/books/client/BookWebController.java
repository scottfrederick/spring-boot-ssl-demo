package org.example.books.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class BookWebController {

	private final RestTemplate restTemplate;

	@Value("${client.server-base-url}")
	private String baseUrl;

	public BookWebController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/")
	public String index(Model model) {
		ResponseEntity<List<Book>> response = restTemplate.exchange(baseUrl + "/api/books",
				HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
		model.addAttribute("books", response.getBody());
		return "books";
	}

}
