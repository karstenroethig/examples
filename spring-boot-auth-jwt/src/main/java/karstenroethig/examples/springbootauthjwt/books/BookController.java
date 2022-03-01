package karstenroethig.examples.springbootauthjwt.books;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController
{
	@GetMapping()
	public List<Book> findAll()
	{
		List<Book> allBooks = new ArrayList<>();
		allBooks.add(new Book("HWPO: Hard work pays off", "Mat Fraser"));
		return allBooks;
	}
}
