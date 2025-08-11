package code.mogaktae.domain.challenge.controller.api;

import code.mogaktae.domain.challenge.controller.docs.ChallengeControllerSpecification;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeSummariesRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.domain.challenge.entity.ChallengeResult;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController implements ChallengeControllerSpecification {

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createChallenge(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                             @Valid @RequestBody ChallengeCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(challengeService.createChallenge(user, request), "챌린지 생성 성공"));
    }

    @GetMapping("/summaries")
    public ResponseEntity<ResponseDto<ChallengeSummariesResponse>> getChallengeSummaries(@Valid @RequestBody ChallengeSummariesRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesSummary(request.size(), request.lastCursorId()), "챌린지 요약 조회 성공"));
    }

    @GetMapping("/details/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeDetailResponse>> getChallengeDetail(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                                   @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesDetail(user, challengeId), "챌린지 상세 조회 완료"));
    }

    @PostMapping("/participants")
    public ResponseEntity<ResponseDto<Long>> joinChallenge(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                           @Valid @RequestBody ChallengeJoinRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.joinChallenge(user, request), "챌린지 참여 성공"));
    }

    @GetMapping("/results/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeResult>> getChallengeResult(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                           @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengeResult(user, challengeId), "챌린지 결과 조회 성공"));
    }

    @PostMapping("/webhooks/push")
    public ResponseEntity<ResponseDto<?>> pushCodingTestCommit(@RequestBody Map<String, Object> request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.handleChallengeCommit(request), "레포지토리 푸쉬 웹훅 처리 완료"));
    }
}
