package com.wrathyboo.admin.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.wrathyboo.admin.entities.AuthenticateRequest;
import com.wrathyboo.admin.entities.User;
import com.wrathyboo.admin.service.AuthenticationService;
import com.wrathyboo.admin.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Controller()
@RequiredArgsConstructor
public class AuthenController {
    
	private final AuthenticationService authenticationService;
	
	@GetMapping("signUp")
	public String signup() {
		
		return "sign_up";
	}

	@GetMapping("signIn")
	public String login(Model model) {
		model.addAttribute("user", new AuthenticateRequest());
		return "sign_in";
	}
	
	@GetMapping("authenticate")
	public String authenticate( Model model) {
		authenticationService.authorize();
	 model.addAttribute("activePage", "admin");
	return "redirect:admin";
	}
	
	@GetMapping("refresh_token")
	public String refresh_token( Model model) {
		authenticationService.refreshToken();
	 model.addAttribute("activePage", "admin");
	return "redirect:admin";
	}
}
