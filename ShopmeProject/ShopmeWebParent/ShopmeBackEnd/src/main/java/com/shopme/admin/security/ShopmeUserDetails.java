package com.shopme.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

public class ShopmeUserDetails implements UserDetails {
	private static final long serialVersionUID = -7662800453748795185L;

	private User user;

	public ShopmeUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public String getFullName() {
		return user.getFirstName() + " " + user.getLastName();
	}

	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
}
