package com.agribank.schedule.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
	private Integer id;

	private String name;
	
	private List<PrivilegeDTO> privileges;

}
