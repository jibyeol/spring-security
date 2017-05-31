package com.gstar.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping(value={"", "/home"})
	public String home(){
		return "home";
	}
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/main")
	public void main(){}
	
	@RequestMapping("/403")
	public String error403(){
		return "403";
	}
	
	@RequestMapping("/admin")
	public void admin(){}
	
	@RequestMapping("/register/form")
	public String registerForm(){
		return "registerForm";
	}
	
}
