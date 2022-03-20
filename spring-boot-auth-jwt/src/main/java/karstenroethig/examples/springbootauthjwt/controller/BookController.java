package karstenroethig.examples.springbootauthjwt.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import karstenroethig.examples.springbootauthjwt.model.Book;

@RestController
@RequestMapping("/books")
public class BookController
{
	private static final List<Book> books = Stream
			.of(new Book("HWPO: Hard work pays off", "Mat Fraser"))
			.collect(Collectors.toList());

	@GetMapping("/public")
	public List<Book> findAllPublic()
	{
		return books;
	}

	@GetMapping("/private/user")
	public List<Book> findAllPrivateUser()
	{
		return books;
	}

	@GetMapping("/private/admin")
	public List<Book> findAllPrivateAdmin()
	{
		return books;
	}
}
