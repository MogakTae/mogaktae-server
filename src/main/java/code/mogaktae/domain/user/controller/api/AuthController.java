package code.mogaktae.domain.user.controller.api;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.controller.docs.AuthControllerSpecification;
import code.mogaktae.domain.user.dto.req.SignUpRequest;
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
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerSpecification {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<String>> signUp(@Valid @RequestBody SignUpRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(authService.signUp(request), "회원가입 성공"));
    }
}
