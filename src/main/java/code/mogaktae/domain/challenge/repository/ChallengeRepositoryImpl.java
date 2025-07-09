package code.mogaktae.domain.challenge.repository;

import code.mogaktae.domain.challenge.dto.common.ChallengeIdName;
import code.mogaktae.domain.challenge.dto.common.QChallengeIdName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static code.mogaktae.domain.challenge.entity.QChallenge.challenge;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChallengeIdName> findEndChallengeIdName(LocalDate date){
        return jpaQueryFactory
                .select(new QChallengeIdName(
                        challenge.id,
                        challenge.name
                ))
                .from(challenge)
                .where(
                        challenge.endDate.eq(date)
                )
                .fetch();
    }

}
