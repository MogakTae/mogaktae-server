package code.mogaktae.domain.challenge.controller.api;

import code.mogaktae.auth.domain.UserDetailsImpl;
import code.mogaktae.domain.challenge.controller.docs.ChallengeControllerSpecification;
import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeSummariesRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.domain.challenge.facade.ChallengeFacade;
import code.mogaktae.domain.challengeResult.entity.ChallengeResult;
import code.mogaktae.domain.common.dto.ResponseDto;
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
@RequestMapping("/api/v2/challenges")
public class ChallengeController implements ChallengeControllerSpecification {

    private final ChallengeFacade challengeFacade;

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createChallenge(@AuthenticationPrincipal UserDetailsImpl user,
                                                             @Valid @RequestBody ChallengeCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(challengeFacade.createChallenge(user.getUsername(), request), "챌린지 생성 성공"));
    }

    @GetMapping("/summaries")
    public ResponseEntity<ResponseDto<ChallengeSummariesResponse>> getChallengeSummaries(@Valid @RequestBody ChallengeSummariesRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeFacade.getChallengesSummary(request.size(), request.lastCursorId()), "챌린지 요약 조회 성공"));
    }

    @GetMapping("/details/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeDetailResponse>> getChallengeDetail(@AuthenticationPrincipal UserDetailsImpl user,
                                                                                   @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeFacade.getChallengesDetail(user.getUsername(), challengeId), "챌린지 상세 조회 완료"));
    }

    @PostMapping("/participants")
    public ResponseEntity<ResponseDto<Long>> joinChallenge(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @Valid @RequestBody ChallengeJoinRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeFacade.joinChallenge(user.getUsername(), request), "챌린지 참여 성공"));
    }

    @GetMapping("/results/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeResult>> getChallengeResult(@AuthenticationPrincipal UserDetailsImpl user,
                                                                           @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeFacade.getChallengeResult(user.getUsername(), challengeId), "챌린지 결과 조회 성공"));
    }

    @PostMapping("/webhooks/push")
    public ResponseEntity<ResponseDto<?>> pushCodingTestCommit(@RequestBody Map<String, Object> request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeFacade.handleChallengeCommit(request), "레포지토리 푸쉬 웹훅 처리 완료"));
    }
}
