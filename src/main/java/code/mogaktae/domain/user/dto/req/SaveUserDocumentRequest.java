package code.mogaktae.domain.user.dto.req;

public record SaveUserDocumentRequest(
        Long userId,
        String nickname,
        String profileImageUrl
) {
}
