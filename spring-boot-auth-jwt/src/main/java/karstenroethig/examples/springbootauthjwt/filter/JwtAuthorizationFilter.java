package karstenroethig.examples.springbootauthjwt.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import karstenroethig.examples.springbootauthjwt.service.TokenService;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter
{
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";

	@Autowired private TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		Optional<String> token = getJwtFromRequest(request);
		if (token.isPresent())
		{
			try
			{
				UsernamePasswordAuthenticationToken authenticationToken
						= tokenService.convertAccessTokenToAuthorizationToken(token.get());
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			catch (Exception ex)
			{
				response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private Optional<String> getJwtFromRequest(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		return StringUtils.startsWith(authorizationHeader, BEARER_TOKEN_PREFIX)
			? Optional.of(authorizationHeader.substring(BEARER_TOKEN_PREFIX.length()))
			: Optional.empty();
	}
}
