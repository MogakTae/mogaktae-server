package code.mogaktae.domain.user.controller.docs;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.req.SignUpRequest;
import code.mogaktae.global.exception.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "AuthController", description = "인증 API")
public interface AuthControllerSpecification {
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다<br>" +
            "🔐 <strong>Jwt 불필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "✅ 회원가입 성공"),
            @ApiResponse(responseCode = "VALID_4001", description = "🚨 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"VALID_4001\", \"message\" : \"유효성 검사 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "USER_4001", description = "🚨 중복되는 닉네임",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "중복되는 닉네임",
                                            value = "{\"error\" : \"USER_4001\", \"message\" : \"중복되는 닉네임\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/sign-up")
    ResponseEntity<ResponseDto<String>> signUp(@Valid @RequestBody SignUpRequest request);
}