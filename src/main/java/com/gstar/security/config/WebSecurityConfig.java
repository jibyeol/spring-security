package com.gstar.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity // @EnableWebSecurity과 WebSecurityConfigurerAdapter는 웹기반 보안을 위해 함께 작동한다.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired DataSource dataSource;
	
	@Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity
            .ignoring()
                .antMatchers("/h2console/**");
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests() // http.authorizeRequests() 자식이 여러개 있고 선언 된 순서대로 간주됨.
				.antMatchers("/", "/home", "/abc", "/jspTest", "/test").permitAll()
				.antMatchers("/admin").hasRole("ADMIN") // hasRole 메소드 호출해서 접두어 없어도 됨
				.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				//.logoutUrl("/custom/logout") // logout URL 바꿀 수 있음
				//.logoutSuccessUrl("/custom/main") // logout 성공시 redirect될 주소 (default : login?logout)
				//.logoutSuccessHandler(logoutSuccessHandler) // logout 성공시 호출됨. 이거 있으면 .logoutSuccessUrl 무시됨
				//.invalidateHttpSession(true) // 로그아웃시 HttpSession을 무효화할지 여부 (default : true)
				//.addLogoutHandler(logoutHandler) // SecurityContextLogoutHandler는 기본적으로 마지막 logoutHandler로 추가됨
				//.deleteCookie(cookieNamesToClear) // 로그아웃 성공시 삭제할 쿠키의 이름 지정
				.permitAll()
			.and()
                .headers()
                    .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","script-src 'self'"))
                    .frameOptions().disable();
		/**
		 * LogoutHandler -- http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-logout-handler
		 */
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource);
	}

}
