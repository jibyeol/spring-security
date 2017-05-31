package com.gstar.security.config;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.gstar.security.security.AuthSuccessHandler;
import com.gstar.security.security.CookieAuthenticationFilter;
import com.gstar.security.service.CookieService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
// WebSecurityConfigurerAdapter를 상속받은 클래스에 @EnableWebSecurity 어노테이션을
// 명시하면 springSecurityFilterChain가 자동으로 포함된다.
// @EnableWebSecurity : HttpSecurity 설정 제공 <http></http>를 찾을 수 있음
// @EnableGlobalMethodSecurity : AOP security on methods (PreAuthorize, PostAuthorize)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired UserDetailsService userDetailsService;
	@Autowired CookieService cookieService;
	@Autowired DataSource dataSource;
	
	String REMEMBER_ME_KEY="JIBYOLEEKEY";
	
	@Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity
            .ignoring()
                .antMatchers("/console/**", "/WEB-INF/**");
        /*
         * https://stackoverflow.com/questions/20053107/spring-security-invalid-remember-me-token-series-token-mismatch-implies-previ
         * /WEB-INF/**
         * 이거 추가한 이유는 연속으로 2번 요청을 보낼경우 CookieTheftException이 발생하는데,
         * 보니까 로그인페이지로 예를들면
         * 		/login
         * 		/WEB-INF/views/login.jsp
         * 위의 경로로 두번 요청을 보내고 있음... 왜이런지는 나두 잘... 모룸....
         */
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
        	.sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		/*
		 * SessionCreationPolicy
		 * 
		 * ALWAYS : 항상 HpptSession을 만듦
		 * If_REQUEST : 요청시에만 HttpSession을 만듦
		 * NEVER : HttpSession을 만들지 않지만 이미 존재하는 경우만 사용됨?
		 * STATELESS : HttpSession을 만들지 않고 
		 * 			   SecurityContext를 만들기 위해 HttpSession을 사용하지 않음
		 */
		
		http
			.authorizeRequests() // http.authorizeRequests() 자식이 여러개 있고 선언 된 순서대로 간주됨.
				.antMatchers("/", "/home", "/register/**").permitAll()
				.antMatchers("/admin").hasRole("ADMIN") // hasRole 메소드 호출해서 접두어 없어도 됨
				.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
				.anyRequest().hasRole("USER")
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.successHandler(authSuccessHandler())
				.and()
			.logout()
				.deleteCookies(cookieService.getCookieName()) // 로그아웃 성공시 삭제할 쿠키의 이름 지정
				.permitAll();
		
		http.rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(persistentTokenBasedRememberMeService());
		
		http.exceptionHandling().accessDeniedPage("/403");
		
		http.addFilterBefore(getCookieAuthenticationFilter(), LogoutFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		/* [in memory]
		 auth
			.inMemoryAuthentication()
				.withUser("admin").password("1234").roles("ADMIN")
				.and()
				.withUser("user").password("1234").roles("USER")
				.and()
				.withUser("dbo").password("1234").roles("ADMIN", "DBO");
		*/
		/* [jdbc]
		auth.jdbcAuthentication().dataSource(dataSource);
		*/
		
		// cutomizing
		auth.userDetailsService(userDetailsService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// 스프링 시큐리티에서 기본적으로 사용하는 암호화 방식으로 암화화가 될때마다 새로운 값을 생성한다.
		// 임의적인 값을 추가해서 암호화하지 않아도 된다.
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

	@Bean // https://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/rememberme/TokenBasedRememberMeServices.html
	public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
		TokenBasedRememberMeServices tokenBasedRememberMeServices = 
				new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
		tokenBasedRememberMeServices.setCookieName("jibyoleeCookies");
		return tokenBasedRememberMeServices;
	}
	
	public Filter getCookieAuthenticationFilter() {
		return new CookieAuthenticationFilter(userDetailsService, cookieService);
	}
	
	@Bean
	public AuthenticationSuccessHandler authSuccessHandler() {
		AuthenticationSuccessHandler handler = new AuthSuccessHandler(cookieService);
		return handler;
	}
	
	@Bean
	public ViewResolver getViewResolver(){
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/views/");
	    resolver.setSuffix(".jsp");
	    resolver.setViewClass(JstlView.class);
	    return resolver;
	}

}
