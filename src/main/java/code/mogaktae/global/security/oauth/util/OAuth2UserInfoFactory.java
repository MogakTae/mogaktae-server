package code.mogaktae.global.security.oauth.util;

import code.mogaktae.global.security.oauth.domain.common.OAuth2UserInfo;
import code.mogaktae.global.security.oauth.domain.github.entity.GithubOAuth2UserInfo;
import code.mogaktae.global.security.oauth.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes, String accessToken){

        if (OAuth2Provider.GITHUB.getRegistrationId().equals(registrationId)) return new GithubOAuth2UserInfo(attributes, accessToken);

        throw new OAuth2AuthenticationProcessingException("Invalid Provider with " + registrationId);
    }
}
