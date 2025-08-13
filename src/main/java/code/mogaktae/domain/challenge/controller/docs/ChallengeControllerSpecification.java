package code.mogaktae.domain.challenge.controller.docs;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeSummariesRequest;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequest;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailResponse;
import code.mogaktae.domain.challenge.dto.res.ChallengeSummariesResponse;
import code.mogaktae.domain.challenge.entity.ChallengeResult;
import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.global.exception.error.ErrorResponse;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "ChallengeController", description = "챌린지 API")
public interface ChallengeControllerSpecification {

    @Operation(summary = "챌린지 생성", description = "새로운 챌린지를 생성합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "✅ 챌린지 생성 성공"),
            @ApiResponse(responseCode = "USER_4041", description = "🚨 유저 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"USER_4041\", \"message\" : \"유저 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "CHALLENGE_4001", description = "🚨 챌린지 참여 한도 초과",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지 참여 한도 초과",
                                            value = "{\"error\" : \"CHALLENGE_4001\", \"message\" : \"챌린지 참여 한도 초과\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "API_5031", description = "🚨 외부 API 요청 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "외부 API 요청 실패",
                                            value = "{\"error\" : \"API_5031\", \"message\" : \"외부 API 요청 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "VALID_4001", description = "🚨 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"VALID_4001\", \"message\" : \"유효성 검사 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    ResponseEntity<ResponseDto<Long>> createChallenge(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                      @Valid @RequestBody ChallengeCreateRequest request);

    @Operation(summary = "챌린지 요약 조회", description = "메인 페이지용 챌린지 요약 정보를 조회합니다<br>" +
            "🔐 <strong>Jwt 불필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 챌린지 요약 조회 성공"),
            @ApiResponse(responseCode = "VALID_4001", description = "🚨 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"VALID_4001\", \"message\" : \"유효성 검사 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    ResponseEntity<ResponseDto<ChallengeSummariesResponse>> getChallengeSummaries(@Valid @RequestBody ChallengeSummariesRequest request);

    @Operation(summary = "챌린지 상세 조회", description = "특정 챌린지의 상세 정보를 조회합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 챌린지 상세 조회 성공"),
            @ApiResponse(responseCode = "CHALLENGE_4041", description = "🚨 챌린지 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지 조회 실패",
                                            value = "{\"error\" : \"CHALLENGE_4041\", \"message\" : \"챌린지 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "USER_4011", description = "🚨 챌린지 접근 권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지 접근 권한 없음",
                                            value = "{\"error\" : \"USER_4011\", \"message\" : \"챌린지 접근 권한 없음\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{challengeId}")
    ResponseEntity<ResponseDto<ChallengeDetailResponse>> getChallengeDetail(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                            @PathVariable Long challengeId);

    @Operation(summary = "챌린지 참여", description = "기존 챌린지에 참여합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 챌린지 참여 성공"),
            @ApiResponse(responseCode = "USER_4041", description = "🚨 유저 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"USER_4041\", \"message\" : \"유저 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "CHALLENGE_4041", description = "🚨 챌린지 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지 조회 실패",
                                            value = "{\"error\" : \"CHALLENGE_4041\", \"message\" : \"챌린지 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "API_5031", description = "🚨 외부 API 요청 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "외부 API 요청 실패",
                                            value = "{\"error\" : \"API_5031\", \"message\" : \"외부 API 요청 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "VALID_4001", description = "🚨 유효성 검사 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"VALID_4001\", \"message\" : \"유효성 검사 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/participants")
    ResponseEntity<ResponseDto<Long>> joinChallenge(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                    @Valid @RequestBody ChallengeJoinRequest request);

    @Operation(summary = "챌린지 결과 조회", description = "완료된 챌린지의 결과를 조회합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 챌린지 결과 조회 성공"),
            @ApiResponse(responseCode = "USER_4041", description = "🚨 유저 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"USER_4041\", \"message\" : \"유저 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "USER_4011", description = "🚨 챌린지 접근 권한 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지 접근 권한 없음",
                                            value = "{\"error\" : \"USER_4011\", \"message\" : \"챌린지 접근 권한 없음\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "CHALLENGE_4043", description = "🚨 챌린지 결과 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지 결과 조회 실패",
                                            value = "{\"error\" : \"CHALLENGE_4043\", \"message\" : \"챌린지 결과 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/results/{challengeId}")
    ResponseEntity<ResponseDto<ChallengeResult>> getChallengeResult(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                    @PathVariable Long challengeId);

    @Operation(summary = "깃허브 푸시 웹훅 처리", description = "깃허브에서 전송되는 푸시 이벤트를 처리합니다<br>" +
            "🔐 <strong>Jwt 불필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 웹훅 처리 성공"),
            @ApiResponse(responseCode = "CHALLENGE_4002", description = "🚨 문제 미해결",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "문제 미해결",
                                            value = "{\"error\" : \"CHALLENGE_4002\", \"message\" : \"문제 미해결\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "GIT_4002", description = "🚨 커밋 메시지 형식 불일치",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "커밋 메시지 형식 불일치",
                                            value = "{\"error\" : \"GIT_4002\", \"message\" : \"커밋 메시지 형식 불일치\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "USER_4041", description = "🚨 유저 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"USER_4041\", \"message\" : \"유저 조회 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "CHALLENGE_4042", description = "🚨 챌린지에 참여하지 않은 유저",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "챌린지에 참여하지 않은 유저",
                                            value = "{\"error\" : \"CHALLENGE_4042\", \"message\" : \"챌린지에 참여하지 않은 유저\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "API_5031", description = "🚨 외부 API 요청 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "외부 API 요청 실패",
                                            value = "{\"error\" : \"API_5031\", \"message\" : \"외부 API 요청 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "API_4221", description = "🚨 외부 API 응답 파싱 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "외부 API 응답 파싱 실패",
                                            value = "{\"error\" : \"API_4221\", \"message\" : \"외부 API 응답 파싱 실패\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/webhooks/push")
    ResponseEntity<ResponseDto<?>> pushCodingTestCommit(@RequestBody Map<String, Object> request);
}