package com.shopme.common.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;

	@Column(length = 64)
	private String photos;

	private boolean enabled;

	@ManyToMany(fetch = FetchType.EAGER)
	// The @JoinTable annotation in JPA is used to customize the association table
	// that holds the relationships between two entities in a many-to-many
	// relationship.
	// joinColumns: The foreign key columns of the join table which reference the
	// primary table
	// of the entity owning the association.
	// inverseJoinColumns: The foreign key columns of the join table which reference
	// the primary table
	// of the entity that does not own the association.
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
		super();
	}

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
			+ ", roles=" + roles + "]";
	}

	@Transient
	public String getPhotosImagePath() {
		if (id == null || photos == null) {
			return null;
		}

		return "/user-photos/" + id + "/" + photos;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = roles.iterator();

		while (iterator.hasNext()) {
			if (iterator.next().getName().equals(roleName)) {
				return true;
			}
		}

		return false;
	}
}
