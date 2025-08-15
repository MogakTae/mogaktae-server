package code.mogaktae.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor
@Document(indexName = "user")
@Mapping(mappingPath = "static/elasticsearch/elastic-mapping.json")
@Setting(settingPath = "static/elasticsearch/elastic-token.json")
public class UserDocument {

    @Id
    private Long userId;

    @Field(type = FieldType.Text, analyzer = "nickname_index_analyzer", searchAnalyzer = "nickname_search_analyzer")
    private String nickname;

    @Field(type = FieldType.Keyword)
    private String profileImageUrl;

    @Builder
    private UserDocument(Long userId, String nickname, String profileImageUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserDocument create(Long userId, String nickname, String profileImageUrl) {
        return UserDocument.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}