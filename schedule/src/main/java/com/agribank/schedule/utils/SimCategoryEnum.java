package com.agribank.schedule.utils;

public enum SimCategoryEnum {
	TAM_HOA(1, "Tam Hoa"), //
	TAM_HOA_KEP(2, "Tam Hoa Kép"), //
	TU_QUY(3, "Tứ Quý"), //
	NGU_QUY(4, "Ngũ Quý"), //
	LUC_QUY(5, "Lục Quý"), //
	LAP(6, "Lặp"), //
	KEP(7, "Kép"), //
	TAXI_2(8, "Taxi 2"), //
	TAXI_3(9, "Taxi 3"), //
	TAXI_4(10, "Taxi 4"), //
	LAP_GANH(11, "Lặp Gánh"), //
	GANH_DAO(12, "Gánh Đảo"), //
	LOC_PHAT(13, "Lộc Phát"), //
	THAN_TAI(14, "Thần Tài"), //
	ONG_DIA(15, "Ông Địa"), //
	TIEN_DON(16, "Tiến Đơn"), //
	TIEN_DOI(17, "Tiến Đôi"), //
	NAM_SINH(18, "Năm Sinh"), //
	NGAY_SINH(19, "Ngày Sinh"), OTHER(20, "Sim dễ nhớ");

	private Integer id;
	private String name;

	private SimCategoryEnum(Integer id, String name) {
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
