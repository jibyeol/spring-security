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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gstar.security.service.HelloMessageService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

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
	
}
