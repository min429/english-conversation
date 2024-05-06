package toyproject.personal.englishconversation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.personal.englishconversation.controller.dto.user.SignUpRequestDto;
import toyproject.personal.englishconversation.controller.dto.user.LogInRequestDto;
import toyproject.personal.englishconversation.controller.dto.user.LogInResponseDto;
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
    public ResponseEntity<LogInResponseDto> login(@RequestBody LogInRequestDto request){
        return ResponseEntity.ok().body(userService.login(request));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(@RequestBody String email) {
        userService.delete(email);
        return ResponseEntity.ok().body("회원탈퇴 완료");
    }
}
