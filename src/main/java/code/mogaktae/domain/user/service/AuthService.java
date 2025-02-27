package code.mogaktae.domain.user.service;

import code.mogaktae.domain.user.dto.req.SignUpRequestDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public String signUp(SignUpRequestDto request){
        User user = User.builder()
                .request(request)
                .build();

        userRepository.save(user);

        return user.getNickname();
    }

}
