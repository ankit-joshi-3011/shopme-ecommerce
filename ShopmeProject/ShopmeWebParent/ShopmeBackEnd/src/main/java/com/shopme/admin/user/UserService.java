package com.shopme.admin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

	public boolean isEmailUnique(String email) {
		User user = userRepository.getUserByEmail(email);

		return user == null;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
}
