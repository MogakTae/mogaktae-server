package code.mogaktae.global.security.oauth.handler;

import code.mogaktae.domain.user.dto.res.TokenResponseDto;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import code.mogaktae.global.security.oauth.util.CookieUtils;
import code.mogaktae.global.security.oauth.util.CustomOAuth2UserService;
import code.mogaktae.global.security.oauth.util.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CustomOAuth2UserService oAuth2UserService;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException{

        String targetUrl = setTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String setTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        Optional<String> redirectUrl = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM)
                .map(Cookie::getValue);

        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserDetailsImpl userDetails = getOAuth2UserDetails(authentication);

        if (userDetails == null)
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login Failed with Authentication Error")
                    .build()
                    .toUriString();

        if ("login".equalsIgnoreCase(mode)){
            if (!oAuth2UserService.checkUserPresent(userDetails.getUsername())){
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .path("/signup")
                        .queryParam("provider", userDetails.getUserInfo().getProvider())
                        .build()
                        .toUriString();
            }else{
                TokenResponseDto tokenResponseDto = customOAuth2UserService.oAuth2Login(authentication);
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .path("/main")
                        .queryParam("accessToken", tokenResponseDto.getAccessToken())
                        .queryParam("refreshToken", tokenResponseDto.getRefreshToken())
                        .build()
                        .toUriString();
            }
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login Failed with unexpected Error")
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
