package toyproject.personal.englishconversation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.personal.englishconversation.controller.dto.jwt.JwtRefreshRequestDto;
import toyproject.personal.englishconversation.controller.dto.jwt.JwtRefreshResponseDto;
import toyproject.personal.englishconversation.service.JwtService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<JwtRefreshResponseDto> refreshTokens(@RequestBody JwtRefreshRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(jwtService.refreshTokens(request.getRefreshToken()));
    }
}
