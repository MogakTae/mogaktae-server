package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailsResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    public List<ChallengeSummaryResponseDto> getMyCompletedChallenges(User user) {

        List<ChallengeSummaryResponseDto> completedChallenges = userChallengeRepository.findCompletedChallengesByUser(user);

        log.info("getCompletedChallenges() - {}의 {} 개의 완료된 챌린지 조회 완료", user.getNickname(), completedChallenges.size());

        return completedChallenges;
    }

    public List<ChallengeSummaryResponseDto> getMyInProgressChallenges(User user) {

        List<ChallengeSummaryResponseDto> inProgressChallenges = userChallengeRepository.findInProgressChallengeByUser(user);

        int size = inProgressChallenges.size();

        log.info("getInProgressChallenges() - {}의 {} 개의 진행중인 챌린지 조회 완료", user.getNickname(), size);

        return inProgressChallenges;
    }

    @Transactional(readOnly = true)
    public ChallengeResponseDto getChallengesSummary(int size, Long lastCursorId){
        PageRequest pageRequest = PageRequest.of(0, size+1);

        Page<ChallengeSummaryResponseDto> challenges = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeSummaryResponseDto> collection = CursorBasedPaginationCollection.of(challenges.getContent(), size);

        log.info("getChallenges() - {} 개의 챌린지 조회 완료", size);

        return ChallengeResponseDto.of(collection, challengeRepository.count());
    }

    @Transactional
    public Boolean createChallenge(User authUser, ChallengeCreateRequestDto request){

        Challenge challenge = Challenge.builder()
                .request(request)
                .build();

        challengeRepository.save(challenge);

        request.getUserNicknames().add(authUser.getNickname());

        request.getUserNicknames().forEach(nickname -> {
            User user = userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

            UserChallenge userChallenge = UserChallenge.builder()
                    .user(user)
                    .challenge(challenge)
                    .build();

            challenge.getUserChallenges().add(userChallenge);
            user.getUserChallenges().add(userChallenge);

            userChallengeRepository.save(userChallenge);
        });

        log.info("createChallenge() - 챌린지 생성 완료");

        return true;
    }

    @Transactional(readOnly = true)
    public ChallengeDetailsResponseDto getChallengesDetails(User authUser, Long challengeId){

        if(!userChallengeRepository.existsByUserIdAndChallengeId(authUser.getId(), challengeId))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<UserChallengeSummaryDto> userChallengeSummaries = userChallengeRepository.findUserChallengeSummariesByChallengeId(challengeId);

        Long totalPenalty = userChallengeSummaries.stream()
                .mapToLong(UserChallengeSummaryDto::getPenalty)
                .sum();

        return ChallengeDetailsResponseDto.builder()
                .name(challenge.getName())
                .startDate(challenge.getStartDate().toString())
                .endDate(challenge.getEndDate().toString())
                .totalPenalty(totalPenalty)
                .userChallengeSummaries(userChallengeSummaries)
                .build();
    }
}