package code.mogaktae.domain.challenge.controller;

import code.mogaktae.domain.challenge.dto.req.ChallengeRequestDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.dto.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<ResponseDto<ChallengeResponseDto>> getChallenges(@Valid @RequestBody ChallengeRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallenges(request.getSize(), request.getLastCursorId()), "챌린지 조회 성공"));
    }
}
