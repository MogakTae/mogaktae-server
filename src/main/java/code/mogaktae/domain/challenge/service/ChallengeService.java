package code.mogaktae.domain.challenge.service;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequestDto;
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
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChallengeService {

    public ChallengeService(@Qualifier("solvedAcRestTemplate") RestTemplate restTemplate, UserRepository userRepository,
                            UserChallengeRepository userChallengeRepository, ChallengeRepository challengeRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.challengeRepository = challengeRepository;
    }

    private final RestTemplate restTemplate;
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
    public Long createChallenge(OAuth2UserDetailsImpl authUser, ChallengeCreateRequestDto request){

        Challenge challenge = Challenge.builder()
                .request(request)
                .build();

        challengeRepository.save(challenge);

        User user = userRepository.findByNickname(authUser.getUsername())
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        UserChallenge userChallenge = UserChallenge.builder()
                        .user(user)
                        .challenge(challenge)
                        .build();

        challenge.getUserChallenges().add(userChallenge);
        user.getUserChallenges().add(userChallenge);

        userChallengeRepository.save(userChallenge);

        log.info("createChallenge() - 챌린지 생성 완료");

        return challenge.getId();
    }

    @Transactional(readOnly = true)
    public ChallengeDetailsResponseDto getChallengesDetails(OAuth2UserDetailsImpl authUser, Long challengeId){

        // TODO 쿼리 최적화 작업 필요

        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if(!userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId))
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

    @Transactional
    public Long joinChallenge(OAuth2UserDetailsImpl authUser, ChallengeJoinRequestDto request){
        User user = userRepository.findByNickname(authUser.getUsername())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Challenge challenge = challengeRepository.findById(request.getChallengeId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.CHALLENGE_NOT_FOUND));

        String endPoint = UriComponentsBuilder.fromUriString("/user/show")
                .queryParam("handle", user.getSolvedAcId())
                .toUriString();

        Map<String, Object> response;
        try{
            response = restTemplate.getForObject(
                    endPoint,
                    Map.class
            );
        }catch (HttpClientErrorException e){
            log.error("joinChallenge() - 챌린지 참여 실패. solvedAc 백준 티어 가져오기 실패");
            throw new RestApiException(CustomErrorCode.HTTP_REQUEST_FAILED);
        }

        Long tier = ((Number) response.get("tier")).longValue();

        UserChallenge userChallenge = UserChallenge.builder()
                        .user(user)
                        .challenge(challenge)
                        .tier(tier)
                        .build();

        challenge.getUserChallenges().add(userChallenge);
        user.getUserChallenges().add(userChallenge);

        userChallengeRepository.save(userChallenge);

        log.info("joinChallenge() - 챌린지 참여 성공");

        return challenge.getId();
    }
}