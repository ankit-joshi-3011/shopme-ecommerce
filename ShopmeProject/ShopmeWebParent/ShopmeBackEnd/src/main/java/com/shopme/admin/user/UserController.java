package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);

		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);

		List<Role> listRoles = service.listRoles();
		model.addAttribute("listRoles", listRoles);

		return "user_form";
	}

	// On the controller request mapping method, we can specify RedirectAttributes
	// parameter and populate it with attributes for when the request gets
	// redirected. The redirect attributes will then be available on the target page
	// where it redirects to.
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes attributes) {
		service.save(user);

		attributes.addFlashAttribute("message", "The user has been saved successfully.");

		return "redirect:/users";
	}
}
