package karstenroethig.examples.springbootauthjwt.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class TokenService
{
	private static final String CLAIMS_NAME_ROLES = "roles";

	// Attention: The refresh tokens should be stored in a database.
	private static Set<String> refreshTokens = new HashSet<>();

	@Value("${jwt.accessToken.secret}") private String accessTokenSecret;
	@Value("${jwt.accessToken.expiration}") private Long accessTokenExpiration;
	@Value("${jwt.refreshToken.secret}") private String refreshTokenSecret;
	@Value("${jwt.refreshToken.expiration}") private Long refreshTokenExpiration;

	public String generateAccessToken(User user, String issuer)
	{
		return generateToken(user, issuer, accessTokenSecret, accessTokenExpiration);
	}

	public String generateRefreshToken(User user, String issuer)
	{
		String refreshToken = generateToken(user, issuer, refreshTokenSecret, refreshTokenExpiration);
		refreshTokens.add(refreshToken);
		return refreshToken;
	}

	public String generateAccessTokenFromRefreshToken(String refreshToken, String issuer)
	{
		if (!refreshTokens.contains(refreshToken))
			throw new JWTVerificationException("unknown refresh token");

		Algorithm algorithm = createAlgorithm(refreshTokenSecret);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJwt = verifier.verify(refreshToken);
		String username = decodedJwt.getSubject();
		List<String> roles = decodedJwt.getClaim(CLAIMS_NAME_ROLES).asList(String.class);

		return generateToken(username, roles, issuer, accessTokenSecret, accessTokenExpiration);
	}

	public void removeRefreshToken(String refreshToken)
	{
		refreshTokens.remove(refreshToken);
	}

	public UsernamePasswordAuthenticationToken convertAccessTokenToAuthorizationToken(String accessToken)
	{
		Algorithm algorithm = createAlgorithm(accessTokenSecret);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJwt = verifier.verify(accessToken);
		String username = decodedJwt.getSubject();
		String roles[] = decodedJwt.getClaim(CLAIMS_NAME_ROLES).asArray(String.class);
		Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

	private String generateToken(User user, String issuer, String secret, Long expiration)
	{
		return generateToken(
				user.getUsername(),
				user.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()),
				issuer,
				secret,
				expiration);
	}

	private String generateToken(String username, List<String> roles, String issuer, String secret, Long expiration)
	{
		ZonedDateTime createdDateTime = ZonedDateTime.now();
		ZonedDateTime expirationDateTime = createdDateTime.plusMinutes(expiration);

		Algorithm algorithm = createAlgorithm(secret);
		return JWT.create()
				.withSubject(username)
				.withIssuedAt(Date.from(createdDateTime.toInstant()))
				.withExpiresAt(Date.from(expirationDateTime.toInstant()))
				.withIssuer(issuer)
				.withClaim(CLAIMS_NAME_ROLES, roles)
				.sign(algorithm);
	}

	private Algorithm createAlgorithm(String secret)
	{
		return Algorithm.HMAC256(secret);
	}
}
