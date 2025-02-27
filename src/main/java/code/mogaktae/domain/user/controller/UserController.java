package code.mogaktae.domain.user.controller;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.req.UpdateUserRepositoryUrlRequestDto;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getMyPageInfo(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getMyPageInfo(user), "유저 정보 조회 성공"));
    }

    @PutMapping("/repository-url")
    public ResponseEntity<ResponseDto<String>> updateRepositoryUrl(@AuthenticationPrincipal User user,
                                                                   @Valid @RequestBody UpdateUserRepositoryUrlRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.updateRepositoryUrl(user, request), "레포지토리 URL 변경 완료"));
    }
}