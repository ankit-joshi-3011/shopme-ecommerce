package com.shopme.admin.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll() {
		Iterable<User> users = userRepository.findAll();
		List<User> returnedUsers = new ArrayList<>();

		for (User user : users) {
			returnedUsers.add(user);
		}

		return returnedUsers;
	}

	public List<Role> listRoles() {
		Iterable<Role> roles = roleRepository.findAll();
		List<Role> returnedRoles = new ArrayList<>();

		for (Role role : roles) {
			returnedRoles.add(role);
		}

		return returnedRoles;
	}

	public void save(User user) {
		encodePassword(user);
		userRepository.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
}
