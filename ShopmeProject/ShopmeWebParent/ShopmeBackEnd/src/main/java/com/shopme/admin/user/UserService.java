package com.shopme.admin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	public static final int USERS_PER_PAGE = 5;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	public List<User> listAll() {
		Iterable<User> users = userRepository.findAll(Sort.by("firstName").ascending());
		List<User> returnedUsers = new ArrayList<>();

		for (User user : users) {
			returnedUsers.add(user);
		}

		return returnedUsers;
	}

	public Page<User> listByPage(int pageNumber, String sortField, String sortDirection, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);

		if (keyword != null) {
			return userRepository.findAll(keyword, pageable);
		}

		return userRepository.findAll(pageable);
	}

	public List<Role> listRoles() {
		Iterable<Role> roles = roleRepository.findAll();
		List<Role> returnedRoles = new ArrayList<>();

		for (Role role : roles) {
			returnedRoles.add(role);
		}

		return returnedRoles;
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);

		if (isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();

			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}

		return userRepository.save(user);
	}

	public User updateLoggedInUserAccount(User loggedInUser) {
		User user = userRepository.findById(loggedInUser.getId()).get();

		user.setFirstName(loggedInUser.getFirstName());
		user.setLastName(loggedInUser.getLastName());

		if (!loggedInUser.getPassword().isEmpty()) {
			user.setPassword(loggedInUser.getPassword());
			encodePassword(user);
		}

		if (loggedInUser.getPhotos() != null) {
			user.setPhotos(loggedInUser.getPhotos());
		}

		return userRepository.save(user);
	}

	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User user = userRepository.getUserByEmail(email);

		if (user == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (user != null) {
				return false;
			}
		} else {
			if (user.getId() != id) {
				return false;
			}
		}

		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		userRepository.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepository.updateEnabledStatus(id, enabled);
	}
}
