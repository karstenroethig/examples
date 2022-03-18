package karstenroethig.examples.springbootauthjwt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import karstenroethig.examples.springbootauthjwt.model.LoginRequest;
import karstenroethig.examples.springbootauthjwt.model.LoginResponse;
import karstenroethig.examples.springbootauthjwt.service.JwtTokenService;

@RestController
public class AuthController
{
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private JwtTokenService jwtTokenService;

	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request)
	{
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		User user = (User)authentication.getPrincipal();
		String issuer = request.getRequestURL().toString();

		String accessToken = jwtTokenService.generateAccessToken(user, issuer);
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
