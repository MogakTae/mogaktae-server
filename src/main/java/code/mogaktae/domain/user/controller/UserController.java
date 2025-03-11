package code.mogaktae.domain.user.controller;

import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.user.dto.res.UserInfoResponseDto;
import code.mogaktae.domain.user.entity.UserDocument;
import code.mogaktae.domain.user.service.UserService;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getMyPageInfo(@AuthenticationPrincipal OAuth2UserDetailsImpl user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getMyPageInfo(user), "유저 정보 조회 성공"));
    }

    @GetMapping("/suggest")
    public ResponseEntity<ResponseDto<List<UserDocument>>> searchUsers(@RequestParam("keyword") String nickname){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.searchUsers(nickname),"키워드와 일치하는 유저 조회 성공"));
    }
}