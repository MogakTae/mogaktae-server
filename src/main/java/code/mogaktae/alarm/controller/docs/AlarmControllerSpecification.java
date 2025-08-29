package code.mogaktae.alarm.controller.docs;

import code.mogaktae.auth.domain.UserDetailsImpl;
import code.mogaktae.alarm.dto.res.AlarmResponseDto;
import code.mogaktae.common.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "AlarmController", description = "알림 API")
public interface AlarmControllerSpecification {

    @Operation(summary = "알림 목록 조회", description = "사용자의 모든 알림 목록을 조회합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 알림 조회 성공")
    })
    @GetMapping
    ResponseEntity<ResponseDto<AlarmResponseDto>> getAllAlarm(@AuthenticationPrincipal UserDetailsImpl user);
}