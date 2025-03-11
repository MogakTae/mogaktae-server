package code.mogaktae.domain.challenge.controller;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeRequestDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailsResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/info")
    public ResponseEntity<ResponseDto<ChallengeResponseDto>> getChallengesSummary(@Valid @RequestBody ChallengeRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesSummary(request.getSize(), request.getLastCursorId()), "요약 챌린지 조회 성공"));
    }

    @GetMapping("/info/details")
    public ResponseEntity<ResponseDto<ChallengeDetailsResponseDto>> getChallengesDetails(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                                         @RequestParam Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesDetails(user, challengeId), "챌린지 상세 조회 완료"));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createChallenges(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                              @Valid @RequestBody ChallengeCreateRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(challengeService.createChallenge(user, request), "챌린지 생성 성공"));
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<Long>> joinChallenges(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                            @Valid @RequestBody ChallengeJoinRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.joinChallenge(user, request), "챌린지 참여 성공"));
    }
}
