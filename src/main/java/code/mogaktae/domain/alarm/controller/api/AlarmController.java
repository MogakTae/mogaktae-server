package code.mogaktae.domain.alarm.controller.api;

import code.mogaktae.auth.domain.UserDetailsImpl;
import code.mogaktae.domain.alarm.controller.docs.AlarmControllerSpecification;
import code.mogaktae.domain.alarm.dto.res.AlarmResponseDto;
import code.mogaktae.domain.alarm.service.AlarmService;
import code.mogaktae.domain.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/alarms")
public class AlarmController implements AlarmControllerSpecification {

    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<ResponseDto<AlarmResponseDto>> getAllAlarm(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(alarmService.getAlarms(user.getUsername()), "알림 조회 성공"));
    }
}
