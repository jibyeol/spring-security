package com.gstar.security.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.filter.CompositeFilter;

import com.gstar.security.repository.AccountRepository;
import com.gstar.security.security.OAuth2SuccessHandler;
import com.gstar.security.security.UserTokenServices;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {
	
	@Autowired OAuth2ClientContext oauth2ClientContext;
	@Autowired AccountRepository accountService;
	
	@Bean
	@ConfigurationProperties("facebook.client")
	AuthorizationCodeResourceDetails facebook(){
		return new AuthorizationCodeResourceDetails();
	}

	
	@Bean
	@ConfigurationProperties("facebook.resource")
	ResourceServerProperties facebookResource(){
		return new ResourceServerProperties();
	}
	
	@Bean
	@ConfigurationProperties("naver.clinet")
	AuthorizationCodeResourceDetails naver(){
		return new AuthorizationCodeResourceDetails();
	}
	
	@Bean
	@ConfigurationProperties("naver.resource")
	ResourceServerProperties naverResource(){
		return new ResourceServerProperties();
	}
	
	@Bean
	FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter){
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setOrder(-100); // 다른 필터보다 우선순위를 주려고
		return registration;
	}
	
	@Bean("sso.filter")
	Filter ssoFilter(){
		List<Filter> filters = new ArrayList<>();
		
		// facebook
		OAuth2ClientAuthenticationProcessingFilter facebook = new OAuth2ClientAuthenticationProcessingFilter("/sign-in/facebook");
		facebook.setRestTemplate(new OAuth2RestTemplate(facebook(), oauth2ClientContext));
		facebook.setTokenServices(new UserTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId()));
		facebook.setAuthenticationSuccessHandler(new OAuth2SuccessHandler("facebook", accountService));
		filters.add(facebook);
		
		// naver
		OAuth2ClientAuthenticationProcessingFilter naver
			= new OAuth2ClientAuthenticationProcessingFilter("/sign-in/naver");
		naver.setRestTemplate(new OAuth2RestTemplate(naver(), oauth2ClientContext));
		naver.setTokenServices(new UserTokenServices(naverResource().getUserInfoUri(), naver().getClientId()));
		naver.setAuthenticationSuccessHandler(new OAuth2SuccessHandler("naver", accountService));
		filters.add(naver);
	
		CompositeFilter filter = new CompositeFilter();
		filter.setFilters(filters);
		return filter;
	}
}
