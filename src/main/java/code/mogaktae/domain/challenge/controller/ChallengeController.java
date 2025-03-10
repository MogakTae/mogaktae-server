package code.mogaktae.domain.challenge.controller;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeRequestDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailsResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/info")
    public ResponseEntity<ResponseDto<ChallengeResponseDto>> getChallengesSummary(@Valid @RequestBody ChallengeRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesSummary(request.getSize(), request.getLastCursorId()), "챌린지 요약 조회 성공"));
    }

    @GetMapping("/info/details")
    public ResponseEntity<ResponseDto<ChallengeDetailsResponseDto>> getChallengesDetails(@AuthenticationPrincipal User user,
                                                                                         @RequestParam Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesDetails(user, challengeId), "챌린지 상세 조회 완료"));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Boolean>> createChallenges(@AuthenticationPrincipal User user,
                                                                 @Valid @RequestBody ChallengeCreateRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(challengeService.createChallenge(user, request), "챌린지 생성 성공"));
    }


}
