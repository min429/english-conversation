package toyproject.personal.englishconversation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import toyproject.personal.englishconversation.domain.User;
import toyproject.personal.englishconversation.exception.UserEmailException;
import toyproject.personal.englishconversation.mapper.UserMapper;

/**
 * 사용자 인증 관련 정보를 가져오는 서비스
 */
@Transactional
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UserEmailException("아이디 불일치");
        }
        return user;
    }

    public User getUserById(Long userId) {
        User user = userMapper.findById(userId);
        if(user == null){
            throw new UserEmailException("아이디 불일치");
        }
        return user;
    }
}
