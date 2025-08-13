package code.mogaktae.auth.service;

import code.mogaktae.auth.dto.req.SignUpRequest;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.entity.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signUp(SignUpRequest request){

        // 유저가 이미 존재하는지 확인
        if (Boolean.TRUE.equals(userRepository.existsByNickname(request.nickname())))
            throw new RestApiException(CustomErrorCode.USER_NICKNAME_DUPLICATED);

        // 유저를 생성하고 저장
        return userRepository.save(User.signUp(request)).getNickname();
    }
}