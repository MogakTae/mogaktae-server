package code.mogaktae.user.dto.req;

public record UserDocumentSaveRequest(
        Long userId,
        String nickname,
        String profileImageUrl
) {
}
