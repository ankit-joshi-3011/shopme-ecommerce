package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String[] directoryNames = new String[] { "user-photos", "category-images", "brand-logo-images", "product-images", "site-logo" };
		boolean[] parentDirectoryOrCurrentDirectory = new boolean[] { false, true, true, true, true };

		for (int i = 0; i < directoryNames.length; i++) {
			Path directoryPath = Paths.get((parentDirectoryOrCurrentDirectory[i] ? "../" : "") + directoryNames[i]);

			String filePath = directoryPath.toFile().getAbsolutePath();

			registry.addResourceHandler("/" + directoryNames[i] + "/**")
				.addResourceLocations("file:/" + filePath + "/");
		}
	}
}
