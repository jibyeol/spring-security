package com.gstar.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gstar.security.service.HelloMessageService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MessageServiceTest {
	
	@Autowired HelloMessageService service;
	@Autowired WebApplicationContext context;
	
	private MockMvc mvc;
	
	@Before
	public void setup(){
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void 인증되지않은사용자는_AuthenticationCredentialsNotFoundException를_발생시킨다(){
		service.getMessage();
	}
	
	@Test
	@WithMockUser
	public void getMessageWithMockUser(){
		String message = service.getMessage();
		System.out.println("받은 메세지 : " + message);
		assertNotNull(message);
	}
	
	@Test
	@WithMockUser(username="admin", authorities={"ADMIN", "USER"})
	public void getMessageWithMockUserCustomAuthorities(){
		String message = service.getMessage();
		System.out.println(message);
		assertNotNull(message);
	}
	
	@Test
	public void mvcAdminTest() throws Exception {
		ResultActions result = mvc.perform(
				get("/admin")
					.with(user("admin").password("pass").roles("USER", "ADMIN")));
		result.andDo(print());
		result.andExpect(status().isOk()); // 200
	}
	
	@Test
	public void mvcAdminTestByNormalUser() throws Exception {
		ResultActions result = mvc.perform(
				get("/admin")
					.with(user("user").password("pass").roles("USER")));
		result.andDo(print());
		result.andExpect(status().isForbidden()); // 403 error
		/**
		 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/result/StatusResultMatchers.html
		 */
	}
	
}
