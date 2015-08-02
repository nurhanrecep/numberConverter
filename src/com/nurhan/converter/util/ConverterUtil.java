package com.nurhan.converter.util;

/**
 * A util class used by the converters.
 * 
 * @author Nurhan R. Mustafa
 * @version 1.0
 */
public class ConverterUtil {
	
	/**
	 * Validates the number to be converted.
	 * 
	 * @param number  the number to be converted.
	 * @param isCurrency  true when is the number is a currency
	 * 
	 * @throws Exception
	 */
	public static void validateNumber(String number, boolean isCurrency) throws Exception {
		if(number == null) {
			throw new Exception("Null number!");
		} 
		
		number = number.trim();
		
		if(number.equals("")) {
			throw new Exception("Empty number specified!");
		} else if(!number.trim().matches("-?\\d+(\\.\\d+)?")) {
			throw new Exception("Specified number is NaN!");
		}
		
		try {
			if(isCurrency && number.contains(".") && Integer.parseInt(number.substring(number.indexOf(".")+1)) > 99) {		
				throw new Exception("Incorect currency format specified. Too big fraction part.");			
			}
		} catch(NumberFormatException e) {
			throw new Exception("Number could not be parsed!");	
		}
	}
}
