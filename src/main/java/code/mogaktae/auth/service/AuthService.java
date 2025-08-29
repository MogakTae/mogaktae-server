package code.mogaktae.auth.service;

import code.mogaktae.auth.dto.req.SignUpRequest;
import code.mogaktae.common.client.BaekjoonClient;
import code.mogaktae.common.client.SolvedAcClient;
import code.mogaktae.user.dto.req.UserDocumentSaveRequest;
import code.mogaktae.user.entity.User;
import code.mogaktae.user.entity.UserRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationEventPublisher publisher;

    private final SolvedAcClient solvedAcClient;
    private final BaekjoonClient baekjoonClient;
    private final UserRepository userRepository;

    @Transactional
    public String signUp(SignUpRequest request){

        if(userRepository.existsByNickname(request.nickname()))
            throw new RestApiException(CustomErrorCode.USER_DUPLICATED);

        if(userRepository.existsBySolvedAcId(request.solvedAcId()))
            throw new RestApiException(CustomErrorCode.SOLVED_AC_ID_DUPLICATED);

        if(!solvedAcClient.verifySolvedAcId(request.solvedAcId()))
            throw new RestApiException(CustomErrorCode.SOLVED_AC_ID_NOT_AVAILABLE);

        User user = userRepository.save(User.signUp(request, baekjoonClient.getSolvedProblemIds(request.solvedAcId())));

        publisher.publishEvent(new UserDocumentSaveRequest(user.getId(), user.getNickname(), user.getProfileImageUrl()));

        return user.getNickname();
    }
}