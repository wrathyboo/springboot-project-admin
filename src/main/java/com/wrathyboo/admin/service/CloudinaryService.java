package com.wrathyboo.admin.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
	   private final Cloudinary cloudinaryConfig;


	    public String uploadFile(MultipartFile gif) {
	        try {
	            File uploadedFile = convertMultiPartToFile(gif);
	            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
	            boolean isDeleted = uploadedFile.delete();

	            if (isDeleted){
	                System.out.println("File successfully deleted");
	            }else
	                System.out.println("File doesn't exist");
	            return  uploadResult.get("url").toString();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }

	    private File convertMultiPartToFile(MultipartFile file) throws IOException {
	        File convFile = new File(file.getOriginalFilename());
	        FileOutputStream fos = new FileOutputStream(convFile);
	        fos.write(file.getBytes());
	        fos.close();
	        return convFile;
	    }

	  
	    
}
