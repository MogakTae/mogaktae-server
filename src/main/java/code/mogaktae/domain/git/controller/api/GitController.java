package code.mogaktae.domain.git.controller.api;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.git.controller.docs.GitControllerSpecification;
import code.mogaktae.domain.git.dto.req.RepositoryUrlVerifyRequest;
import code.mogaktae.domain.git.service.GitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/git")
public class GitController implements GitControllerSpecification {

    private final GitService gitService;

    @PostMapping("/repository/validations")
    public ResponseEntity<ResponseDto<Boolean>> checkRepositoryUrlAvailable(@Valid @RequestBody RepositoryUrlVerifyRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(gitService.checkRepositoryUrlAvailable(request.nickname(), request.repositoryUrl()), "레포지토리 URL 검사 완료"));
    }
}
