package code.mogaktae.global.security.oauth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.http.ResponseCookie;

import java.io.Serializable;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static ResponseCookie createCookie(String key, String value, long maxAge){
        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(maxAge)
                .build();
    }

    public static String getValueFromCookie(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(key)){
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(key)){
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void setCookie(HttpServletResponse response, String key, String value, int maxAge){
        Cookie cookie = new Cookie(key, value);

        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String key){
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(key)){
                    cookie.setPath("/");
                    cookie.setValue("");
                    cookie.setMaxAge(0);

                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize((Serializable) object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
