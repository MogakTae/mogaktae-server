package code.mogaktae.domain.user.repository;

import code.mogaktae.domain.user.entity.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserDocumentRepository extends ElasticsearchRepository<UserDocument, String> {

    @Query("{ \"match_phrase_prefix\": { \"nickname\": \"?0\" } }")
    List<UserDocument> findByNickname(String nickname);
}
