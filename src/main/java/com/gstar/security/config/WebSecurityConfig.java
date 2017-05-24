package com.gstar.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired UserDetailsService userDetailsService;
	@Autowired DataSource dataSource;
	
	String REMEMBER_ME_KEY="JIBYOLEEKEY";
	
	@Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity
            .ignoring()
                .antMatchers("/console/**");
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests() // http.authorizeRequests() 자식이 여러개 있고 선언 된 순서대로 간주됨.
				.antMatchers("/", "/home", "/register/**").permitAll()
				.antMatchers("/admin").hasRole("ADMIN") // hasRole 메소드 호출해서 접두어 없어도 됨
				.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
				.anyRequest().authenticated()
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
				.permitAll();
		http.rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(persistentTokenBasedRememberMeService());
		http.exceptionHandling().accessDeniedPage("/403");
		/**
		 * LogoutHandler -- http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-logout-handler
		 */
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeService(){
		PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
				new PersistentTokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService, jdbcTokenRepository());
		return persistentTokenBasedRememberMeServices;
	}
	
	@Bean
	public JdbcTokenRepositoryImpl jdbcTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setCreateTableOnStartup(false);
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}

	@Bean
	public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
		TokenBasedRememberMeServices tokenBasedRememberMeServices = 
				new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
		tokenBasedRememberMeServices.setCookieName("jibyoleeCookies");
		return tokenBasedRememberMeServices;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource);
		
//		auth
//			.inMemoryAuthentication()
//				.withUser("admin").password("1234").roles("ADMIN")
//				.and()
//				.withUser("user").password("1234").roles("USER")
//				.and()
//				.withUser("dbo").password("1234").roles("ADMIN", "DBO");
		
		auth.userDetailsService(userDetailsService);
	}

}
