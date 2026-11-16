import io.jsonwebtoken.Claims;

public class JwtService {

        public Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}