package code.mogaktae.global.security.oauth.domain.common;

import code.mogaktae.domain.user.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class OAuth2UserDetailsImpl implements OAuth2User, UserDetails {

    private final OAuth2UserInfo oAuth2UserInfo;

    public OAuth2UserDetailsImpl(OAuth2UserInfo oAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return oAuth2UserInfo.getNickname();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.USER.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getNickname();
    }

    public OAuth2UserInfo getUserInfo() {
        return oAuth2UserInfo;
    }
}

