package com.wrathyboo.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.EntityResponse;

import com.google.gson.Gson;
import com.sun.jersey.api.client.GenericType;
import com.wrathyboo.admin.entities.Category;
import com.wrathyboo.admin.entities.Product;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final AuthenticationService authenticationService;
	private final Path storageFolder = Paths.get("C:\\Images");

	public List<Category> getCategories() {
		Gson son = new Gson();
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/api/v1/category")
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<Category>> listType = new GenericType<List<Category>>() {
		};
		List<Category> list = son.fromJson(responseBody, listType.getType());
		return list;
	}

	public void createCategory(Category request) {
		WebClient client = WebClient.create();
		val response = client.post().uri("http://localhost:8080/api/v1/category/create")
				.headers(h -> h.setBearerAuth(authenticationService.getToken())).accept(MediaType.APPLICATION_JSON)
				.bodyValue(request).retrieve().bodyToMono(Void.class).block();

	}

	public List<Product> getItems() {
		Gson son = new Gson();
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/api/v1/product/manager")
				.headers(h -> h.setBearerAuth(authenticationService.getToken())).accept(MediaType.APPLICATION_JSON)
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<Product>> listType = new GenericType<List<Product>>() {
		};
		List<Product> list = son.fromJson(responseBody, listType.getType());
		return list;
	}
	
	public Product getItem(Integer id) {
		Gson son = new Gson();
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/api/v1/product/{id}",id)
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<Product> listType = new GenericType<Product>() {
		};
		Product item = son.fromJson(responseBody, listType.getType());

		return item;
	}

	public Object createItem(Product request) {
		WebClient client = WebClient.create();
		val response = client.post().uri("http://localhost:8080/api/v1/product/create")
				.headers(h -> h.setBearerAuth(authenticationService.getToken())).accept(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		return response;
	}
	
	public void deleteItem(Integer id) {
		WebClient client = WebClient.create();
		val response = client.delete().uri("http://localhost:8080/api/v1/product/delete/{id}",id)
				.headers(h -> h.setBearerAuth(authenticationService.getToken())).accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
				
	}
	
	public void updateItem(Integer id, Product request) {
		WebClient client = WebClient.create();
		val response = client.put().uri("http://localhost:8080/api/v1/product/update/{id}",id)
				.headers(h -> h.setBearerAuth(authenticationService.getToken())).accept(MediaType.APPLICATION_JSON)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
				
	}

	public String storeLocalImage(MultipartFile image) throws IOException {

		if (image.isEmpty()) {
			return "unavailable";
		}
		else {
			String filenameExtension = FilenameUtils.getExtension(image.getOriginalFilename());
			String generatedFilename = UUID.randomUUID().toString().replace("-", "");
			generatedFilename = generatedFilename + "." + filenameExtension;
			Path destinationFilename = this.storageFolder.resolve(Paths.get(generatedFilename)).normalize()
					.toAbsolutePath();
			// Save file
			InputStream inputStream = image.getInputStream();
			Files.copy(inputStream, destinationFilename, StandardCopyOption.REPLACE_EXISTING);
			return generatedFilename;
		}
	}
	
	public void uploadFile(MultipartFile request) {
		WebClient client = WebClient.create();
		val response = client.post().uri("http://localhost:8080/api/v1/uploads")
				.headers(h -> h.setBearerAuth(authenticationService.getToken())).accept(MediaType.MULTIPART_FORM_DATA)
				.bodyValue(request)
				.retrieve()
				.bodyToMono(Void.class)
				.block();

	}
}
