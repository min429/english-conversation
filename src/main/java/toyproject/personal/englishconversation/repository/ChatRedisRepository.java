package toyproject.personal.englishconversation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.personal.englishconversation.domain.Chat;

@Repository
public interface ChatRedisRepository extends CrudRepository<Chat, String> {
}
