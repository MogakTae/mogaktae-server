package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse;
import code.mogaktae.domain.challenge.dto.res.QChallengeInfoSummaryResponse;
import code.mogaktae.domain.challenge.dto.common.QUserChallengeSummary;
import code.mogaktae.domain.challenge.dto.common.UserChallengeSummary;
import code.mogaktae.domain.result.dto.common.PersonalResult;
import code.mogaktae.domain.result.dto.common.QPersonalResult;
import code.mogaktae.domain.userChallenge.entity.UserChallenge;
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
public class UserChallengeRepositoryImpl implements UserChallengeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PersonalResult> findPersonalResultByChallengeId(Long challengeId){
        return jpaQueryFactory
                .select(new QPersonalResult(
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
                        userChallenge.isCompleted.isFalse()
                )
                .fetchOne();

        return count != null ? count : Long.MAX_VALUE;
    }

    @Override
    public List<ChallengeInfoSummaryResponse> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isCompleted){
        return jpaQueryFactory
                .select(new QChallengeInfoSummaryResponse(
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
                        userChallenge.isCompleted.eq(isCompleted)
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
                        userChallenge.isCompleted
                ))
                .from(userChallenge)
                .join(user).on(userChallenge.userId.eq(user.id))
                .where(
                        userChallenge.challengeId.eq(challengeId),
                        userChallenge.isCompleted.isFalse()
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
                                .and(userChallenge.isCompleted.eq(false))
                )
                .fetchOne());
    }

    @Override
    public List<UserChallenge> findAllByIsCompleted(){
        return jpaQueryFactory
                .select(userChallenge)
                .where(userChallenge.isCompleted.eq(false))
                .fetch();
    }
}
