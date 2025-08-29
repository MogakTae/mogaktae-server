package code.mogaktae.git.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "깃허브 레포지토리 URL 유효성 검사 Request")
public record RepositoryUrlVerifyRequest(

        @Schema(description = "닉네임", example = "xunxxoie")
        @NotBlank(message = "닉네임이 비어있습니다.")
        String nickname,

        @Schema(description = "레포지토리 URL", example = "https://github.com/xunxxoie/Haruni-server")
        @Pattern(regexp = "^https://github\\.com/.*$", message = "레포지토리 URL 은 'https://github.com/'로 시작해야 합니다.")
        String repositoryUrl
) {}