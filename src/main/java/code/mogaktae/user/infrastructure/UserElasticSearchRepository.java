package code.mogaktae.user.infrastructure;

import code.mogaktae.user.entity.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserElasticSearchRepository extends ElasticsearchRepository<UserDocument, String> {
    @Query("{ \"match\": { \"nickname\": \"?0\" } }")
    List<UserDocument> findByKeyword(String keyword);
}
