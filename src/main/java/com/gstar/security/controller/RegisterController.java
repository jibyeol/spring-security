package com.gstar.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gstar.security.model.Account;
import com.gstar.security.repository.AccountRepository;
import com.gstar.security.security.UserDetailsImpl;

@Controller
@RequestMapping("/register")
public class RegisterController {

	@Autowired AccountRepository accountRepository;
	@Autowired PasswordEncoder passwordEncoder;
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String registerForm(){
		return "registerForm";
	}
	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String register(Account account){
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		UserDetailsImpl userDetails = new UserDetailsImpl(account);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/";
	}
	
}
