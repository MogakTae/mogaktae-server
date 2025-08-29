package code.mogaktae.userChallenge.service;

import code.mogaktae.challengeResult.dto.common.EndChallengeIdName;
import code.mogaktae.userChallenge.dto.req.UserChallengeCreateRequest;
import code.mogaktae.userChallenge.entity.UserChallenge;
import code.mogaktae.userChallenge.entity.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserChallengeEventHandler {

    private final UserChallengeRepository userChallengeRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createUserChallenge(UserChallengeCreateRequest userChallengeCreateRequest) {
        userChallengeRepository.save(UserChallenge.create(
                userChallengeCreateRequest.userId(),
                userChallengeCreateRequest.challengeId(),
                userChallengeCreateRequest.repositoryUrl(),
                userChallengeCreateRequest.tier()
        ));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateUserChallenge(List<EndChallengeIdName> endChallengeIdNames) {

        List<Long> endChallengeIds = endChallengeIdNames.stream()
                .map(EndChallengeIdName::challengeId)
                .toList();

        userChallengeRepository.updateUserChallengeEndStatus(endChallengeIds);
    }
}