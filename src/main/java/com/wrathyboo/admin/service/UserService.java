package com.wrathyboo.admin.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.sun.jersey.api.client.GenericType;
import com.wrathyboo.admin.entities.Token;
import com.wrathyboo.admin.entities.User;
import com.wrathyboo.admin.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final AuthenticationService authenticationService;
	
     public List<User> getAllUsers(){
    	 Gson son = new Gson();
 		WebClient client = WebClient.create();
 		WebClient.ResponseSpec responseSpec = client.get()
 		    .uri("http://localhost:8080/api/v1/users")
 		    .headers(h -> h.setBearerAuth(authenticationService.getToken()))
 		    .retrieve();
 		String responseBody = responseSpec.bodyToMono(String.class).block();
 		GenericType<List<User>> listBookType = new GenericType<List<User>>() {};
 		List<User> list = son.fromJson(responseBody, listBookType.getType());
 		return list;
     }
   
}
