package code.mogaktae.global.security.jwt;

import code.mogaktae.auth.dto.res.JwtResponse;
import code.mogaktae.global.security.oauth.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtReissueFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{

        if(request.getRequestURI().equals("/api/v2/auth/reissue")){
            // 리프레시 토큰이 유효한지 확인
            String nickname = jwtProvider.validateRefreshToken(jwtProvider.getRefreshTokenFromRequest(request));

            // 토큰 재발급
            Authentication authentication = jwtProvider.getAuthenticationFromNickname(nickname);

            // 응답 쿠키에 포함시켜 반환
            JwtResponse jwtResponse = jwtProvider.generateToken(authentication);

            setResponse(response, jwtResponse);

            return;
        }

        filterChain.doFilter(request, response);
    }

    protected void setResponse(HttpServletResponse response, JwtResponse jwtResponse) throws IOException{

        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.createCookie("access-token", jwtResponse.accessToken(), 21600).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.createCookie("refresh-token", jwtResponse.refreshToken(), 259200).toString());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write("토큰 재발급 완료");
    }
}
