package toyproject.personal.englishconversation.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import toyproject.personal.englishconversation.exception.handler.jwt.JwtAccessDeniedHandler;
import toyproject.personal.englishconversation.exception.handler.jwt.JwtAuthenticationEntryPoint;
import toyproject.personal.englishconversation.mapper.UserMapper;
import toyproject.personal.englishconversation.service.JwtUserDetailsService;

@Profile("local")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserMapper userMapper;

    /** security 관련 필터 체인 설정(보안 설정 및 검증 관련 설정 등) **/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // REST API 방식은 Stateless 이므로 csrf 공격의 가능성이 덜함
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 암호화 사용x
                .formLogin(formLogin -> formLogin.disable()) // 폼 기반 로그인 사용x
                .logout(logout -> logout.disable()); // 기본 로그아웃 사용x

        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter(jwtProvider(jwtProperties())), UsernamePasswordAuthenticationFilter.class);

        // 인증 실패 시 수행할 작업 설정
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()));

        // 인가 실패 시 수행할 작업 설정
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.accessDeniedHandler(accessDeniedHandler()));

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider) {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtProvider jwtProvider(JwtProperties jwtProperties) {
        return new JwtProvider(jwtProperties);
    }

    /** 인증 처리 관리 및 인증 객체 전달하는 역할 수행 **/
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        // AuthenticationMagnagerBuilder 설정
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        // AuthenticationManager 생성
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    /** 비밀번호를 암호화하는 역할 수행 **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        // salt(비밀번호마다 고유한 랜덤값)를 사용하여 비밀번호 암호화(해싱)하는 인코더 // 단방향 해싱 함수라서 복호화 불가
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtUserDetailsService jwtUserDetailsService() {
        return new JwtUserDetailsService(userMapper);
    }
}
