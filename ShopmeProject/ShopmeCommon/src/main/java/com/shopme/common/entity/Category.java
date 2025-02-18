package com.shopme.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String name;

	@Column(length = 64, nullable = false, unique = true)
	private String alias;

	@Column(length = 128, nullable = false)
	private String image;

	private boolean enabled;

	@Column(name = "all_parent_ids", nullable = true)
	private String allParentIds;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OrderBy("name asc")
	private Set<Category> children = new HashSet<>();

	public static Category createCopy(Category category) {
		Category categoryCopy = new Category();

		categoryCopy.setId(category.getId());
		categoryCopy.setName(category.getName());
		categoryCopy.setAlias(category.getAlias());
		categoryCopy.setImage(category.getImage());
		categoryCopy.setEnabled(category.isEnabled());
		categoryCopy.setParent(category.getParent());
		categoryCopy.setChildren(category.getChildren());

		return categoryCopy;
	}

	public Category() {
	}

	public Category(int id) {
		this.id = id;
	}

	public Category(String name, String alias, String image) {
		this.name = name;
		this.alias = alias;
		this.image = image;
	}

	public Category(String name, String alias, String image, Category parent) {
		this(name, alias, image);
		this.parent = parent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAllParentIds() {
		return allParentIds;
	}

	public void setAllParentIds(String allParentIds) {
		this.allParentIds = allParentIds;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Set<Category> getChildren() {
		return children;
	}

	public void setChildren(Set<Category> children) {
		this.children = children;
	}

	@Transient
	public String getImagePath() {
		if (id == null || image == null) {
			return "/images/MissingImage.png";
		}

		return "/category-images/" + id + "/" + image;
	}

	@Transient
	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	@Override
	public String toString() {
		return name;
	}
}
