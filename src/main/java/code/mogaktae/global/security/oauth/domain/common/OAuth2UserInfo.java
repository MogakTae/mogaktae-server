package code.mogaktae.global.security.oauth.domain.common;

import code.mogaktae.global.security.oauth.util.OAuth2Provider;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    Long getId();

    String getNickname();

    String getProfileImageUrl();
}
