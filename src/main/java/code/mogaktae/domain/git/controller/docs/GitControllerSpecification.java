package code.mogaktae.domain.git.controller.docs;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.req.RepositoryUrlVerifyRequest;
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

@Tag(name = "GitControllerSpecification", description = "깃 관련 API")
public interface GitControllerSpecification {
    @Operation(summary = "레포지토리 URL 유효성 검사", description = "사용자의 GitHub 레포지토리 URL이 유효하고 사용 가능한지 확인합니다<br>" +
            "🔐 <strong>Jwt 불필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 레포지토리 URL 검사 성공"),
            @ApiResponse(responseCode = "VALID_4001", description = "🚨 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\\\"error\\\" : \\\"VALID_4001\\\", \\\"message\\\" : \\\"유효성 검사 실패\\\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "GIT_4001", description = "🚨 레포지토리 주소 중복",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "레포지토리 주소 중복",
                                            value = "{\\\"error\\\" : \\\"GIT_4001\\\", \\\"message\\\" : \\\"레포지토리 주소 중복\\\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "API_5031", description = "🚨 외부 API 요청 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "외부 API 요청 실패",
                                            value = "{\\\"error\\\" : \\\"API_5031\\\", \\\"message\\\" : \\\"외부 API 요청 실패\\\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "API_4221", description = "🚨 외부 API 응답 파싱 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "외부 API 응답 파싱 실패",
                                            value = "{\\\"error\\\" : \\\"API_4221\\\", \\\"message\\\" : \\\"외부 API 응답 파싱 실패\\\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/repository/validations")
    ResponseEntity<ResponseDto<Boolean>> checkRepositoryUrlAvailable(@Valid @RequestBody RepositoryUrlVerifyRequest request);
}
