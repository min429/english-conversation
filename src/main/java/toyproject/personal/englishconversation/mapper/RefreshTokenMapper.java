package toyproject.personal.englishconversation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toyproject.personal.englishconversation.domain.RefreshToken;

@Mapper
public interface RefreshTokenMapper {
    RefreshToken findByUserId(Long userId);
    RefreshToken findByRefreshToken(String refreshToken);
    void save(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    void update(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);
}