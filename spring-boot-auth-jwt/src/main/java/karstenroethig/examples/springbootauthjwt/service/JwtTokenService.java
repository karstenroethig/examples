package karstenroethig.examples.springbootauthjwt.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService
{
	private static final String CLAIMS_NAME_ROLES = "roles";

	@Value("${jwt.accessToken.secret}") private String accessTokenSecret;
	@Value("${jwt.accessToken.expiration}") private Long accessTokenExpiration;

	public String generateAccessToken(User user, String issuer)
	{
		ZonedDateTime createdDateTime = ZonedDateTime.now();
		ZonedDateTime expirationDateTime = createdDateTime.plusMinutes(accessTokenExpiration);

		Algorithm algorithm = createAlgorithm();
		return JWT.create()
				.withSubject(user.getUsername())
				.withIssuedAt(Date.from(createdDateTime.toInstant()))
				.withExpiresAt(Date.from(expirationDateTime.toInstant()))
				.withIssuer(issuer)
				.withClaim(CLAIMS_NAME_ROLES,
						user.getAuthorities().stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.sign(algorithm);
	}

	public UsernamePasswordAuthenticationToken convertAccessTokenToAuthorizationToken(String accessToken)
	{
		Algorithm algorithm = createAlgorithm();
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJwt = verifier.verify(accessToken);
		String username = decodedJwt.getSubject();
		String roles[] = decodedJwt.getClaim(CLAIMS_NAME_ROLES).asArray(String.class);
		Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

	private Algorithm createAlgorithm()
	{
		return Algorithm.HMAC256(accessTokenSecret);
	}
}
