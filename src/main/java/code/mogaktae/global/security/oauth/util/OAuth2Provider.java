package code.mogaktae.global.security.oauth.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    GITHUB("github");

    @JsonValue
    private final String registrationId;

    @JsonCreator
    public static OAuth2Provider fromOAuth2Provider(String value){
        return Arrays.stream(values())
                .filter(p -> p.getRegistrationId().equals(value))
                .findAny()
                .orElse(null);
    }
}
