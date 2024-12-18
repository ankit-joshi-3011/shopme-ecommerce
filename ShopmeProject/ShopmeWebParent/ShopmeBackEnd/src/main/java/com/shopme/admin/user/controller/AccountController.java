package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	@Autowired
	private UserService service;

	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedInUser, Model model) {
		String email = loggedInUser.getUsername();
		User user = service.getByEmail(email);
		model.addAttribute("user", user);

		return "account_form";
	}

	@PostMapping("/account/update")
	public String updateUser(User user, RedirectAttributes attributes,
			@RequestParam("image") MultipartFile multipartFile, @AuthenticationPrincipal ShopmeUserDetails principal)
			throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.updateLoggedInUserAccount(user);

			String uploadDirectory = "user-photos/" + savedUser.getId();

			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, fileName, multipartFile);
		} else {
			if (user.getPhotos() == null || user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}

			service.updateLoggedInUserAccount(user);
		}

		principal.setFirstName(user.getFirstName());
		principal.setLastName(user.getLastName());

		attributes.addFlashAttribute("message", "Your account details have been updated successfully");

		return "redirect:/account";
	}

}
