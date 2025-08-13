package code.mogaktae.domain.user.infrastructure;

import code.mogaktae.domain.user.dto.common.UserIdSolvedAcId;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.entity.UserDocument;
import code.mogaktae.domain.user.entity.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static code.mogaktae.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserJpaRepository userJpaRepository;
    private final UserElasticSearchRepository userElasticSearchRepository;

    //JPA
    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname);
    }

    @Override
    public UserIdSolvedAcId findUserIdAndSolvedAcIdByNickname(String nickname) {
        return userJpaRepository.findUserIdAndSolvedAcIdByNickname(nickname);
    }

    @Override
    public Boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

    @Override
    public User save(User user){
        return userJpaRepository.save(user);
    }

    // QueryDsl
    @Override
    public List<Long> findUserIdByNicknameIn(List<String> nicknames){
        return jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.nickname.in(nicknames))
                .fetch();
    }

    @Override
    public Long findUserIdByNickname(String nickname){
        return jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();
    }

    @Override
    public Optional<String> findSolvedAcIdByNickname(String nickname){
        return Optional.ofNullable(jpaQueryFactory
                .select(user.solvedAcId)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne()
        );
    }

    //Elasticsearch
    @Override
    public List<UserDocument> findByKeyword(String keyword){
        return userElasticSearchRepository.findByKeyword(keyword);
    }
}
