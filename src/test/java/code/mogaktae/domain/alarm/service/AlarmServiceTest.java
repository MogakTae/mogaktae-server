package code.mogaktae.domain.alarm.service;

import code.mogaktae.domain.alarm.entity.Alarm;
import code.mogaktae.domain.alarm.entity.AlarmType;
import code.mogaktae.domain.alarm.repository.AlarmRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @InjectMocks
    private AlarmService alarmService;

    @Mock
    private AlarmRepository alarmRepository;


    @BeforeEach
    public void before(){
        System.out.println("@BeforeEach 테스트");
    }

    @Test
    @DisplayName("두 수가 같으면 성공")
    void saveAlarm() {
        // Given
        Alarm alarm = Alarm.builder()
                .userId(1L)
                .alarmType(AlarmType.JOIN)
                .challengeName("인천대학교 컴퓨터공학부 1일 1백준 챌린지")
                .senderNickname("xunxxoie")
                .build();

        // When
    }

    @Test
    void 두_수가_같으면_실패() {
        // Given
        Integer targetValue = 10;
        doReturn(10).when(alarmService).generateNumber();

        // When
        Integer value = alarmService.generateNumber();

        // Then
        assertThat(value).isNotEqualTo(alarmService.generateNumber());
    }

    @AfterEach
    public void after(){
        System.out.println("@AfterEach 테스트");
    }
}