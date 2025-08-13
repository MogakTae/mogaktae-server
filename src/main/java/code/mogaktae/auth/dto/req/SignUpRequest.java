package code.mogaktae.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "회원가입 Request")
public record SignUpRequest(

        @Schema(description = "깃허브 닉네임", example = "xunxxoie")
        @NotBlank(message = "깃허브 닉네임이 비어있습니다.")
        String nickname,

        @Schema(description = "프로필 이미지 Url", example = "https://avatars.githubusercontent.com/u/146558936?v=4")
        @Pattern(regexp = "^https://avatars\\.githubusercontent\\.com/.*$", message = "유효하지 않은 이미지 url 주소입니다.")
        @NotBlank(message = "profileImageUrl이 비어있습니다.")
        String profileImageUrl,

        @Schema(description = "백준 아이디", example = "ahh052")
        @NotBlank(message = "백준 아이디가 비어있습니다.")
        String solvedAcId
) {}