package code.mogaktae.domain.alarm.dto.common;

import code.mogaktae.domain.alarm.entity.AlarmType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "알람 상세 Response")
public record AlarmDetail(

        @Schema(description = "알람 타입", example = "JOIN")
        AlarmType alarmType,

        @Schema(description = "알람 id", example = "10")
        Long alarmId,

        @Schema(description = "챌린지 id", example = "10")
        Long challengeId,

        @Schema(description = "챌린지 이름", example = "하루에 백준 하나씩")
        String challengeName,

        @Schema(description = "전송자 이름", example = "삼준서")
        String senderNickname,

        @Schema(description = "전송 일자", example = "10")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static AlarmDetail create(Long alarmId, Long challengeId, AlarmType alarmType,  String challengeName, String senderNickname, LocalDateTime createdAt){
        return AlarmDetail.builder()
                .alarmType(alarmType)
                .alarmId(alarmId)
                .challengeId(challengeId)
                .challengeName(challengeName)
                .senderNickname(senderNickname)
                .createdAt(createdAt)
                .build();
    }
}
