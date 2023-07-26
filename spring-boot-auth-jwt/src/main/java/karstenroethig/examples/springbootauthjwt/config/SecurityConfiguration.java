package karstenroethig.examples.springbootauthjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import karstenroethig.examples.springbootauthjwt.filter.JwtAuthorizationFilter;

@Configuration
public class SecurityConfiguration
{
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilterBean()
	{
		return new JwtAuthorizationFilter();
	}

	@Bean
	public UserDetailsService userDetailsService()
	{
		UserDetails user = User
			.withUsername("user")
			.password("{noop}password")
			.roles("USER")
			.build();
		UserDetails admin = User
			.withUsername("admin")
			.password("{noop}password")
			.roles("USER", "PASSWORD")
			.build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http
			.sessionManagement((sessionManagement) -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.csrf((csrf) -> csrf
				.disable() // deactivate Cross Site Request Forgery (CSRF), otherwise it is annoying with REST requests
			)
			.headers((headers) -> headers
				.frameOptions((frameOptions) -> frameOptions.disable())
				.cacheControl((cacheControl) -> cacheControl.disable())
			)
			.addFilterBefore(jwtAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				// take care of order, if a condition applies no further rules will be checked
				.requestMatchers(
					"/login",
					"/token",
					"/invalidate",
					"/error",
					"/books/public").permitAll()
				.requestMatchers(
					"/books/private/user").hasAnyAuthority("ROLE_USER")
				.requestMatchers(
					"/books/private/admin").hasAnyAuthority("ROLE_ADMIN")
				.anyRequest()
					.authenticated()
			);

		return http.build();
	}
}
