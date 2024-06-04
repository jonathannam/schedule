package com.agribank.schedule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@Data
public class PrivilegeDTO {
    private Long id;

    private String authority;

    private String api;

    private String method;

    private boolean secured;
    
	private boolean authenticated;

    @JsonIgnoreProperties("privileges")
    private List<RoleDTO> roles;
}
