package com.gstar.security.security;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class UserTokenServices extends UserInfoTokenServices {

	public UserTokenServices(String userInfoEndpointUrl, String clientId) {
		super(userInfoEndpointUrl, clientId);
		setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor());
	}
	
	public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor{

		@Override
		public List<GrantedAuthority> extractAuthorities(Map<String, Object> arg0) {
			return AuthorityUtils.createAuthorityList("USER", "OAUTH");
		}
		
	}

}
