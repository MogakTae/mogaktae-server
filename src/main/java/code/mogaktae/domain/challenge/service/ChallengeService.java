package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.challenge.dto.ChallengeSummaryResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final UserChallengeRepository userChallengeRepository;

    public List<ChallengeSummaryResponseDto> getCompletedChallenges(User user) {

        List<ChallengeSummaryResponseDto> completedChallenges = userChallengeRepository.findCompletedChallengesByUser(user);

        log.info("getCompletedChallenges() - {} 개의 완료된 챌린지 조회 완료", completedChallenges.size());

        return completedChallenges;
    }

    public ChallengeSummaryResponseDto getInProgressChallenges(User user) {

        ChallengeSummaryResponseDto inProgressChallenges = userChallengeRepository.findInProgressChallengeByUser(user)
                .orElse(null);

        int size = inProgressChallenges == null ? 0 : 1;

        log.info("getInProgressChallenges() - {} 개의 진행중인 챌린지 조회 완료", size);


        return userChallengeRepository.findInProgressChallengeByUser(user)
                .orElse(null);
    }
}
