package code.mogaktae.domain.challenge.repository;

import code.mogaktae.domain.challenge.dto.common.ChallengeIdName;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepositoryCustom {

    List<ChallengeIdName> findEndChallengeIdName(LocalDate date);
}
