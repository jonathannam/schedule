package com.agribank.schedule.utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class SimCategoryUtils {
	/**
	 * <ul>
	 * <li>Sim Tam hoa: 0x...AAA | 0x...AAA...</li>
	 * <li>Sim Tam hoa kep: 0x...AAABBB</li>
	 * <li>Sim Tu quy: 0x...AAAA</li>
	 * <li>Sim Ngu quy: 0x...AAAAA</li>
	 * <li>Sim Luc quy: 0x...AAAAAA</li>
	 * <li>Sim Lap: 0x...AB AB</li>
	 * <li>Sim Kep: 0x...AA BB</li>
	 * <li>Sim Taxi 2: 0x...AB AB AB [AB]</li>
	 * <li>Sim Taxi 3: 0x...ABC ABC</li>
	 * <li>Sim Taxi 4: 0x ABCD ABCD</li>
	 * <li>Sim Lap ganh: 0x AB[C]AB | 0x ABC[D]ABC</li>
	 * <li>Sim Ganh dao: 0x AB[C]BA | 0x ABC[D]CBA</li>
	 * <li>Sim Loc phat: 0x...68 | 0x...86 | 0x...66 | 0x...88</li>
	 * <li>Sim Than tai: 0x...39 | 0x...79</li>
	 * <li>Sim Ong dia: 0x...38 | 0x...78</li>
	 * <li>Sim Tien don: 0x...123 | 0x... 258 | ...</li>
	 * <li>Sim Tien doi: 0x...121416 | 0x... 586878 | ...</li>
	 * <li>Sim Nam sinh: 0x...1900-2050</li>
	 * <li>Sim Ngay sinh: 0x...ddMM1900-2050 | 0x...ddMMyy</li>
	 * </ul>
	 * 
	 * @author Nhat
	 *
	 */
	protected static final Function<int[], SimCategoryEnum> CHECK_TAM_HOA = (numbers) -> {
		if (numbers[0] == numbers[1] && numbers[1] == numbers[2]
				|| numbers[3] == numbers[4] && numbers[4] == numbers[5])
			return SimCategoryEnum.TAM_HOA;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TAM_HOA_KEP = (numbers) -> {
		if (numbers[0] == numbers[1] && numbers[1] == numbers[2] && numbers[3] == numbers[4]
				&& numbers[4] == numbers[5])
			return SimCategoryEnum.TAM_HOA_KEP;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TU_QUY = (numbers) -> {
		if (numbers[0] == numbers[1] && numbers[1] == numbers[2] && numbers[2] == numbers[3])
			return SimCategoryEnum.TU_QUY;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_NGU_QUY = (numbers) -> {
		if (CHECK_TU_QUY.apply(numbers) != null && numbers[3] == numbers[4])
			return SimCategoryEnum.NGU_QUY;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_LUC_QUY = (numbers) -> {
		if (CHECK_NGU_QUY.apply(numbers) != null && numbers[4] == numbers[5])
			return SimCategoryEnum.LUC_QUY;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_LAP = (numbers) -> {
		if (numbers[0] == numbers[2] && numbers[1] == numbers[3])
			return SimCategoryEnum.LAP;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_KEP = (numbers) -> {
		if (numbers[0] == numbers[1] && numbers[2] == numbers[3])
			return SimCategoryEnum.KEP;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TAXI_2 = (numbers) -> {
		if (concatNums(numbers[1], numbers[0]) == concatNums(numbers[3], numbers[2])
				&& concatNums(numbers[3], numbers[2]) == concatNums(numbers[5], numbers[4])
				&& concatNums(numbers[5], numbers[4]) == concatNums(numbers[7], numbers[6]))
			return SimCategoryEnum.TAXI_2;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TAXI_3 = (numbers) -> {
		if (concatNums(numbers[2], numbers[1], numbers[0]) == concatNums(numbers[5], numbers[4], numbers[3]))
			return SimCategoryEnum.TAXI_3;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TAXI_4 = (numbers) -> {
		if (concatNums(numbers[3], numbers[2], numbers[1], numbers[0]) == concatNums(numbers[7], numbers[6], numbers[5],
				numbers[4]))
			return SimCategoryEnum.TAXI_4;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_LAP_GANH = (numbers) -> {
		for (int i = 0; i < 3; i++) {
			int endToken = concatNumsInRange(numbers, i, 0);
			int toCompare1 = concatNumsInRange(numbers, 2 * i + 1, i + 1);
			int toCompare2 = concatNumsInRange(numbers, 2 * i + 2, i + 2);
			if (((endToken == toCompare1) && i > 1) || endToken == toCompare2)
				return SimCategoryEnum.LAP_GANH;
		}
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_GANH_DAO = (numbers) -> {
		for (int i = 0; i < 3; i++) {
			int endToken = concatNumsInRange(numbers, i, 0);
			int toCompare1 = concatNumsInRange(numbers, i + 1, 2 * i + 1);
			int toCompare2 = concatNumsInRange(numbers, i + 2, 2 * i + 2);
			if (((endToken == toCompare1) && i > 1) || endToken == toCompare2)
				return SimCategoryEnum.GANH_DAO;
		}
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_LOC_PHAT = (numbers) -> {
		int endToken = concatNums(numbers[1], numbers[0]);
		if (endToken == 68 || endToken == 86 || endToken == 66 || endToken == 88)
			return SimCategoryEnum.LOC_PHAT;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_THAN_TAI = (numbers) -> {
		int endToken = concatNums(numbers[1], numbers[0]);
		if (endToken == 39 || endToken == 79)
			return SimCategoryEnum.THAN_TAI;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_ONG_DIA = (numbers) -> {
		int endToken = concatNums(numbers[1], numbers[0]);
		if (endToken == 38 || endToken == 78)
			return SimCategoryEnum.ONG_DIA;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TIEN_DON = (numbers) -> {
		if (numbers[0] - numbers[1] > 0 && numbers[0] - numbers[1] == numbers[1] - numbers[2])
			return SimCategoryEnum.TIEN_DON;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_TIEN_DOI = (numbers) -> {
		if (numbers[0] == numbers[2] && numbers[2] == numbers[4]) {
			if (numbers[1] - numbers[3] > 0 && numbers[1] - numbers[3] == numbers[3] - numbers[5])
				return SimCategoryEnum.TIEN_DOI;
		}
		if (numbers[1] == numbers[3] && numbers[3] == numbers[5]) {
			if (numbers[0] - numbers[2] > 0 && numbers[0] - numbers[2] == numbers[2] - numbers[4])
				return SimCategoryEnum.TIEN_DOI;
		}
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_NAM_SINH = (numbers) -> {
		int year = concatNumsInRange(numbers, 3, 0);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if (year >= currentYear - 100 && year <= currentYear + 10)
			return SimCategoryEnum.NAM_SINH;
		return null;
	};
	protected static final Function<int[], SimCategoryEnum> CHECK_NGAY_SINH = (numbers) -> {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		dateFormat.setLenient(false);

		StringBuilder date = new StringBuilder();
		if (CHECK_NAM_SINH.apply(numbers) != null) {
			try {
				date.append(Integer.toString(concatNums(numbers[7], numbers[6])));
				date.append(Integer.toString(concatNums(numbers[5], numbers[4])));
				date.append(Integer.toString(concatNumsInRange(numbers, 3, 0)));
				dateFormat.parse(date.toString());
				return SimCategoryEnum.NGAY_SINH;
			} catch (ParseException e) {
			}
		} else {
			try {
				dateFormat.applyPattern("ddMMyy");
				date.append(Integer.toString(concatNums(numbers[5], numbers[4])));
				date.append(Integer.toString(concatNums(numbers[3], numbers[2])));
				date.append(Integer.toString(concatNums(numbers[1], numbers[0])));
				dateFormat.parse(date.toString());
				return SimCategoryEnum.NGAY_SINH;
			} catch (ParseException e1) {
			}
		}
		return null;
	};

	private static int concatNums(int... cs) {
		StringBuilder sb = new StringBuilder();
		for (int c : cs) {
			sb.append(Integer.toString(c));
		}
		return Integer.parseInt(sb.toString());
	}

	private static int concatNumsInRange(int[] numbers, int start, int end) {
		StringBuilder sb = new StringBuilder();
		int direction = (int) Math.signum(end - start) != 0 ? (int) Math.signum(end - start) : 1;
		for (int i = start; i != end + direction; i += direction) {
			sb.append(Integer.toString(numbers[i]));
		}
		return Integer.parseInt(sb.toString());
	}

	private static int[] getInverseNumberArray(String number) {
		int[] numbers = new int[number.length()];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = Character.getNumericValue(number.charAt(numbers.length - i - 1));
		}
		return numbers;
	}

	private static Set<Function<int[], SimCategoryEnum>> checkFunctions = new HashSet<>();
	static {
		Field[] fs = SimCategoryUtils.class.getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			if (field.getName() != "checkFunctions") {
				try {
					checkFunctions.add((Function<int[], SimCategoryEnum>) field.get(null));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<SimCategoryEnum> getSimCategories(String number) {
		int[] inverseNumbers = getInverseNumberArray(number);
		List<SimCategoryEnum> categories = new ArrayList<>();
		Function<SimCategoryEnum, Void> addIfNotNull = (SimCategoryEnum simCate) -> {
			if (simCate != null)
				categories.add(simCate);
			return null;
		};
		checkFunctions.forEach((func) -> {
			addIfNotNull.apply(func.apply(inverseNumbers));
		});
		if (categories.isEmpty()) {
			categories.add(SimCategoryEnum.OTHER);
		}
		return categories;
	}
}
