package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequestDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailsResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.repository.ChallengeRepository;
import code.mogaktae.domain.common.util.CursorBasedPaginationCollection;
import code.mogaktae.domain.common.util.SolvedAcUtils;
import code.mogaktae.domain.redis.service.RedisCacheService;
import code.mogaktae.domain.result.dto.res.ChallengeResultResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.repository.UserChallengeRepository;
import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
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

    private final SolvedAcUtils solvedAcUtils;

    private final AlarmService alarmService;
    private final RedisCacheService redisCacheService;

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    @Transactional(readOnly = true)
    public ChallengeResponseDto getChallengesSummary(int size, Long lastCursorId) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);

        Page<ChallengeSummaryResponseDto> challenges = challengeRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageRequest);

        CursorBasedPaginationCollection<ChallengeSummaryResponseDto> collection = CursorBasedPaginationCollection.of(challenges.getContent(), size);

        log.info("getChallenges() - {} 개의 챌린지 조회 완료", size);

        return ChallengeResponseDto.of(collection, challengeRepository.count());
    }

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
    public ChallengeDetailsResponseDto getChallengesDetails(OAuth2UserDetailsImpl authUser, Long challengeId) {

        if (!userChallengeRepository.existsByNicknameAndChallengeId(authUser.getUsername(), challengeId))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        List<UserChallengeSummaryDto> userChallengeSummaries = userChallengeRepository.findUserChallengeSummariesByChallengeId(challengeId);

        Long totalPenalty = userChallengeSummaries.stream()
                .mapToLong(UserChallengeSummaryDto::getPenalty)
                .sum();

        log.info("getChallengesDetails() - 챌린지 상세정보 조회 완료");

        return ChallengeDetailsResponseDto.builder()
                .name(challenge.getName())
                .startDate(challenge.getStartDate().toString())
                .endDate(challenge.getEndDate().toString())
                .totalPenalty(totalPenalty)
                .userChallengeSummaries(userChallengeSummaries)
                .build();
    }

    @Transactional
    public Long createChallenge(OAuth2UserDetailsImpl authUser, ChallengeCreateRequestDto request) {

        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if (userChallengeRepository.countUserChallenge(user.getId()) > 3)
            throw new RestApiException(CustomErrorCode.CHALLENGE_MAX_PARTICIPATION_REACHED);

        Challenge challenge = Challenge.builder()
                .request(request)
                .build();

        challengeRepository.save(challenge);

        Long tier = solvedAcUtils.getUserBaekJoonTier(user.getSolvedAcId());

        UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .repositoryUrl(request.getRepositoryUrl())
                .tier(tier)
                .build();

        challenge.getUserChallenges().add(userChallenge);
        user.getUserChallenges().add(userChallenge);

        userChallengeRepository.save(userChallenge);

        alarmService.sendChallengeJoinAlarm(user, challenge, request.getParticipants());

        log.info("createChallenge() - 챌린지 참여 알림 저장 완료");

        log.info("createChallenge() - 챌린지 생성 완료");

        return challenge.getId();
    }

    @Transactional
    public Long joinChallenge(OAuth2UserDetailsImpl authUser, ChallengeJoinRequestDto request) {
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        Long tier = solvedAcUtils.getUserBaekJoonTier(user.getSolvedAcId());

        UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .repositoryUrl(request.getRepositoryUrl())
                .tier(tier)
                .build();

        challenge.getUserChallenges().add(userChallenge);
        user.getUserChallenges().add(userChallenge);

        userChallengeRepository.save(userChallenge);

        log.info("joinChallenge() - 챌린지 참여 성공");

        return challenge.getId();
    }


    public ChallengeResultResponseDto getChallengeResult(OAuth2UserDetailsImpl authUser, Long challengeId) {

        User user = userRepository.findByNickname(authUser.getName())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if (userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId))
            throw new RestApiException(CustomErrorCode.USER_NO_PERMISSION_TO_CHALLENGE);


        return redisCacheService.getChallengeResult(challengeId);
    }

}