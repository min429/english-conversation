package toyproject.personal.englishconversation.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import toyproject.personal.englishconversation.config.jwt.JwtProvider;
import toyproject.personal.englishconversation.controller.dto.jwt.JwtRefreshResponseDto;
import toyproject.personal.englishconversation.domain.RefreshToken;
import toyproject.personal.englishconversation.domain.User;
import toyproject.personal.englishconversation.exception.TokenValidationException;
import toyproject.personal.englishconversation.mapper.RefreshTokenMapper;

import java.time.Duration;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtRefreshResponseDto refreshTokens(String token) {
        RefreshToken refreshToken = refreshTokenMapper.findByRefreshToken(token);
        if(refreshToken == null) {
            throw new TokenValidationException("로그인 필요");
        }

        jwtProvider.ValidateToken(token);

        Long userId = refreshToken.getUserId();
        User user = jwtUserDetailsService.getUserById(userId);

        String newAccessToken = jwtProvider.generateToken(user, Duration.ofMinutes(30), List.of("ROLE_USER"));
        String newRefreshToken = jwtProvider.generateToken(user, Duration.ofDays(7), List.of("ROLE_USER"));

        refreshTokenMapper.update(userId, newRefreshToken);

        return new JwtRefreshResponseDto(newAccessToken, newRefreshToken);
    }

    public JwtRefreshResponseDto createNewTokens(User user){
        String newAccessToken = jwtProvider.generateToken(user, Duration.ofMinutes(30), List.of("ROLE_USER"));
        String newRefreshToken = jwtProvider.generateToken(user, Duration.ofDays(7), List.of("ROLE_USER"));
        return new JwtRefreshResponseDto(newAccessToken, newRefreshToken);
    }

    public void saveOrUpdateRefreshToken(User user, String newRefreshToken){
        RefreshToken refreshToken = refreshTokenMapper.findByUserId(user.getId());
        if(refreshToken != null) {
            refreshTokenMapper.update(user.getId(), newRefreshToken);
        }
        else {
            refreshTokenMapper.save(user.getId(), newRefreshToken);
        }
    }
}