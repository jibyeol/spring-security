package com.gstar.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gstar.security.model.Account;
import com.gstar.security.repository.AccountRepository;

@Controller
public class TestController {

	@Autowired AccountRepository accountRepository;
	
	@RequestMapping("/test")
	public String test(){
		return "home";
	}
	
	@RequestMapping("/user")
	public String userList(){
		
		return "user";
	}
	
	@RequestMapping("/me")
	public String me(){
		Account a = accountRepository.findMe();
		return "redirect:/";
	}
	
}
