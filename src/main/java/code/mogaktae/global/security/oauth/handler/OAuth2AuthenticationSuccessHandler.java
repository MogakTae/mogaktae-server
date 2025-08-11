package code.mogaktae.global.security.oauth.handler;

import code.mogaktae.domain.user.dto.res.JwtResponse;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import code.mogaktae.global.security.oauth.util.CookieUtils;
import code.mogaktae.global.security.oauth.util.CustomOAuth2UserService;
import code.mogaktae.global.security.oauth.util.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    private final CustomOAuth2UserService oAuth2UserService;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

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

        String mode = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserDetailsImpl userDetails = getOAuth2UserDetails(authentication);

        if (userDetails == null)
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "AUTHENTICATION_LOGIN_ERROR")
                    .build()
                    .toUriString();

        if ("login".equalsIgnoreCase(mode)){
            log.info("Github oAuth2.0 인증 성공 user = {}", userDetails.getUsername());
            if (Boolean.FALSE.equals(oAuth2UserService.checkUserPresent(userDetails.getUsername()))){
                log.info("서비스 회원 아님. 회원가입 페이지로 리다이랙트");
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .path("/signup")
                        .queryParam("nickname", userDetails.getName())
                        .queryParam("profileImageUrl", userDetails.getUserInfo().getProfileImageUrl())
                        .build()
                        .toUriString();
            }else{
                log.info("서비스 회원 인증 완료. 페인 페이지로 리다이랙트");
                JwtResponse tokenResponse = oAuth2UserService.oAuth2Login(authentication);
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .path("/main")
                        .queryParam("accessToken", tokenResponse.accessToken())
                        .queryParam("refreshToken", tokenResponse.refreshToken())
                        .build()
                        .toUriString();
            }
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "UNEXPECTED_LOGIN_ERROR")
                .build()
                .toUriString();
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
