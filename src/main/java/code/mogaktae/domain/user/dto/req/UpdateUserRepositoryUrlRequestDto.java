package code.mogaktae.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(example = "유저 레포지토리 URL 업데이트 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserRepositoryUrlRequestDto {

    @Schema(
            description = "변경하고 싶은 레포지토리 Url",
            example = "https://github.com/xunxxoie/BOJ"
    )
    @NotBlank(message = "레포지토리 URL이 비어있습니다")
    @Pattern(
            regexp = "^https://github\\.com/.*$",
            message = "레포지토리 URL은 'https://github.com/'로 시작해야 합니다."
    )
    private String repositoryUrl;
}
