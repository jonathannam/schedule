package com.agribank.schedule.utils;

public enum SimOperatorEnum {
	VIETTEL(1, "Viettel"), //
	VINAPHONE(2, "Vinaphone"), //
	MOBIPHONE(3, "Mobifone"), //
	VIETNAMMOBILE(4, "Vietnammobile"), //
	GMOBILE(5, "GMobile"),
	ITELECOM(6, "ITel"),
	HOMEPHONE(7, "Số cố định");

	private Integer id;
	private String name;

	private SimOperatorEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
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
