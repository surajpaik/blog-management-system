package com.blogapp.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.jsoup.Jsoup;
import org.springframework.web.multipart.MultipartFile;

public class Helper {

	public static String getUrlFromTitle(String postTitle) {

		String title = postTitle.trim().toLowerCase();

		String url = title.replaceAll("\\s", "-");
		url = url.replaceAll("[^A-Za-z0-9]", "-");

		return url;
	}

	// to save file
	public static boolean saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {

		Path uploadPath = Paths.get(uploadDir);

		try (InputStream inputstream = multipartFile.getInputStream()) {

			if (!Files.exists(uploadPath)) {

				Files.createDirectories(uploadPath);
			}

			// represent the full path with root directory/sub directory/imagename
			Path fullPath = uploadPath.resolve(fileName);

			Files.copy(inputstream, fullPath, StandardCopyOption.REPLACE_EXISTING);
			return true;

		} catch (IOException ex) {

			ex.printStackTrace();
			return false;
		}

	}
	
	public static String htmlSanitize(String shortDesc) {
		
		return Jsoup.parse(shortDesc).text();
	}
	
	public static int calculateReadingTime(String content) {

	    if(content == null || content.isBlank()) {
	        return 1;
	    }

	    String text = htmlSanitize(content);

	    int words = text.trim().split("\\s+").length;

	    int minutes = (int) Math.ceil(words / 200.0);

	    return Math.max(minutes, 1);
	}

}
