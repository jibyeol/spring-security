package com.gstar.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gstar.security.model.Account;

@Controller
public class MessageController {

	@RequestMapping("/getPrivateMessage")
	@PreAuthorize("(#account.userId == principal.Username) or hasRole('ADMIN')")
	public String getPrivateMessage(Account account, Model model){
		model.addAttribute("msg", "당신은 관리자이거나 요청 파라미터와 아이디가 같습니다.");
		return "authorizeMessage";
	}
	
	@RequestMapping("/getUserMessage")
	@PreAuthorize("hasRole('USER')")
	public String getUserMessage(Model model){
		model.addAttribute("msg", "당신은 유저입니다.");
		return "authorizeMessage";
	}
	
}
