package com.agribank.schedule.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchDTO {
	public static final int MAX_20 = 20;
	public static final int MAX_200 = 200;
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	@Min(value = 0)
	private int page;

	@Min(value = 1)
	@Builder.Default
	private int size = MAX_20;

	@Builder.Default
	private String value = "%%";

	private List<OrderBy> orders;

	@Builder.Default
	private Map<String, String> filterBys = new HashMap<String, String>();

	@Data
	@ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderBy {
		@Builder.Default
		private String order = ASC;// asc, desc
		private String property;
	}
}
