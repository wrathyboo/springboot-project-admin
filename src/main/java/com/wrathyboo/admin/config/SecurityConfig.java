package com.wrathyboo.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;


import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
//	private final LogoutHandler logoutHandler;
	
	private static final String[] AUTH_WHITELIST = {
            "/signUp",
            "/signIn",
	        "/assets/**"
	};
	private static final String[] AUTH_USER = {
	        "/cart",
	        "/checkout",
	        "/account",
	        "/wishlist"
	};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> auth
		           .requestMatchers(AUTH_WHITELIST).permitAll()
                   .anyRequest().hasAuthority("ADMIN"))
				   .csrf(csrf -> csrf.disable())
			       .formLogin(form -> form
			    		   .usernameParameter("username")
		                   .passwordParameter("password")
			    		   .loginPage("/signIn")
			    		   .loginProcessingUrl("/process_login")
			    		   .failureUrl("/signIn?error")
			    		   .defaultSuccessUrl("/authenticate",true)
			    		   .permitAll())
//			       .sessionManagement((session) -> session
//			               .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//			       .authenticationProvider(authenticationProvider)
//			       .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			       .logout(logout -> logout 
			    		   .logoutUrl("/logout")
//			    		   .addLogoutHandler(logoutHandler)
//			    		   .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
			    		   .logoutSuccessUrl("/login")
			    		   .permitAll())
			       .build();
	}
}