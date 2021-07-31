package app.core.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import app.core.enums.ClientType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	private String signatureAlgorithm;
	private String secret;
	private Key decodedSecretKey;
	private long tokenDurationMS;

	@Autowired
	public JwtUtil(@Value("${app.jwt.token.secret}") String secret,
			@Value("${app.jwt.token.durationMS}") long tokenDurationMS) {
		this.signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
		this.secret = secret;
		this.tokenDurationMS = tokenDurationMS;
	}

	@PostConstruct
	private void init() {
		this.decodedSecretKey = new SecretKeySpec(Base64.getDecoder().decode(this.secret), this.signatureAlgorithm);
	}

	/**
	 * Generates JWT token by specified user details.
	 * 
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userDetails.id);
		claims.put("userName", userDetails.name);
		claims.put("userType", userDetails.clientType);
		return createToken(claims, userDetails.email);
	}

	/**
	 * Creates JWT token by specified claims and subject.
	 * 
	 * @param claims
	 * @param subject
	 * @return
	 */
	private String createToken(Map<String, Object> claims, String subject) {

		Instant now = Instant.now();

		return Jwts.builder().setClaims(claims)

				.setSubject(subject)

				.setIssuedAt(Date.from(now))

				.setExpiration(Date.from(now.plusMillis(this.tokenDurationMS)))

				.signWith(this.decodedSecretKey)

				.compact();
	}

	/**
	 * Extracts all claims of the JWT token.
	 * 
	 * @param token
	 * @return
	 * @throws ExpiredJwtException
	 */
	private Claims extractAllClaims(String token) throws ExpiredJwtException {
		JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(this.decodedSecretKey).build();
		return jwtParser.parseClaimsJws(token).getBody();
	}

	/**
	 * Returns the JWT subject (email).
	 * 
	 * @param token
	 * @return
	 */
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	/**
	 * Returns expiration date of the JWT token.
	 * 
	 * @param token
	 * @return
	 */
	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	public ClientType extractUserType(String token) {
		ClientType type = extractAllClaims(token).get("userType", ClientType.class);
		System.out.println("client type: " + type);
		return type;
	}

	/**
	 * Return {@code true} if JWT token is expired, otherwise - {@code false}.
	 * 
	 * @param token
	 * @return
	 */
	public boolean isTokenExpired(String token) {
		try {
			if (new Date().after(this.extractExpiration(token)))
				return true;
			return false;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	/**
	 * Returns User Details object extracted from JWT token.
	 * 
	 * @param token
	 * @return
	 */
	private UserDetails extractUserDetails(String token) {
		Claims claims = this.extractAllClaims(token);
		UserDetails userDetails = new UserDetails(claims.get("userId", Integer.class),
				claims.get("userName", String.class), claims.getSubject(), claims.get("userType", ClientType.class));
		return userDetails;
	}

	/**
	 * Returns refreshed JWT token.
	 * 
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		return generateToken(extractUserDetails(token));
	}

}
