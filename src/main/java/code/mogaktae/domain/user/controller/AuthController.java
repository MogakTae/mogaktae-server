package code.mogaktae.domain.user.controller;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.req.CheckRepositoryUrlRequestDto;
import code.mogaktae.domain.user.dto.req.SignUpRequestDto;
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
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<String>> signUp(@Valid @RequestBody SignUpRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(authService.signUp(request), "회원가입 성공"));
    }


}
