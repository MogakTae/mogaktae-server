package code.mogaktae.domain.git;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/github-webhook")
public class GitController {

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        log.info("Received GitHub Webhook Payload: {}", payload);

        String ref = (String) payload.get("ref");
        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        String repoName = (repository != null) ? (String) repository.get("full_name") : "Unknown";
        Map<String, Object> headCommit = (Map<String, Object>) payload.get("head_commit");
        String commitMessage = (headCommit != null) ? (String) headCommit.get("message") : "No commit message";

        log.info("Branch: {}", ref);
        log.info("Repository: {}", repoName);
        log.info("Commit Message: {}", commitMessage);

        return ResponseEntity.ok("Webhook received");
    }
}
