# Usage with spring-boot

CacheConfiguration.java

	package karstenroethig.app.config;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;

	@Configuration
	public class CacheConfiguration
	{
		@Autowired private ApplicationProperties applicationProperties;

		@Bean
		public <K, V> IGenericCache<K, V> getCache(@Value("${app.cache-timeout}") Long cacheTimeout)
		{
			return new GenericCache<K, V>(cacheTimeout);
		}
	}


UserServiceImpl.java

	package karstenroethig.app.service.impl;

	import org.springframework.stereotype.Service;

	@Service
	public class UserServiceImpl
	{
		private final UserRepository repository;

		private final GenericCache<String, User> cache;

		public UserServiceImpl(UserRepository repository, GenericCache<String, User> cache)
		{
			this.repository = repository;
			this.cache = cache;
		}

		public User findUserByUsername(String username)
		{
			Optional<User> cachedUser = this.cache.get(username);
			if (cachedUser.isPresent())
				return cachedUser.get();

			User user = this.repository.findByUsername(username).orElseThrow(NotFoundException::new);
			this.cache.put(username, user);

			return user;
		}
	}
