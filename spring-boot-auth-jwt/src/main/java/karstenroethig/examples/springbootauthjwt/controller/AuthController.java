package karstenroethig.examples.springbootauthjwt.controller;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import karstenroethig.examples.springbootauthjwt.model.LoginRequest;
import karstenroethig.examples.springbootauthjwt.model.LoginResponse;

@RestController
public class AuthController
{
	@Autowired private AuthenticationManager authenticationManager;

	@Value("${jwt.secret}") private String secret;
	@Value("${jwt.expiration}") private Long expiration;

	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request)
	{
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		User user = (User)authentication.getPrincipal();

		ZonedDateTime createdDateTime = ZonedDateTime.now();
		ZonedDateTime expirationDateTime = createdDateTime.plusMinutes(expiration);

		Algorithm algorithm = Algorithm.HMAC256(secret);
		String accessToken = JWT.create()
				.withSubject(user.getUsername())
				.withIssuedAt(Date.from(createdDateTime.toInstant()))
				.withExpiresAt(Date.from(expirationDateTime.toInstant()))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setAccessToken(accessToken);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	@ExceptionHandler
	void handleAuthenticationException(HttpServletResponse response, AuthenticationException ex) throws IOException
	{
		response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
	}
}
