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
		String userPhotosDirectoryName = "user-photos";

		Path userPhotosDirectory = Paths.get(userPhotosDirectoryName);
		String userPhotosPath = userPhotosDirectory.toFile().getAbsolutePath();

		registry.addResourceHandler("/" + userPhotosDirectoryName + "/**")
			.addResourceLocations("file:/" + userPhotosPath + "/");

		String categoryImagesDirectoryName = "category-images";

		Path categoryImagesDirectory = Paths.get("../" + categoryImagesDirectoryName);
		String categoryImagesPath = categoryImagesDirectory.toFile().getAbsolutePath();

		registry.addResourceHandler("/" + categoryImagesDirectoryName + "/**")
			.addResourceLocations("file:/" + categoryImagesPath + "/");
	}
}
