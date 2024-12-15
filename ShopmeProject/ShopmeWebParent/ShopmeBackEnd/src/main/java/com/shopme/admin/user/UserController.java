package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtility;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		return listByPage(1, model);
	}

	@GetMapping("/users/page/{pageNumber}")
	public String listByPage(@PathVariable int pageNumber, Model model) {
		Page<User> page = service.listByPage(pageNumber);
		List<User> pageUsers = page.getContent();
		model.addAttribute("listUsers", pageUsers);

		model.addAttribute("totalItems", page.getTotalElements());

		long startCount = (pageNumber - 1) * UserService.USERS_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);

		long endCount = startCount + UserService.USERS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		model.addAttribute("endCount", endCount);

		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Create New User");

		List<Role> listRoles = service.listRoles();
		model.addAttribute("listRoles", listRoles);

		return "user_form";
	}

	// On the controller request mapping method, we can specify RedirectAttributes
	// parameter and populate it with attributes for when the request gets
	// redirected. The redirect attributes will then be available on the target page
	// where it redirects to.
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes attributes, @RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);

			String uploadDirectory = "user-photos/" + savedUser.getId();

			FileUploadUtility.cleanDirectory(uploadDirectory);
			FileUploadUtility.saveFile(uploadDirectory, fileName, multipartFile);
		} else {
			if (user.getPhotos() == null || user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}

			service.save(user);
		}

		attributes.addFlashAttribute("message", "The user has been saved successfully.");

		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id, RedirectAttributes attributes, Model model) {
		try {
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();

			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
			model.addAttribute("listRoles", listRoles);
			return "user_form";
		} catch (UserNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Integer id, RedirectAttributes attributes) {
		try {
			service.delete(id);
			attributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
		} catch (UserNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable Integer id, @PathVariable boolean status,
			RedirectAttributes attributes) {
		service.updateUserEnabledStatus(id, status);
		attributes.addFlashAttribute("message", "The user ID " + id + " has been " + (status ? "enabled" : "disabled"));
		return "redirect:/users";
	}
}
