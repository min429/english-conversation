package toyproject.personal.englishconversation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.personal.englishconversation.config.jwt.JwtProvider;
import toyproject.personal.englishconversation.controller.dto.jwt.JwtRefreshResponseDto;
import toyproject.personal.englishconversation.controller.dto.user.SignUpRequestDto;
import toyproject.personal.englishconversation.controller.dto.user.SignInRequestDto;
import toyproject.personal.englishconversation.controller.dto.user.SignInResultDto;
import toyproject.personal.englishconversation.domain.RefreshToken;
import toyproject.personal.englishconversation.domain.User;
import toyproject.personal.englishconversation.exception.UserEmailException;
import toyproject.personal.englishconversation.exception.UserPasswordException;
import toyproject.personal.englishconversation.mapper.RefreshTokenMapper;
import toyproject.personal.englishconversation.mapper.UserMapper;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void save(SignUpRequestDto signUpRequestDto) {
        User user = userMapper.findByEmail(signUpRequestDto.getEmail());
        if(user != null) {
            throw new UserEmailException("아이디 중복");
        }
        userMapper.save(User.builder()
                        .email(signUpRequestDto.getEmail())
                        .password(passwordEncoder.encode(signUpRequestDto.getPassword())) // 암호화한 비밀번호 저장
                        .nickname(signUpRequestDto.getNickname())
                        .build());
    }

    public SignInResultDto login(SignInRequestDto signInRequestDto){
        User user = userMapper.findByEmail(signInRequestDto.getEmail());
        if(user == null){
            throw new UserEmailException("아이디 불일치");
        }
        /*
        1. user.password에 저장된 해시값에서 salt값을 추출해서 SignInRequestDto의 비밀번호를 해싱 (salt: 해시값의 일부)
        2. user.password에 저장된 해시값에서 비밀번호 부분을 추출 (비밀번호: 해시값의 일부)
        3. user.password에서 추출한 비밀번호와 해싱된 signInRequestDto의 비밀번호가 일치하는지 확인
        */
        if(!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())){
            throw new UserPasswordException("비밀번호 불일치");
        }

        JwtRefreshResponseDto newTokens = jwtService.createNewTokens(user);

        String newAccessToken = newTokens.getAccessToken();
        String newRefreshToken = newTokens.getRefreshToken();

        jwtService.saveOrUpdateRefreshToken(user, newRefreshToken);

        return new SignInResultDto(newAccessToken, newRefreshToken);
    }

    public void delete(String email) {
        User user = userMapper.findByEmail(email);
        log.debug("email: {}", email);
        log.debug("user.getEmail(): {}", user.getEmail());
        if(user == null){
            throw new UserEmailException("존재하지 않는 유저");
        }
        userMapper.delete(email);
    }
}
