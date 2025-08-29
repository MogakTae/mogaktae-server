package code.mogaktae.global.security.jwt;

import code.mogaktae.auth.domain.UserDetailsImpl;
import code.mogaktae.auth.dto.res.JwtResponse;
import code.mogaktae.auth.service.UserDetailsServiceImpl;
import code.mogaktae.global.exception.entity.CustomJwtException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.security.oauth.util.CookieUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final Long ACCESS_TOKEN_EXPIRED_TIME = 21600000L;

    private static final Long REFRESH_TOKEN_EXPIRED_TIME = 259200000L;

    private final Key key;

    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtProvider(@Value("${spring.security.jwt.secret}") String secret, UserDetailsServiceImpl userDetailsService, RedisTemplate<String, String> redisTemplate){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    public JwtResponse generateToken(Authentication authentication){

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        String nickname = authentication.getName();
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(nickname)
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date((now.getTime() + ACCESS_TOKEN_EXPIRED_TIME)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(nickname)
                .setIssuedAt(now)
                .setExpiration(new Date((now.getTime() + REFRESH_TOKEN_EXPIRED_TIME)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisTemplate.opsForValue().set(
                nickname,
                refreshToken,
                Duration.ofSeconds(REFRESH_TOKEN_EXPIRED_TIME)
        );

        return JwtResponse.create(accessToken, refreshToken);
    }

    public String getAccessTokenFromRequest(HttpServletRequest request){
        return CookieUtils.getValueFromCookie(request, "access-token");
    }

    public String getRefreshTokenFromRequest(HttpServletRequest request){
        return CookieUtils.getValueFromCookie(request, "refresh-token");
    }

    public boolean validateAccessToken(String accessToken){
        if(accessToken == null)
            throw new CustomJwtException(CustomErrorCode.JWT_ACCESS_TOKEN_NULL);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_MALFORMED);
        } catch (SignatureException | SecurityException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_SIGNATURE_INVALID);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_UNSUPPORTED);
        }
    }

    public String validateRefreshToken(String refreshToken) {
        if (refreshToken == null)
            throw new CustomJwtException(CustomErrorCode.JWT_REFRESH_TOKEN_NULL);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String nickname = claims.getSubject();

            String savedRefreshToken = redisTemplate.opsForValue().get(nickname);

            if (savedRefreshToken == null) {
                throw new CustomJwtException(CustomErrorCode.JWT_REFRESH_TOKEN_NOT_FOUND);
            } else if (!savedRefreshToken.equals(refreshToken)) {
                throw new CustomJwtException(CustomErrorCode.JWT_REFRESH_TOKEN_MISMATCH);
            }

            return nickname;

        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_EXPIRED);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_MALFORMED);
        } catch (SignatureException | SecurityException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_SIGNATURE_INVALID);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException(CustomErrorCode.JWT_TOKEN_UNSUPPORTED);
        }
    }

    public Authentication getAuthenticationFromJwt(String token){

        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(getNicknameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Authentication getAuthenticationFromNickname(String nickname){
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(nickname);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String getNicknameFromToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
