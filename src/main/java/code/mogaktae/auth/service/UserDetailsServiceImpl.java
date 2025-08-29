package code.mogaktae.auth.service;

import code.mogaktae.domain.user.entity.User;
import code.mogaktae.auth.domain.UserDetailsImpl;
import code.mogaktae.domain.user.entity.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user);
    }

}
