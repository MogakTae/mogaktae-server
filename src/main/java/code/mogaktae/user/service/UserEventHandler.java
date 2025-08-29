package code.mogaktae.user.service;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.user.dto.common.UserSolvedProblemIdsUpdateRequest;
import code.mogaktae.user.entity.User;
import code.mogaktae.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class UserEventHandler {

    private final UserRepository userRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateUserSolvedProblemIds(UserSolvedProblemIdsUpdateRequest userSolvedProblemIdsUpdateRequest){
        User user = userRepository.findByNickname(userSolvedProblemIdsUpdateRequest.nickname())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.addSolvedProblemId(userSolvedProblemIdsUpdateRequest.problemId());

        userRepository.save(user);
    }
}
