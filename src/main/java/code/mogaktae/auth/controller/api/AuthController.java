package code.mogaktae.auth.controller.api;

import code.mogaktae.auth.controller.docs.AuthControllerSpecification;
import code.mogaktae.auth.dto.req.SignUpRequest;
import code.mogaktae.auth.service.AuthService;
import code.mogaktae.common.dto.ResponseDto;
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
@RequestMapping("/api/v2/auth")
public class AuthController implements AuthControllerSpecification {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<String>> signUp(@Valid @RequestBody SignUpRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(authService.signUp(request), "회원가입 성공"));
    }
}
