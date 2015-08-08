package com.nurhan.converter;

/**
 * Number to words converter.
 * Each language converter must extend this interface.
 * 
 *
 * @author Nurhan R. Mustafa
 */

interface Converter {
	
	/**
	 * Converts the number/currency.
	 * 
	 * @param number to number to be converted
	 * @param forCurrency  true if the number represents a currency
	 * @return word representation of the number/currency
	 * @throws Exception
	 */
	String convert(String number, boolean forCurrency) throws Exception;
}
