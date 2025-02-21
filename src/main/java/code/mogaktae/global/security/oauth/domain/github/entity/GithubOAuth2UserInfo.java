package code.mogaktae.global.security.oauth.domain.github.entity;

import code.mogaktae.global.security.oauth.domain.common.OAuth2UserInfo;
import code.mogaktae.global.security.oauth.util.OAuth2Provider;
import lombok.Getter;

import java.util.Map;

@Getter
public class GithubOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    private final Long id;
    private final String nickname;
    private final String profileImageUrl;
    private final String accessToken;

    public GithubOAuth2UserInfo(Map<String, Object> attributes, String accessToken){
        this.attributes = attributes;
        Object idValue = attributes.get("id");

        this.id = (idValue instanceof Integer) ? ((Integer) idValue).longValue() : (Long) idValue;

        this.nickname = (String) attributes.get("login");
        this.profileImageUrl = (String) attributes.get("avatar_url");
        this.accessToken = accessToken;
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GITHUB;
    }

    @Override
    public String getAccessToken(){
        return this.accessToken;
    }

    @Override
    public Map<String, Object> getAttributes(){
        return this.attributes;
    }

    @Override
    public Long getId(){
        return this.id;
    }

    @Override
    public String getNickname(){
        return this.nickname;
    }

    @Override
    public String getProfileImageUrl(){
        return this.profileImageUrl;
    }

}
