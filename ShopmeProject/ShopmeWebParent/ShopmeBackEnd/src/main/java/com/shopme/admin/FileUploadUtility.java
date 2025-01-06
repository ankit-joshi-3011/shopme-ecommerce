package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtility {
//	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtility.class);

	public static void saveFile(String uploadDirectory, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDirectory);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}

	public static void cleanDirectory(String directory) {
		Path directoryPath = Paths.get(directory);

		try {
			Files.list(directoryPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
//						LOGGER.error("Error in deleting file: " + file);
						System.err.println("Error in deleting file: " + file);
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
//			LOGGER.error("Error in listing directory: " + directory);
			System.err.println("Error in listing directory: " + directory);
			e.printStackTrace();
		}
	}

	public static void removeDirectory(String directory) {
		cleanDirectory(directory);

		try {
			Files.delete(Paths.get(directory));
		} catch (IOException e) {
			System.err.println("Error in deleting directory: " + directory);
			e.printStackTrace();
		}
	}

	public static void deleteUnneededFiles(String directory, Set<String> namesOfFilesToKeep) {
		Path directoryPath = Paths.get(directory);

		try {
			Files.list(directoryPath).forEach(file -> {
				String fileName = file.toFile().getName();

				if (!namesOfFilesToKeep.contains(fileName)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						System.err.println("Error in deleting file: " + file);
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			System.err.println("Error in listing directory: " + directory);
			e.printStackTrace();
		}
	}
}
