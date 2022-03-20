package karstenroethig.examples.springbootauthjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import karstenroethig.examples.springbootauthjwt.filter.JwtAuthorizationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilterBean()
	{
		return new JwtAuthorizationFilter();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication()
				.withUser("user")
				.password("{noop}password")
				.roles("USER")
			.and()
				.withUser("admin")
				.password("{noop}password")
				.roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.csrf().disable()
			.headers()
				.frameOptions().disable()
				.cacheControl().disable()
		.and()
			.addFilterBefore(jwtAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers(
					"/login",
					"/token",
					"/invalidate",
					"/error").permitAll()
			.antMatchers("/books/public").permitAll()
			.antMatchers("/books/private/user").hasAnyAuthority("ROLE_USER")
			.antMatchers("/books/private/admin").hasAnyAuthority("ROLE_ADMIN")
			.anyRequest().authenticated();
	}
}
