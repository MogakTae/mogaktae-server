package code.mogaktae.global.security.oauth.handler;

import code.mogaktae.auth.dto.res.JwtResponse;
import code.mogaktae.global.security.jwt.JwtProvider;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import code.mogaktae.global.security.oauth.util.CookieUtils;
import code.mogaktae.global.security.oauth.util.CustomOAuth2UserService;
import code.mogaktae.global.security.oauth.util.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.security.jwt.access-token.expired-time}")
    private Long accessTokenExpiredTime;

    @Value("${spring.security.jwt.refresh-token.expired-time}")
    private Long refreshTokenExpiredTime;

    private final CustomOAuth2UserService oAuth2UserService;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl = setTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String setTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Optional<String> redirectUrl = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM)
                .map(Cookie::getValue);

        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());

        OAuth2UserDetailsImpl userDetails = getOAuth2UserDetails(authentication);

        if (userDetails == null)
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "AUTHENTICATION_LOGIN_ERROR")
                    .build()
                    .toUriString();

        if(Boolean.TRUE.equals(oAuth2UserService.checkUserPresent(userDetails.getName()))){
            // 회원인 경우
            JwtResponse jwtResponse = jwtProvider.generateToken(authentication);

            response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.createCookie("access-token", jwtResponse.accessToken(), accessTokenExpiredTime / 1000).toString());
            response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.createCookie("refresh-token", jwtResponse.refreshToken(), refreshTokenExpiredTime / 1000).toString());

            log.info("{}", HttpHeaders.SET_COOKIE);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build()
                    .toUriString();

        }else{
            // 회원이 아닌 경우
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .path("/sign-up")
                    .queryParam("nickname", userDetails.getName())
                    .queryParam("profile-image-url", userDetails.getUserInfo().getProfileImageUrl())
                    .build()
                    .toUriString();
        }
    }

    private OAuth2UserDetailsImpl getOAuth2UserDetails(Authentication authentication){

        Object principal = authentication.getPrincipal();

        if(principal instanceof OAuth2UserDetailsImpl)
            return (OAuth2UserDetailsImpl) principal;

        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
