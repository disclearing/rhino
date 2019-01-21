package com.sergivb01.util;

import java.util.ArrayList;
import java.util.Random;

public class RandomUtils{
	private static final String[] alpha = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	private static final String[] numeric = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

	public static String randomAlphaNumeric(Integer maxChar){
		ArrayList<String> alphaText = new ArrayList<String>();
		for(String text : alpha){
			alphaText.add(text);
		}
		for(String nubmer : numeric){
			alphaText.add(nubmer);
		}
		String randomName = null;
		for(int i = 0; i < maxChar; ++i){
			Random random = new Random();
			Integer rand = random.nextInt(alphaText.size());
			if(randomName == null){
				randomName = alphaText.get(rand);
			}
			randomName = randomName + alphaText.get(rand);
		}
		return randomName;
	}

	public static Integer randomNumeric(Integer maxChar){
		ArrayList<String> alphaText = new ArrayList<String>();
		for(String nubmer : numeric){
			alphaText.add(nubmer);
		}
		String randomName = null;
		for(int i = 0; i < maxChar; ++i){
			Random random = new Random();
			Integer rand = random.nextInt(alphaText.size());
			if(randomName == null){
				randomName = alphaText.get(rand);
			}
			randomName = randomName + alphaText.get(rand);
		}
		return Integer.parseInt(randomName);
	}

	public static String randomString(Integer maxChar){
		ArrayList<String> alphaText = new ArrayList<String>();
		for(String text : alpha){
			alphaText.add(text);
		}
		String randomName = null;
		for(int i = 0; i < maxChar; ++i){
			Random random = new Random();
			Integer rand = random.nextInt(alphaText.size());
			if(randomName == null){
				randomName = alphaText.get(rand);
			}
			randomName = randomName + alphaText.get(rand);
		}
		return randomName;
	}
}

