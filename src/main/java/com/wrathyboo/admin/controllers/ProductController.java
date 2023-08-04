package com.wrathyboo.admin.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.wrathyboo.admin.entities.Category;
import com.wrathyboo.admin.entities.Product;
import com.wrathyboo.admin.entities.Token;
import com.wrathyboo.admin.entities.Type;
import com.wrathyboo.admin.entities.User;
import com.wrathyboo.admin.repository.TokenRepository;
import com.wrathyboo.admin.service.AwsService;
import com.wrathyboo.admin.service.CloudinaryService;
import com.wrathyboo.admin.service.ProductService;
import com.wrathyboo.admin.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Controller
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
	
	private final UserService userService;
	private final ProductService productService;
	private final CloudinaryService cloudinaryService;

	@GetMapping("product_create")
	public String product_create(Model model) {
	 List<Category> list = productService.getCategories();
     model.addAttribute("s", new Product());
     model.addAttribute("list",list);
	 model.addAttribute("activePage", "products");
	return "create_product";
	}
	
	@GetMapping("category_create")
	public String category_create(Model model) {
     model.addAttribute("s", new Category());
	 model.addAttribute("activePage", "products");
	return "create_category";
	}
	
	@GetMapping("{id}")
	public String editItem(@PathVariable("id") Integer id, Model model) {
	 Product item = productService.getItem(id);
	 List<Category> list = productService.getCategories();
     model.addAttribute("list",list);
	 model.addAttribute("old", item);
     model.addAttribute("s", new Product());
	 model.addAttribute("activePage", "products");
	return "update_product";
	}
	
	@PostMapping("process_create")
	public String process_create(@ModelAttribute("s") Category request, Model model) {
		
		productService.createCategory(request);
		
	 model.addAttribute("activePage", "products");
	return "redirect:../products";
	}
	
	@PostMapping("create_item")
	public String process_ProductCreate(@ModelAttribute("s") Product request,@RequestParam("file") MultipartFile image, Model model) throws IOException, InterruptedException {
//		request.setImage(productService.storeLocalImage(image));
		request.setImage(cloudinaryService.uploadFile(image));
		if (request.getStatus() == null) {
			request.setStatus(false);
		}
		if (request.getType() == null) {
			request.setType(Type.UNISEX);
		}
		if (request.getPrice() == null) {
			request.setPrice(0);
		}
		if (request.getReviews() == null) {
			request.setReviews(0);
		}
		request.setPrice(request.getPrice()*100);
        val test = productService.createItem(request);	
	 model.addAttribute("activePage", "products");
	return "redirect:../products";
	}
	
	@PutMapping("{id}")
	public String process_ProductUpdate(@ModelAttribute("s") Product request,@PathVariable("id") Integer id,@RequestParam("file") MultipartFile image, Model model) throws IOException, InterruptedException {
		if (!image.isEmpty()) {
//			request.setImage(productService.storeLocalImage(image));
			request.setImage(cloudinaryService.uploadFile(image));
		}
		
	
		System.out.print(request);
         productService.updateItem(id,request);	
	 model.addAttribute("activePage", "products");
	return "redirect:../products";
	}
	
	@DeleteMapping("{id}")
	public String process_ProductDestroy(@PathVariable("id") Integer id, Model model) {
//		request.setImage(productService.storeLocalImage(image));
		productService.deleteItem(id);
//        val test = productService.createItem(request);	
	 model.addAttribute("activePage", "products");
	return "redirect:../products";
	}
}
