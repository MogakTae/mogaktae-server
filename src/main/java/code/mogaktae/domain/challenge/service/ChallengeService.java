package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.challenge.dto.ChallengeSummaryResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final UserChallengeRepository userChallengeRepository;

    public List<ChallengeSummaryResponseDto> getCompletedChallenges(User user) {
        return userChallengeRepository.findCompletedChallengesByUser(user);
    }

    public ChallengeSummaryResponseDto getInProgressChallenges(User user) {
        return userChallengeRepository.findInProgressChallengeByUser(user)
                .orElse(null);
    }
}
