package toyproject.personal.englishconversation.mapper;

import org.apache.ibatis.annotations.Mapper;
import toyproject.personal.englishconversation.domain.User;

@Mapper
public interface UserMapper {
    User findById(Long userId);
    User findByEmail(String email);
    void save(User user);
    void update(User user);
    void deleteAll();
}
