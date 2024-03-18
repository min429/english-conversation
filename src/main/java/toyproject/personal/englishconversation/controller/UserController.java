package toyproject.personal.englishconversation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.personal.englishconversation.controller.dto.user.SignUpRequestDto;
import toyproject.personal.englishconversation.controller.dto.user.SignInRequestDto;
import toyproject.personal.englishconversation.controller.dto.user.SignInResultDto;
import toyproject.personal.englishconversation.service.UserService;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto request) {
        userService.save(request);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResultDto> login(@RequestBody SignInRequestDto request){
        return ResponseEntity.ok().body(userService.login(request));
    }

    @PostMapping("/signout")
    public void signout() {
        // 회원 탈퇴 구현 예정
    }
}
