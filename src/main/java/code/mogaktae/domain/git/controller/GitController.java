package code.mogaktae.domain.git.controller;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.req.CheckRepositoryUrlRequestDto;
import code.mogaktae.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/git")
public class GitController {

    private final AuthService authService;

    @PostMapping("/repository-url/check")
    public ResponseEntity<ResponseDto<Boolean>> checkRepositoryUrlAvailable(@Valid @RequestBody CheckRepositoryUrlRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.checkRepositoryUrlAvailable(request.getNickname(), request.getRepositoryUrl()), "레포지토리 URL 검사 완료"));
    }
}
