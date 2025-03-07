package code.mogaktae.domain.git.controller;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.req.CheckRepositoryUrlRequestDto;
import code.mogaktae.domain.user.dto.req.UpdateUserRepositoryUrlRequestDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/git")
public class GitController {

    private final AuthService authService;

    @PostMapping("/repository-url/confirm")
    public ResponseEntity<ResponseDto<Boolean>> checkRepositoryUrlAvailable(@Valid @RequestBody CheckRepositoryUrlRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.checkRepositoryUrlAvailable(request.getNickname(), request.getRepositoryUrl()), "레포지토리 URL 검사 완료"));
    }

    @PutMapping("/repository-url")
    public ResponseEntity<ResponseDto<String>> updateRepositoryUrl(@AuthenticationPrincipal User user,
                                                                   @Valid @RequestBody UpdateUserRepositoryUrlRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.updateRepositoryUrl(user, request), "레포지토리 URL 변경 완료"));
    }
}
