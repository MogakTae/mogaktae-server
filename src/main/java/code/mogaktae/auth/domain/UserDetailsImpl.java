package code.mogaktae.auth.domain;

import code.mogaktae.user.entity.Role;
import code.mogaktae.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails{

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Role.USER.getAuthorities();
    }

    @Override
    public String getPassword(){
        return null;
    }

    @Override
    public String getUsername(){
        return user.getNickname();
    }
}
