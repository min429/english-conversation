package toyproject.personal.englishconversation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import toyproject.personal.englishconversation.domain.Chat;

import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ChatCustomRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRedisRepository chatRedisRepository;

    public void save(Chat chat) {
        String key = chat.getKey();
        double score = chat.getTimestamp().toEpochSecond(ZoneOffset.UTC);

        chatRedisRepository.save(chat);
        // 채팅 시간에 따라 키 값 저장 (해시 데이터는 순서를 보장하지 않으므로 데이터 순서를 별도로 관리해주어야 함)
        redisTemplate.opsForZSet().add("chat:timestamps:" + chat.getUserId(), key, score);
    }

    public Optional<Chat> findChatById(String id) {
        return chatRedisRepository.findById(id);
    }

    public List<Chat> findAllChatsByUserId(Long userId) {
        Set<String> keys = redisTemplate.opsForZSet().rangeByScore("chat:timestamps:" + userId, Double.MIN_VALUE, Double.MAX_VALUE)
                .stream().map(Object::toString).collect(Collectors.toSet());

        List<Chat> chats = new LinkedList<>();
        for (String key : keys) {
            chats.add(chatRedisRepository.findById(key)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅 리스트")));
        }
        return chats;
    }

    public Long generateMessageId(Long userId) {
        String key = "messageId:"+userId;
        return redisTemplate.opsForValue().increment(key);
    }
}
