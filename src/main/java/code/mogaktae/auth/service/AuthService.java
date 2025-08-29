package code.mogaktae.auth.service;

import code.mogaktae.auth.dto.req.SignUpRequest;
import code.mogaktae.domain.common.client.SolvedAcClient;
import code.mogaktae.domain.user.dto.req.SaveUserDocumentRequest;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.entity.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationEventPublisher publisher;

    private final SolvedAcClient solvedAcClient;
    private final UserRepository userRepository;

    @Transactional
    public String signUp(SignUpRequest request){

        if(userRepository.existsByNickname(request.nickname())) {
            throw new RestApiException(CustomErrorCode.USER_NICKNAME_DUPLICATED);
        } else if (!solvedAcClient.verifySolvedAcId(request.solvedAcId())) {
            throw new RestApiException(CustomErrorCode.USER_SOLVED_AC_ID_NOT_AVAILABLE);
        } else if (userRepository.existsBySolvedAcId(request.solvedAcId())) {
            throw new RestApiException(CustomErrorCode.USER_SOLVED_AC_ID_DUPLICATED);
        }

        // 유저를 생성하고 저장
        User user = userRepository.save(User.signUp(request));

        // Elasticsearch 유저 토큰 동기화
        publisher.publishEvent(new SaveUserDocumentRequest(user.getId(), user.getNickname(), user.getProfileImageUrl()));

        log.info("Sign up successful for user {}", user.getNickname());

        return user.getNickname();
    }
}