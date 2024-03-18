package toyproject.personal.englishconversation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(NoSecurityTestConfig.class)
class EnglishConversationApplicationTests {

	@Test
	void contextLoads() {
	}
}
