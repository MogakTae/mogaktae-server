package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    public List<ChallengeSummaryResponseDto> getMyCompletedChallenges(User user) {

        List<ChallengeSummaryResponseDto> completedChallenges = userChallengeRepository.findCompletedChallengesByUser(user);

        log.info("getCompletedChallenges() - {}의 {} 개의 완료된 챌린지 조회 완료", user.getNickname(), completedChallenges.size());

        return completedChallenges;
    }

    public ChallengeSummaryResponseDto getMyInProgressChallenges(User user) {

        ChallengeSummaryResponseDto inProgressChallenges = userChallengeRepository.findInProgressChallengeByUser(user)
                .orElse(null);

        int size = inProgressChallenges == null ? 0 : 1;

        log.info("getInProgressChallenges() - {}의 {} 개의 진행중인 챌린지 조회 완료", user.getNickname(), size);


        return userChallengeRepository.findInProgressChallengeByUser(user)
                .orElse(null);
    }

    public ChallengeResponseDto getChallenges(int size, Long lastCursorId){
        PageRequest pageRequest = PageRequest.of(0, size+1);

        Page<ChallengeSummaryResponseDto> challenges = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeSummaryResponseDto> collection = CursorBasedPaginationCollection.of(challenges.getContent(), size);

        log.info("getChallenges() - {} 개의 챌린지 조회 완료", size);

        return ChallengeResponseDto.of(collection, challengeRepository.count());
    }
}
