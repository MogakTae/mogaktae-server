package code.mogaktae.domain.alarm.dto.res;

import code.mogaktae.domain.alarm.dto.common.AlarmDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "알람 조회 Response")
public record AlarmResponseDto(

        @Schema(description = "총 알람 수", example = "4")
        int totalAlarm,

        List<AlarmDetail> alarmDetail
) {
    public static AlarmResponseDto create(List<AlarmDetail> alarmDetail){
        return AlarmResponseDto.builder()
                .totalAlarm(alarmDetail.size())
                .alarmDetail(alarmDetail)
                .build();
    }
}
