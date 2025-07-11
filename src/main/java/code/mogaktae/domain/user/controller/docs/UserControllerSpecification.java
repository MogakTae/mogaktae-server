package code.mogaktae.domain.user.controller.docs;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.res.UserInfoResponse;
import code.mogaktae.domain.user.entity.UserDocument;
import code.mogaktae.global.exception.error.ErrorResponse;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "UserControllerSpecification", description = "유저 관련 API")
public interface UserControllerSpecification {
    @Operation(summary = "마이페이지 정보 조회", description = "사용자의 마이페이지 정보를 조회합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 유저 정보 조회 성공"),
            @ApiResponse(responseCode = "USER_4041", description = "🚨 유저 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\\\"error\\\" : \\\"USER_4041\\\", \\\"message\\\" : \\\"유저 조회 실패\\\"}"
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
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/me")
    ResponseEntity<ResponseDto<UserInfoResponse>> getMyPageInfo(@AuthenticationPrincipal OAuth2UserDetailsImpl user);

    @Operation(summary = "사용자 검색", description = "키워드로 사용자를 검색합니다<br>" +
            "🔐 <strong>Jwt 불필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 키워드와 일치하는 유저 조회 성공")
    })
    @GetMapping("/search")
    ResponseEntity<ResponseDto<List<UserDocument>>> searchUsers(@RequestParam("keyword") String nickname);
}
