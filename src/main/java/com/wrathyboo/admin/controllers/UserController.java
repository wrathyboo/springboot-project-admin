package com.wrathyboo.admin.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.wrathyboo.admin.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
  
	@GetMapping("init_create")
	public String initCreate(Model model) {
     model.addAttribute("user", new User());
	 model.addAttribute("activePage", "accounts");
	return "create_user";
	}
	
	@PostMapping("create")
	public String create(@ModelAttribute("user") User request, Model model) {
		
		WebClient client = WebClient.create();
		val response = client.post()
			    .uri( "http://localhost:8080/api/v1/auth/register" )
			    .accept(MediaType.APPLICATION_JSON)
			    .bodyValue(request)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		
	 model.addAttribute("activePage", "accounts");
	return "redirect:../accounts";
	}
	
	
}
