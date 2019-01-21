package com.sergivb01.hcf.utils;

public class StringUtils{

	public static String join(String[] args){
		return join(args, 0);
	}

	public static String join(String[] args, int start){
		StringBuilder ret = new StringBuilder();
		for(int i = start; i < args.length; i++){
			ret.append(args[i])
					.append(" ");
		}
		return ret.toString();
	}

}
