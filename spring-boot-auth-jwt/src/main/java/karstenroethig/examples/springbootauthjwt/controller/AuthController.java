package karstenroethig.examples.springbootauthjwt.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import karstenroethig.examples.springbootauthjwt.exception.ForbiddenException;
import karstenroethig.examples.springbootauthjwt.exception.UnauthorizedException;
import karstenroethig.examples.springbootauthjwt.model.AccessAndRefreshToken;
import karstenroethig.examples.springbootauthjwt.model.AccessToken;
import karstenroethig.examples.springbootauthjwt.model.Login;
import karstenroethig.examples.springbootauthjwt.model.RefreshToken;
import karstenroethig.examples.springbootauthjwt.service.TokenService;

@RestController
public class AuthController
{
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private TokenService tokenService;

	@PostMapping(value = "/login")
	public ResponseEntity<AccessAndRefreshToken> login(@RequestBody Login login, HttpServletRequest request)
	{
		try
		{
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			User user = (User)authentication.getPrincipal();
			String issuer = request.getRequestURL().toString();

			String accessToken = tokenService.generateAccessToken(user, issuer);
			String refreshToken = tokenService.generateRefreshToken(user, issuer);

			AccessAndRefreshToken tokens = new AccessAndRefreshToken();
			tokens.setAccessToken(accessToken);
			tokens.setRefreshToken(refreshToken);
			return new ResponseEntity<>(tokens, HttpStatus.OK);
		}
		catch (AuthenticationException ex)
		{
			throw new UnauthorizedException(ex.getMessage(), ex);
		}
	}

	@PostMapping(value = "/token")
	public ResponseEntity<AccessToken> token(@RequestBody RefreshToken refreshToken, HttpServletRequest request)
	{
		if (refreshToken == null || StringUtils.isBlank(refreshToken.getRefreshToken()))
			throw new UnauthorizedException("refresh token cannot be empty");

		try
		{
			String issuer = request.getRequestURL().toString();
			String accessToken = tokenService.generateAccessTokenFromRefreshToken(refreshToken.getRefreshToken(), issuer);

			AccessToken token = new AccessToken();
			token.setAccessToken(accessToken);
			return new ResponseEntity<>(token, HttpStatus.OK);
		}
		catch (Exception ex)
		{
			throw new ForbiddenException(ex.getMessage(), ex);
		}
	}

	@DeleteMapping(value = "/invalidate")
	public ResponseEntity<Void> invalidate(@RequestBody RefreshToken refreshToken)
	{
		tokenService.removeRefreshToken(refreshToken.getRefreshToken());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler
	void handleUnauthorizedException(HttpServletResponse response, UnauthorizedException ex) throws IOException
	{
		response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
	}

	@ExceptionHandler
	void handleForbiddenException(HttpServletResponse response, ForbiddenException ex) throws IOException
	{
		response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
	}
}
