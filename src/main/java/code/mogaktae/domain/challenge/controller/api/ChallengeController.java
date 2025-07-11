package code.mogaktae.domain.challenge.controller.api;

import code.mogaktae.domain.challenge.controller.docs.ChallengeControllerSpecification;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeInfoRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeInfoResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummariesResponse;
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
    public ResponseEntity<ResponseDto<Long>> createChallenges(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                              @Valid @RequestBody ChallengeCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(challengeService.createChallenge(user, request), "챌린지 생성 성공"));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ChallengeInfoSummariesResponse>> getChallengesSummary(@Valid @RequestBody ChallengeInfoRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesSummary(request.size(), request.lastCursorId()), "요약 챌린지 조회 성공"));
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeInfoResponse>> getChallengesDetail(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                                  @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengeDetails(user, challengeId), "챌린지 상세 조회 완료"));
    }

    @PostMapping("/{challengeId}/participants")
    public ResponseEntity<ResponseDto<Long>> joinChallenges(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                     @Valid @RequestBody ChallengeJoinRequest request,
                                                     @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.joinChallenge(user, request, challengeId), "챌린지 참여 성공"));
    }

    @GetMapping("/results/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeResult>> getChallengeResult(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                           @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengeResult(user, challengeId), "챌린지 결과 조회 성공"));
    }

    @PostMapping("/webhooks/push")
    public ResponseEntity<ResponseDto<?>> pushCodingTestCommit(@RequestBody Map<String, Object> request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.pushCodingTestCommit(request), "레포지토리 푸쉬 웹훅 처리 완료"));
    }
}
