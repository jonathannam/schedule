package com.agribank.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class UserDTO {
	private Integer id;
	private String name;
	private String username;
	private String photoURL;
	private String phone;
	private String address;
	private String note;
	private String email;
	private String gender;
	private String website;
	private Boolean enabled;
	private String password;
	private String oldPassword;
	
	@JsonIgnore
	private MultipartFile file;
	
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date birthdate;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;

	private RoleDTO role;
}
