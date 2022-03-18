package karstenroethig.examples.springbootauthjwt.service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService
{
	private static final String CLAIMS_NAME_ROLES = "roles";

	@Value("${jwt.secret}") private String secret;
	@Value("${jwt.expiration}") private Long expiration;

	public String generateToken(User user, String issuer)
	{
		ZonedDateTime createdDateTime = ZonedDateTime.now();
		ZonedDateTime expirationDateTime = createdDateTime.plusMinutes(expiration);

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

	private Algorithm createAlgorithm()
	{
		return Algorithm.HMAC256(secret);
	}
}
