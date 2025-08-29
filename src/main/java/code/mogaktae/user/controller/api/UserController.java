package code.mogaktae.user.controller.api;

import code.mogaktae.auth.domain.UserDetailsImpl;
import code.mogaktae.common.dto.ResponseDto;
import code.mogaktae.user.controller.docs.UserControllerSpecification;
import code.mogaktae.user.dto.res.MyPageResponse;
import code.mogaktae.user.entity.UserDocument;
import code.mogaktae.user.service.UserService;
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
@RequestMapping("/api/v2/users")
public class UserController implements UserControllerSpecification {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<MyPageResponse>> getMyPage(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getMyPage(user.getUsername()), "유저 정보 조회 성공"));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<UserDocument>>> searchUsers(@RequestParam("keyword") String keyword){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.searchUsers(keyword),"키워드와 일치하는 유저 조회 성공"));
    }

}