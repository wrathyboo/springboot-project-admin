package com.wrathyboo.admin.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.cloudinary.*;
import com.wrathyboo.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class ApplicationConfig {
    
	private final UserRepository userRepository;
	
	  @Value("${cloudinary.cloud_name}")
	    private String cloudName;

	    @Value("${cloudinary.api_key}")
	    private String apiKey;

	    @Value("${cloudinary.api_secret}")
	    private String apiSecret;

	
	
    @Bean
    public WebClient CustomWebClient() {
        WebClient client = WebClient.builder()
        		    .baseUrl("http://localhost:8080")
        	        .defaultCookie("cookie-name", "cookie-value")
        	        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        	        .build();
        return client;
    }
	
	@Bean
	public UserDetailsService userDetailsService() {
            return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
    

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
	
	
}
