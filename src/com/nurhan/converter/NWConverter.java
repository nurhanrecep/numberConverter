package com.nurhan.converter;

import com.nurhan.converter.util.ConverterUtil;

/**
 * Number to word converter.
 * 
 * @author Nurhan R. Mustafa
 */
public class NWConverter {
	
	/** supported languages */
	public enum Language { BULGARIAN } 
	
	/**
	 * 
	 * @param number
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	public static String convertNumber(String number, Language lang) throws Exception {
		ConverterUtil.validateNumber(number, false);
		String convertedNumber = null;
		
		if(lang.equals(Language.BULGARIAN)) {
			convertedNumber = new BulgarianConverter().convert(number, false);
		}
		
		return convertedNumber;
	}
	
	/**
	 * 
	 * @param number
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	public static String convertCurrency(String number, Language lang) throws Exception {
		ConverterUtil.validateNumber(number, true);
		
		String convertedNumber = null;
		
		if(lang.equals(Language.BULGARIAN)) {
			convertedNumber = new BulgarianConverter().convert(number, true);
		}
		
		return convertedNumber;
	}

	
}
