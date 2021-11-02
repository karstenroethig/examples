package karstenroethig.example.streamsdistinctbyproperty;

import java.util.ArrayList;
import java.util.List;

public class Usage
{
	public static void main(String[] args)
	{
		List<Person> persons = new ArrayList<>();
		persons.add(new Person(1L, "Michael Jordan"));
		persons.add(new Person(2L, "Dennis Rodman"));
		persons.add(new Person(3L, "Michael Jordan"));

		persons
			.stream()
			.forEach(System.out::println);

		System.out.println("---");

		persons
			.stream()
			.map(person -> new Wrapper<>(person, Person::getName))
			.distinct()
			.map(Wrapper::unwrap)
			.forEach(System.out::println);
	}

	private static class Person
	{
		private final Long id;
		private final String name;

		public Person(Long id, String name)
		{
			this.id = id;
			this.name = name;
		}

		public Long getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public String toString()
		{
			return String.format("%s (%s)", name, id);
		}
	}
}
