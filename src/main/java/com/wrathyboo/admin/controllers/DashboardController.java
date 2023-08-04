package com.wrathyboo.admin.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wrathyboo.admin.entities.Category;
import com.wrathyboo.admin.entities.Product;
import com.wrathyboo.admin.entities.User;
import com.wrathyboo.admin.service.ProductService;
import com.wrathyboo.admin.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {
   
   private final ProductService productService;
	private final UserService userService;


	@GetMapping(value = {"/","admin"})
	public String dashboard(Model model) {
		model.addAttribute("activePage", "admin");
		
		return "index";
	}
    
	@GetMapping("accounts")
	public String accounts(Model model) {
		
		List<User> list = userService.getAllUsers();
		model.addAttribute("users",list);
	    model.addAttribute("activePage", "accounts");
	return "accounts";
	}
	
	@GetMapping("products")
	public String products(Model model) {
		List<Product> list = productService.getItems();
		List<Category> categories = productService.getCategories();
		model.addAttribute("cat", categories);
		model.addAttribute("items", list);
	 model.addAttribute("activePage", "products");
	return "products";
	}
	
	@GetMapping("history")
	public String history(Model model) {
	 model.addAttribute("activePage", "history");
	return "billing";
	}
	
	@GetMapping("profile")
	public String profile(Model model) {

		model.addAttribute("activePage", "profile");
	return "profile";
	}
	
}
