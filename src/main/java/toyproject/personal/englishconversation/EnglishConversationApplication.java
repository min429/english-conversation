package toyproject.personal.englishconversation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import toyproject.personal.englishconversation.mapper.UserMapper;

import javax.sql.DataSource;

@SpringBootApplication
public class EnglishConversationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnglishConversationApplication.class, args);
	}

//	/** 메모리 데이터베이스 데이터 초기화 **/
//	@Bean
//	@Profile("test")
//	public TestDataInit testDataInit(UserMapper userMapper) {
//		return new TestDataInit(userMapper);
//	}

	/** 메모리 데이터베이스 설정 **/
	@Bean
	@Profile("test")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:english-conversation;DB_CLOSE_DELAY=-1"); // 스키마 이름에 '_' 사용 불가
		dataSource.setUsername("root");
		dataSource.setPassword("1234");
		return dataSource;
	}
}
