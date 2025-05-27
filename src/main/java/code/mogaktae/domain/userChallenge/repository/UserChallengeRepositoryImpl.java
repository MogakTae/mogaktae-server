package code.mogaktae.domain.userChallenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.dto.res.QChallengeSummaryResponseDto;
import code.mogaktae.domain.challenge.dto.res.QUserChallengeSummaryDto;
import code.mogaktae.domain.challenge.dto.res.UserChallengeSummaryDto;
import code.mogaktae.domain.result.dto.res.PersonalResultDto;
import code.mogaktae.domain.result.dto.res.QPersonalResultDto;
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
    public List<PersonalResultDto> findPersonalResultByChallengeId(Long challengeId){
        return jpaQueryFactory
                .select(new QPersonalResultDto(
                        user.solvedAcId,
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
    public Boolean existsByNicknameAndChallengeId(String nickname, Long challengeId){
        return jpaQueryFactory
                .selectOne()
                .from(userChallenge)
                .join(user).on(userChallenge.userId.eq(user.id))
                .where(
                        userChallenge.challengeId.eq(challengeId),
                        user.nickname.eq(nickname)
                )
                .fetchFirst() != null;
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
    public List<ChallengeSummaryResponseDto> findChallengesByUserIdAndIsCompleted(Long userId, Boolean isCompleted){
        return jpaQueryFactory
                .select(new QChallengeSummaryResponseDto(
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
    public List<UserChallengeSummaryDto> findUserChallengeSummariesByChallengeId(Long challengeId){
        return jpaQueryFactory
                .select(new QUserChallengeSummaryDto(
                        user.profileImageUrl,
                        userChallenge.repositoryUrl,
                        user.nickname,
                        userChallenge.totalPenalty,
                        userChallenge.todaySolved
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
