package com.agribank.schedule.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class SimOperatorUtils {

	protected static final Function<String, SimOperatorEnum> CHECK_VIETTEL = (firstNumbers) -> {
		if (firstNumbers.equals("86") || firstNumbers.equals("96") || firstNumbers.equals("97")
				|| firstNumbers.equals("98") || firstNumbers.equals("32") || firstNumbers.equals("33")
				|| firstNumbers.equals("34") || firstNumbers.equals("35") || firstNumbers.equals("36")
				|| firstNumbers.equals("37") || firstNumbers.equals("38") || firstNumbers.equals("39"))
			return SimOperatorEnum.VIETTEL;
		return null;
	};

	protected static final Function<String, SimOperatorEnum> CHECK_VINA = (firstNumbers) -> {
		if (firstNumbers.equals("91") || firstNumbers.equals("94") || firstNumbers.equals("88")
				|| firstNumbers.equals("81") || firstNumbers.equals("82") || firstNumbers.equals("83")
				|| firstNumbers.equals("84") || firstNumbers.equals("85"))
			return SimOperatorEnum.VINAPHONE;
		return null;
	};

	protected static final Function<String, SimOperatorEnum> CHECK_MOBI = (firstNumbers) -> {
		if (firstNumbers.equals("89") || firstNumbers.equals("90") || firstNumbers.equals("93")
				|| firstNumbers.equals("70") || firstNumbers.equals("76") || firstNumbers.equals("77")
				|| firstNumbers.equals("78") || firstNumbers.equals("79"))
			return SimOperatorEnum.MOBIPHONE;
		return null;
	};
	
	protected static final Function<String, SimOperatorEnum> CHECK_VIETNAMMOBILE = (firstNumbers) -> {
		if (firstNumbers.equals("92") || firstNumbers.equals("56") || firstNumbers.equals("58")
				||  firstNumbers.equals("52"))
			return SimOperatorEnum.VIETNAMMOBILE;
		return null;
	};

	protected static final Function<String, SimOperatorEnum> CHECK_GMOBILE = (firstNumbers) -> {
		if (firstNumbers.equals("99") || firstNumbers.equals("59"))
			return SimOperatorEnum.GMOBILE;
		return null;
	};
	
	protected static final Function<String, SimOperatorEnum> CHECK_ITELECOM = (firstNumbers) -> {
		if (firstNumbers.equals("87"))
			return SimOperatorEnum.ITELECOM;
		return null;
	};
	
	private static Set<Function<String, SimOperatorEnum>> checkFunctions = new HashSet<>();
	static {
		Field[] fs = SimOperatorUtils.class.getDeclaredFields();
        for (Field field : fs) {
            field.setAccessible(true);
            if(field.getName() != "checkFunctions") {
	            try {
					checkFunctions.add((Function<String, SimOperatorEnum>) field.get(null) );
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
            }
        }
	}
	
	public static SimOperatorEnum getSimOperator(String number) {
		String opNumbers = number.substring(1, 3);
		List<SimOperatorEnum> operator = new ArrayList<>();
		checkFunctions.forEach((func) -> {
			final SimOperatorEnum op = func.apply(opNumbers);
			if(op != null) {
				operator.add(op);
			}
		});
		if (operator.size() > 0)
			return operator.get(0);
		return SimOperatorEnum.HOMEPHONE;
	}
}