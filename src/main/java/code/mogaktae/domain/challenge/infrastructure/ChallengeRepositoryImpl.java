package code.mogaktae.domain.challenge.infrastructure;

import code.mogaktae.domain.challenge.dto.common.ChallengeIdName;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.dto.common.QChallengeIdName;
import code.mogaktae.domain.challenge.entity.Challenge;
import code.mogaktae.domain.challenge.entity.ChallengeRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static code.mogaktae.domain.challenge.entity.QChallenge.challenge;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChallengeJpaRepository challengeJpaRepository;

    //JPA
    @Override
    public Page<ChallengeSummary> findByIdLessThanOrderByIdDesc(Long lastCursorId, Pageable pageable){
        return challengeJpaRepository.findByIdLessThanOrderByIdDesc(lastCursorId, pageable);
    }

    @Override
    public Long findDailyProblemByChallengeId(Long challengeId) {
        return challengeJpaRepository.findDailyProblemByChallengeId(challengeId);
    }

    @Override
    public Optional<Challenge> findById(Long challengeId) {
        return challengeJpaRepository.findById(challengeId);
    }

    @Override
    public Challenge save(Challenge challenge){
        return challengeJpaRepository.save(challenge);
    }

    @Override
    public long count(){
        return challengeJpaRepository.count();
    }

    //QueryDsl
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
