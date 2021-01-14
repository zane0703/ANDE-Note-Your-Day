package sg.LIZ.backend;

import java.util.regex.Pattern;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.ws.rs.WebApplicationException;

public class Auth {
	private static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer [\\S]");
	public static Claims verifyToken(String token) {
		if(token==null||!BEARER_PATTERN.matcher(token).matches()) {
			throw new WebApplicationException(401);
		}
		try {
			return Jwts.parser()
					.setSigningKey(Config.JWT_SECRET).parseClaimsJws(token.split("Bearer ")[2]).getBody();
		} catch (ExpiredJwtException|SignatureException e) {
			throw new WebApplicationException(401);
		}
	}
}
