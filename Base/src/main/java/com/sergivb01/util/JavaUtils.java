package com.sergivb01.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class JavaUtils{
	private static final CharMatcher CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z')).or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
	private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
	private static final int DEFAULT_NUMBER_FORMAT_DECIMAL_PLACES = 5;

	public static boolean isUUID(String string){
		return UUID_PATTERN.matcher(string).find();
	}

	public static boolean isAlphanumeric(String string){
		return CHAR_MATCHER_ASCII.matchesAllOf(string);
	}

	public static boolean containsIgnoreCase(Iterable<? extends String> elements, String string){
		for(String element : elements){
			if(!StringUtils.containsIgnoreCase(element, string)) continue;
			return true;
		}
		return false;
	}

	public static String format(Number number){
		return JavaUtils.format(number, 5);
	}

	public static String format(Number number, int decimalPlaces){
		return JavaUtils.format(number, decimalPlaces, RoundingMode.HALF_DOWN);
	}

	public static String format(Number number, int decimalPlaces, RoundingMode roundingMode){
		Preconditions.checkNotNull((Object) number, "The number cannot be null");
		return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString();
	}

	public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd){
		return JavaUtils.andJoin(collection, delimiterBeforeAnd, ", ");
	}

	public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter){
		if(collection == null || collection.isEmpty()){
			return "";
		}
		ArrayList<String> contents = new ArrayList<String>(collection);
		String last = contents.remove(contents.size() - 1);
		StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
		if(delimiterBeforeAnd){
			builder.append(delimiter);
		}
		return builder.append(" and ").append(last).toString();
	}

	public static long parse(String input){
		if(input == null || input.isEmpty()){
			return -1;
		}
		long result = 0;
		StringBuilder number = new StringBuilder();
		for(int i = 0; i < input.length(); ++i){
			String str;
			char c = input.charAt(i);
			if(Character.isDigit(c)){
				number.append(c);
				continue;
			}
			if(!Character.isLetter(c) || (str = number.toString()).isEmpty()) continue;
			result += JavaUtils.convert(Integer.parseInt(str), c);
			number = new StringBuilder();
		}
		return result;
	}

	private static long convert(int value, char unit){
		switch(unit){
			case 'y':{
				return (long) value * TimeUnit.DAYS.toMillis(365);
			}
			case 'M':{
				return (long) value * TimeUnit.DAYS.toMillis(30);
			}
			case 'd':{
				return (long) value * TimeUnit.DAYS.toMillis(1);
			}
			case 'h':{
				return (long) value * TimeUnit.HOURS.toMillis(1);
			}
			case 'm':{
				return (long) value * TimeUnit.MINUTES.toMillis(1);
			}
			case 's':{
				return (long) value * TimeUnit.SECONDS.toMillis(1);
			}
		}
		return -1;
	}
}

