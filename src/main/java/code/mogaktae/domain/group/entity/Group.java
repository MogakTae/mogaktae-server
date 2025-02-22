package code.mogaktae.domain.group.entity;

import code.mogaktae.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "groups")
public class Group {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "dead_line")
    private LocalDate deadLine;

    @Column(nullable = false, name = "daily_problem_count")
    private Integer dailyProblemCount;

    @Column(nullable = false, name = "penalty_amount")
    private Long penaltyAmount;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<User> users = new ArrayList<>();

}
