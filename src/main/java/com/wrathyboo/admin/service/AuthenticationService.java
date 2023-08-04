package com.wrathyboo.admin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.wrathyboo.admin.entities.AuthenticateRequest;
import com.wrathyboo.admin.entities.Token;
import com.wrathyboo.admin.entities.User;
import com.wrathyboo.admin.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final TokenRepository tokenRepository;

	public String getToken() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String validTokens = "";
		Integer userId;
		if (principal instanceof User) {
			userId = ((User) principal).getId();
		} else {
			return null;
		}
		List<Token> userTokens = tokenRepository.findAllValidTokenByUser(userId);
		for (Token x : userTokens) {
			validTokens = x.getToken();
		}
		final String access_token = validTokens;
		return access_token;
	}
	
	public void refreshToken() {
		WebClient client = WebClient.create();
		val response = client.post()
			    .uri( "http://localhost:8080/api/v1/auth/refresh-token" )
			    .headers(h -> h.setBearerAuth(getToken()))
			    .accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
	}
	
	public void authorize() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof User) {
			username = ((User)principal).getUsername();
		} else {
			username = null;
		}
		AuthenticateRequest user = new AuthenticateRequest();
		user.setUsername(username);
		WebClient client = WebClient.create();
		val response = client.post()
			    .uri( "http://localhost:8080/api/v1/auth/authenticate" )
			    .accept(MediaType.APPLICATION_JSON)
			    .bodyValue(user)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		System.out.println(response);
	}
}