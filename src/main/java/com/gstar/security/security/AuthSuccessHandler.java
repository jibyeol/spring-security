package com.gstar.security.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gstar.security.service.CookieService;

public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	CookieService cookieService;
	
	public AuthSuccessHandler(CookieService cookieService){
		this.cookieService = cookieService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		Object principalObj = context.getAuthentication().getPrincipal();
		String principal = ((UserDetailsImpl) principalObj).getUsername();
		
		response.addCookie(cookieService.createCookie(principal));
		response.sendRedirect("/");
	}

}
