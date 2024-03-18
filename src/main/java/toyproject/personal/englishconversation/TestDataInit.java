//package toyproject.personal.englishconversation;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import toyproject.personal.englishconversation.domain.User;
//import toyproject.personal.englishconversation.mapper.UserMapper;
//
//@RequiredArgsConstructor
//public class TestDataInit {
//
//    private final UserMapper userMapper;
//
//    /** 초기 데이터 추가 **/
//    @EventListener(ApplicationReadyEvent.class)
//    public void initData() {
//        userMapper.save(User.builder()
//                .email("abc@gmail.com")
//                .password("123")
//                .nickname("test1")
//                .build());
//        userMapper.save(User.builder()
//                .email("def@gmail.com")
//                .password("456")
//                .nickname("test2")
//                .build());
//    }
//}
