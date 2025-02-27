package code.mogaktae.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {

    @Schema(
            description = "닉네임",
            example = "xunxxoie"
    )
    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    @Schema(
            description = "레포지토리 Url",
            example = "https://github.com/xunxxoie/Haruni-server"
    )
    @Pattern(
            regexp = "^https://github\\.com/.*$",
            message = "레포지토리 Url 은 'https://github.com/'로 시작해야 합니다."
    )
    @NotBlank(message = "레포지토리 Url 이 비어있습니다.")
    private String repositoryUrl;

    @Schema(
            description = "프로필 이미지 Url",
            example = "https://avatars.githubusercontent.com/u/146558936?v=4"
    )
    @Pattern(
            regexp = "^https://avatars\\.githubusercontent\\.com/.*$",
            message = "프로필 이미지 Url 은 'https://avatars.githubusercontent.com/'로 시작해야 합니다."
    )
    @NotBlank(message = "프로필 이미지 Url 이 비어있습니다.")
    private String profileImageUrl;
}
