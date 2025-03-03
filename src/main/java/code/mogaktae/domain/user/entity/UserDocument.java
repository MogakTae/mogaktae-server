package code.mogaktae.domain.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "user")
@Mapping(mappingPath = "static/elastic-mapping.json")
@Setting(settingPath = "static/elastic-token.json")
public class UserDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "edge_ngram_analyzer", searchAnalyzer = "standard")
    private String nickname;

    @Field(type = FieldType.Keyword)
    private String profileImageUrl;

    @Field(type = FieldType.Keyword)
    private String repositoryUrl;
}