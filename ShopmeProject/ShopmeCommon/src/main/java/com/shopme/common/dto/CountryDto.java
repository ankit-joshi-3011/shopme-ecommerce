package com.shopme.common.dto;

import com.shopme.common.entity.Country;

public class CountryDto {
	private Integer id;
	private String name;
	private String code;

	public CountryDto(Country country) {
		this.id = country.getId();
		this.name = country.getName();
		this.code = country.getCode();
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
