package com.agribank.schedule.entity;

import com.agribank.schedule.utils.StatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "schedule_user")
@EqualsAndHashCode(callSuper = false)
public class ScheduleUser extends UpdateAuditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String title;

	private String description;

	@Enumerated(EnumType.STRING)
	private StatusEnum status;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "schedule _categories", joinColumns = @JoinColumn(name = "sim_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private List<Category> categories;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id")
	private User operator;

}