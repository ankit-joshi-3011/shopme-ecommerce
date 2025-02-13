package com.shopme.common.dto;

import com.shopme.common.entity.State;

public class StateDTO {
	private Integer id;
	private String name;

	public StateDTO(State state) {
		this.id = state.getId();
		this.name = state.getName();
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
}
