package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.common.ChallengePersonalResult;
import code.mogaktae.domain.challenge.dto.common.ChallengeSummary;
import code.mogaktae.domain.challenge.dto.common.QChallengePersonalResult;
import code.mogaktae.domain.challenge.dto.common.QChallengeSummary;
import code.mogaktae.domain.userChallenge.dto.common.QUserChallengeSummary;
import code.mogaktae.domain.userChallenge.dto.common.UserChallengeSummary;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
import code.mogaktae.domain.userChallenge.entity.UserChallengeRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static code.mogaktae.domain.challenge.entity.QChallenge.challenge;
import static code.mogaktae.domain.user.entity.QUser.user;
import static code.mogaktae.domain.userChallenge.entity.QUserChallenge.userChallenge;

@Repository
@RequiredArgsConstructor
public class UserChallengeRepositoryImpl implements UserChallengeRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserChallengeJpaRepository userChallengeJpaRepository;

    //JPA
    @Override
    public Boolean existsByUserIdAndChallengeId(Long userId, Long challengeId) {
        return userChallengeJpaRepository.existsByUserIdAndChallengeId(userId, challengeId);
    }

    //QueryDsl
    @Override
    public List<ChallengePersonalResult> findPersonalResultByChallengeId(Long challengeId){
        return jpaQueryFactory
                .select(new QChallengePersonalResult(
                        user.solvedAcId,
                        user.id,
                        user.profileImageUrl,
                        user.nickname,
                        userChallenge.startTier,
                        userChallenge.totalPenalty,
                        userChallenge.totalSolvedProblem))
                .from(userChallenge)
                .join(user).on(userChallenge.userId.eq(user.id))
                .where(userChallenge.challengeId.eq(challengeId))
                .fetch();
    }

    @Override
    public Long countUserChallenge(Long userId){
        Long count = jpaQueryFactory
                .select(userChallenge.count())
                .from(userChallenge)
                .where(
                        userChallenge.userId.eq(userId),
                        userChallenge.isEnd.isFalse()
                )
                .fetchOne();

        return count != null ? count : Long.MAX_VALUE;
    }

    @Override
    public List<ChallengeSummary> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isEnd){
        return jpaQueryFactory
                .select(new QChallengeSummary(
                        challenge.id,
                        challenge.challengeImageUrl,
                        challenge.name,
                        challenge.dailyProblem,
                        challenge.startDate,
                        challenge.endDate
                ))
                .from(userChallenge)
                .join(challenge).on(userChallenge.challengeId.eq(challenge.id))
                .where(
                        userChallenge.userId.eq(userId),
                        userChallenge.isEnd.eq(isEnd)
                )
                .fetch();
    }

    @Override
    public List<UserChallengeSummary> findUserChallengeSummariesByChallengeId(Long challengeId, Long dailyProblem){
        return jpaQueryFactory
                .select(new QUserChallengeSummary(
                        user.profileImageUrl,
                        userChallenge.repositoryUrl,
                        user.nickname,
                        userChallenge.totalPenalty,
                        userChallenge.todaySolvedProblem,
                        userChallenge.isEnd
                ))
                .from(userChallenge)
                .join(user).on(userChallenge.userId.eq(user.id))
                .where(
                        userChallenge.challengeId.eq(challengeId),
                        userChallenge.isEnd.isFalse()
                )
                .fetch();
    }

    @Override
    public Optional<UserChallenge> findByUserNicknameAndRepositoryUrl(String nickname, String repositoryUrl) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(userChallenge)
                .join(user).on(userChallenge.userId.eq(user.id))
                .where(
                        user.nickname.eq(nickname)
                                .and(userChallenge.repositoryUrl.eq(repositoryUrl))
                                .and(userChallenge.isEnd.isFalse())
                )
                .fetchOne());
    }

    @Override
    public List<UserChallenge> findAllByIsCompleted(){
        return jpaQueryFactory
                .select(userChallenge)
                .where(userChallenge.isEnd.isTrue())
                .fetch();
    }

    @Override
    public Boolean existsByRepositoryUrl(String repositoryUrl){
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(userChallenge)
                .where(userChallenge.repositoryUrl.eq(repositoryUrl))
                .fetchOne();

        return fetchOne != null;
    }
}
