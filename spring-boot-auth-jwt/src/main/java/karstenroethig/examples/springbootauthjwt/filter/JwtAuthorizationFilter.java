package karstenroethig.examples.springbootauthjwt.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter
{
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";

	@Value("${jwt.secret}") private String secret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		Optional<String> token = getJwtFromRequest(request);
		if (token.isPresent())
		{
			try
			{
				Algorithm algorithm = Algorithm.HMAC256(secret);
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJwt = verifier.verify(token.get());
				String username = decodedJwt.getSubject();
				String roles[] = decodedJwt.getClaim("roles").asArray(String.class);
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				Arrays.stream(roles).forEach(role -> {
					authorities.add(new SimpleGrantedAuthority(role));
				});
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
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
