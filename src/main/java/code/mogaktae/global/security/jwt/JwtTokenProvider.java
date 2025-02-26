package code.mogaktae.global.security.jwt;

import code.mogaktae.domain.user.dto.res.TokenResponseDto;
import code.mogaktae.domain.user.entity.UserDetailsImpl;
import code.mogaktae.domain.user.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.security.jwt.access-token.expired-time}")
    private Long accessTokenExpiredTime;

    @Value("${spring.security.jwt.refresh-token.expired-time}")
    private Long refreshTokenExpiredTime;

    private final Key key;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(@Value("${spring.security.jwt.secret}") String secret, UserDetailsServiceImpl userDetailsService){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userDetailsService = userDetailsService;
    }

    public TokenResponseDto generateToken(Authentication authentication){
        log.info("[JwtTokenProvider - generateToken()] : In");

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date((now.getTime() + accessTokenExpiredTime)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date((now.getTime() + refreshTokenExpiredTime)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("[JwtTokenProvider - generateToken()] : Out");

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String getTokenFromRequest(HttpServletRequest req){
        return req
                .getHeader("Authorization")
                .substring(7);
    }

    public boolean validateAccessToken(String accessToken){
        if(accessToken == null)
            throw new JwtException("AccessToken is null");

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰의 형식이 옳바르지 않습니다.");
        } catch (SignatureException | SecurityException e) {
            throw new JwtException("토큰의 서명이 옳바르지 않습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("토큰의 형식이 만료되었습니다. ");
        }
    }

    public Authentication getAuthentication(String accessToken){

        Claims claims = getClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(getEmailFromToken(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Claims getClaims(String accessToken){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    public String getEmailFromToken(String accessToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }
}
