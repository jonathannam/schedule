package com.agribank.schedule.entity;

import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Privilege {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String authority;

	private String api;

	@Enumerated(EnumType.STRING)
	private HttpMethod method;

	private boolean secured;

	private boolean authenticated;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_privilege", joinColumns = @JoinColumn(name = "privilege_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

}