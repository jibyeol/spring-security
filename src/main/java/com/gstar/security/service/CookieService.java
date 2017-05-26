package com.gstar.security.service;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

@Service
public class CookieService {

	String cookieName = "authenticationCookie";
	
	public String extractUsername(Cookie[] cookies) {
		if(cookies == null) return null;
		for(Cookie cookie : cookies){
			if(cookie.getName().trim().equals(cookieName)){
				return cookie.getValue();
			}
		}
		return null;
	}

	public Cookie createCookie(String principal) {
		return new Cookie(cookieName, principal);
	}
	
	public String getCookieName(){
		return cookieName;
	}

}
