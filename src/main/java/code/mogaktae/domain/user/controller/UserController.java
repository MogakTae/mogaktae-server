package code.mogaktae.domain.user.controller;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
import code.mogaktae.domain.user.entity.User;
import code.mogaktae.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getMyPageInfo(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getMyPageInfo(user), "유저 정보 조회 성공"));
    }
}