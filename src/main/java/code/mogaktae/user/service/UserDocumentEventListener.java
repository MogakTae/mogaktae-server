package code.mogaktae.user.service;

import code.mogaktae.user.dto.req.UserDocumentSaveRequest;
import code.mogaktae.user.entity.UserDocument;
import code.mogaktae.user.infrastructure.UserElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDocumentEventListener {

    private final UserElasticSearchRepository userElasticSearchRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void saveUserDocument(UserDocumentSaveRequest saveUserDocumentRequest) {
        UserDocument userDocument = UserDocument.create(
                saveUserDocumentRequest.userId(),
                saveUserDocumentRequest.nickname(),
                saveUserDocumentRequest.profileImageUrl()
        );
        userElasticSearchRepository.save(userDocument);
        log.info("save user success. nickname = {}", userDocument.getNickname());
    }
}
