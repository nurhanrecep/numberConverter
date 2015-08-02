package com.nurhan.converter;

import java.util.ArrayList;

import com.nurhan.converter.bean.ConverterNumber;
import com.nurhan.converter.util.ConverterUtil;

/**
 * Number converter for bulgarian numbers.
 * 
 * @author Nurhan R. Mustafa
 * @version 1.1
 */
class BulgarianConverter {
	/** minus word */
	private static final String MINUS = "минус "; 
	
	/** currency and currency sub unit's words*/
	private static final String CURRENCY = " лев";
	private static final String CURRENCY_PLURAL = " лева";	
	private static final String CURRENCY_SUB_UNIT = " стотинка";
	private static final String CURRENCY_SUB_UNIT_PLURAL = " стотинки";
	
	/** letter added to the end of some words to make it plural */
	private static final String PLURAL_SUFFIX = "а";
	
	/** letter used for 'and' word in bulgarian */
	private static final String AND = "и";
	
	/** a word used when spelling the floating numbers as a conjunction between the integer and fraction number */
	private static final String DOT_WORD = " цяло ";
	
	/** suffix word added after TEN word related to numbers between 11 and 19 including */
	private static final String DECIMAL_CONECTIVE = "на";
	
	private static final String HUNDRED = "сто";
	
	/** a suffix for 200 and 300 words */
	private static final String NUNDRED_TWO_THREE_SUFFIX = "ста";
	
	/** a suffix for 400, 500, 600, 700, 800 and 900 words */
	private static final String NUNDRED_FOUR_TO_NIGN_SUFFIX = "стотин";

	private static final String THOUSANDS = "хиляди";
	
	/** number exponent's words as 1 , 1 000, 1 000 000, 1 000 000 000, ... */
	private static final String[] TRIPLE_EXPONENT = {"един","хиляда", "милион", "милиард", "трилион", "квадрилион", "квинтилион", "секстилион"};
	
	/** number's spellings from 1 to 10 for number's fraction part */
	private static final String[] CURRENCY_FRACTION_NUMBERS = {"", "една", "две", "три", "четири", "пет", "шест", "седем", "осем", "девет", "десет" };
	
	/** number's words for simple numbers and currency respectively */
	private static final String[][] NUMBERS_WORDS = new String[][] {
			{ "нула", "едно", "две", "три", "четири", "пет", "шест", "седем", "осем", "девет", "десет" },
			{ "", "един", "два", "три", "четири", "пет", "шест", "седем", "осем", "девет", "десет" } };
	
	/**  used with NUMBERS_WORDS, for currency is 1, for numbers 0, usage is like NUMBERS_WORDS[wordsGroup][0] */
	private static int wordsGroup = 0; 
	
	/** constants representing the number type in NUMBERS_WORDS array, the 'wordsGroup' is set to one of them */
	private static final int CURRENCY_WORDS_GROUP = 1;
	private static final int NUMBERS_WORDS_GROUP = 0;
	
	public enum NumberPart { INTEGER, FRACTION } // number parts
	
	private static ConverterNumber convertedNumber = null;
			
	public static synchronized String convert(String number, boolean forCurrency) throws Exception {		

		ConverterUtil.validateNumber(number, forCurrency);
		
		/** number represented as instance */
		convertedNumber = new ConverterNumber(number, forCurrency);
		
		/** determining which word group to use, currency or simple number's */
		wordsGroup = forCurrency ? CURRENCY_WORDS_GROUP : NUMBERS_WORDS_GROUP; 
		
		/** if is negative append the minus word */
		convertedNumber.appendWord(convertedNumber.isNegativeNumber() ? MINUS: ""); // append sign word
		
		appendIntegerPartWord();
	
		/** set the word between integer and fraction parts used as conjunction when spelling the number */
		if(forCurrency) { 
			convertedNumber.appendWord(getCurrencyWord());	
			
			if(!convertedNumber.getIntegerPart().equals("") && !convertedNumber.getFractionalPart().equals("")) {
				convertedNumber.appendWord(" ");
				convertedNumber.appendWord(AND);
				convertedNumber.appendWord(" ");
			}
		} else if(convertedNumber.isFloatingNumber()) {
			convertedNumber.appendWord(DOT_WORD);	
			convertedNumber.appendWord(AND);
			convertedNumber.appendWord(" ");
		}
		
		appendFractionPartWord();
		
		if(forCurrency && convertedNumber.isFloatingNumber()) {
			convertedNumber.appendWord(getCurrencySubUnitWord());
		}
	
		return convertedNumber.getNumberInWordRepresentation();
	}
	
	private static void appendIntegerPartWord() {
		if (!convertedNumber.isCurrency()) {
			if (convertedNumber.getIntegerPart().equals("")) {
				convertedNumber.appendWord(NUMBERS_WORDS[wordsGroup][0]); // append ZERO
				return;
			} else if (convertedNumber.getIntegerPart().equals("0")) {
				convertedNumber.appendWord(NUMBERS_WORDS[NUMBERS_WORDS_GROUP][0]); // append ZERO
			}
		}
		
		convertNumberToWord(convertedNumber.getIntegerPart(), NumberPart.INTEGER);
	}
	
	/**
	 * Converts the number to words.
	 * 
	 * @param number a number to be translated
	 * @param numberPart the part of the number: integer or fraction
	 * @return the given number in words form
	 */
	private static void convertNumberToWord(String number, NumberPart numberPart) {
		/** the number is separated to triples */
		ArrayList<String> tripples = getNumberAsTriples(number);
		int tripplesArraySize = tripples.size();
		String aTripleAsWords = "";
		int aTripleAsDecimal = 0;
		
		for (int tripleIndex = tripplesArraySize - 1; tripleIndex >= 0; tripleIndex--) {
			aTripleAsWords = getTripleAsWord(tripples.get(tripleIndex), numberPart);
			aTripleAsDecimal = Integer.parseInt(tripples.get(tripleIndex));
			
			if(aTripleAsDecimal == 0) {
				// if is 000 or 00 or 0: write nothing
				continue;
			}
			
			if(aTripleAsDecimal == 1) { // for 1, there is a special case
				if(tripleIndex == 1) { // is the group before the last triple(between 999 and 100 000 part of the number)
					convertedNumber.appendWord(TRIPLE_EXPONENT[1]);
				} else if(tripleIndex > 1) { // is greater than 999 000					
					convertedNumber.appendWord(TRIPLE_EXPONENT[0]);
					convertedNumber.appendWord(" ");	
					convertedNumber.appendWord(TRIPLE_EXPONENT[tripleIndex]);
				} else { // is less than 1000, is last triple
					convertedNumber.appendWord(aTripleAsWords);				
				}
			} else { // for plurals
				if (tripleIndex > 0 && tripples.get(tripleIndex).length() > 1 && 
					tripples.get(tripleIndex).endsWith("1") && convertedNumber.isCurrency()) {
					// the word ends with 1 and is for currency, so the word 'one' is replaced with it's famine spelling
					aTripleAsWords = aTripleAsWords.replaceAll(NUMBERS_WORDS[1][1], CURRENCY_FRACTION_NUMBERS[1]);
					convertedNumber.appendWord(aTripleAsWords);				
					convertedNumber.appendWord(" ");	
				} else if(!(tripleIndex == 1 && aTripleAsDecimal == 2)) {
					convertedNumber.appendWord(aTripleAsWords);						
				}
								
				if(tripleIndex == 1) { // is thousands
					if(aTripleAsDecimal == 2) {
						convertedNumber.appendWord(NUMBERS_WORDS[1][2]);
						convertedNumber.appendWord(" ");	
					} else {
						convertedNumber.appendWord(" ");	
					}
					
					convertedNumber.appendWord(THOUSANDS);						
				} else if(tripleIndex > 1) {
					convertedNumber.appendWord(" ");
					convertedNumber.appendWord(TRIPLE_EXPONENT[tripleIndex]); 
					convertedNumber.appendWord(PLURAL_SUFFIX);	
				}
			}
			if(tripleIndex != 0){
				convertedNumber.appendWord(" ");	
			}
			//in some cases must be appended an AND word
			addANDLetter(tripleIndex, tripples);		
		}		
	}
	
	/**
	 * Determines according to some rules whether to append AND word or not.
	 * 
	 * @param trippleArrayIndex
	 * @param numberAsTriples
	 */
	private static void addANDLetter(int trippleArrayIndex, ArrayList<String> numberAsTriples) {
		if((trippleArrayIndex - 1) < 0) {
			// this is last triple, so there is not need for AND word
			return;
		}
		
		if (!numberAsTriples.get(trippleArrayIndex - 1).endsWith("000")
				&& (numberAsTriples.get(trippleArrayIndex - 1).charAt(0) == '0' || 
				    numberAsTriples.get(trippleArrayIndex - 1).endsWith("00"))) {
			convertedNumber.appendWord(AND);
			convertedNumber.appendWord(" ");
		} else if (numberAsTriples.get(trippleArrayIndex - 1).endsWith("000")) {
			for (int i = trippleArrayIndex - 1; i > 0; i--) {
				if ((i - 1) > -1 && !numberAsTriples.get(i - 1).endsWith("000")
						&& (numberAsTriples.get(i - 1).charAt(0) == '0' || numberAsTriples.get(i - 1).endsWith("00"))) {
					convertedNumber.appendWord(AND);
					convertedNumber.appendWord(" ");
					break;
				}
			}
		}
	}
	
	private static void appendFractionPartWord() {
		if(!convertedNumber.getFractionalPart().equals("")) {			
			convertNumberToWord(convertedNumber.getFractionalPart(), NumberPart.FRACTION);	
		}			
	}

	/**
	 * Translates the number between 0...999 to it's word representation.
	 * 
	 * @param number a number to be translated
	 * @return a number as word(s)
	 */
	private static String getTripleAsWord(String number, NumberPart numberPart) {
		int numberLength = number.length();
		String translatedTriple = "";
	
		if(numberLength == 3) {
			translatedTriple = parseThreeDigitTriple(number, numberPart);
		} else if(numberLength == 2) {
			translatedTriple = parseTwoDigitTriple(number, numberPart);
		} else {
			translatedTriple = parseOneDigitTriple(number, numberPart);
		}	
		
		return translatedTriple;
	}
	
	private static String parseOneDigitTriple(String number, NumberPart numberPart) { 
		
		if(convertedNumber.isCurrency() && numberPart == NumberPart.FRACTION) {
			return CURRENCY_FRACTION_NUMBERS[Integer.parseInt(number)];
		} 
		
		return NUMBERS_WORDS[wordsGroup][Integer.parseInt(number)];	
	}
	
	private static String parseTwoDigitTriple(String number, NumberPart numberPart) {
		int numberAsDecimal = Integer.parseInt(number);
		String decimalAsWord = "";
		
		if(numberAsDecimal < 11) { // cases as: 00, 01, 10, 09, ... etc.
			decimalAsWord = parseOneDigitTriple(number, numberPart);
		} else if(numberAsDecimal == 11) {
			decimalAsWord = NUMBERS_WORDS[1][numberAsDecimal-10] + PLURAL_SUFFIX +  NUMBERS_WORDS[1][10];	
		} else if(numberAsDecimal < 20) {
			decimalAsWord = NUMBERS_WORDS[1][numberAsDecimal-10] + DECIMAL_CONECTIVE +  NUMBERS_WORDS[1][10];	
		} else {
			int firstDigit = Integer.parseInt(number.substring(0,1));
			int secondDigit = Integer.parseInt(number.substring(1,2));
			
			if(secondDigit == 0) {
				decimalAsWord = NUMBERS_WORDS[1][firstDigit] + NUMBERS_WORDS[1][10];
			} else {
				decimalAsWord = NUMBERS_WORDS[1][firstDigit] + NUMBERS_WORDS[1][10] + " " + AND + " " + parseOneDigitTriple(number.substring(1,2), numberPart);
			}	
		}
		
		return decimalAsWord;
	}
	
	private static String parseThreeDigitTriple(String number, NumberPart numberPart) {
		int numberAsDecimal = Integer.parseInt(number);
		String decimalAsWord = "";
		
		if(numberAsDecimal < 100) {
			decimalAsWord = parseTwoDigitTriple(number.substring(1), numberPart);
		} else if(numberAsDecimal == 100) {
			decimalAsWord = HUNDRED;
		} else if (numberAsDecimal < 200) {
			decimalAsWord = HUNDRED;

			if (numberAsDecimal < 121 || number.endsWith("0")) {
				decimalAsWord += " " + AND + " " ; 
			} else {
				decimalAsWord += " ";
			}
			
			decimalAsWord += parseTwoDigitTriple(number.substring(1), numberPart);			
		} else {
			int firsDigit = Integer.parseInt(number.substring(0, 1));
			
			if (numberAsDecimal < 400) {
				decimalAsWord =  NUMBERS_WORDS[0][firsDigit] + NUNDRED_TWO_THREE_SUFFIX ; // for 200 and 300
			} else {
				decimalAsWord =  NUMBERS_WORDS[0][firsDigit] + NUNDRED_FOUR_TO_NIGN_SUFFIX  ;
			}	
						
			if(!number.substring(1,3).equals("00")) {
				int remainingTwoDigits = Integer.parseInt(number.substring(1));
				
				if(remainingTwoDigits < 21 || remainingTwoDigits%10 == 0) {
					decimalAsWord += " " + AND + " " ;
				} else {
					decimalAsWord += " ";
				}
				
				decimalAsWord += parseTwoDigitTriple(number.substring(1), numberPart);
			}
		}
		
		return decimalAsWord;
	}
	
	private static String getCurrencySubUnitWord() {		
		if(Integer.parseInt(convertedNumber.getFractionalPart()) == 1) {
			return CURRENCY_SUB_UNIT;
		} else {
			return CURRENCY_SUB_UNIT_PLURAL;
		}
	}

	private static String getCurrencyWord() {			
		if(convertedNumber.getIntegerPart().equals("") || convertedNumber.getIntegerPart().equals("0")) {
			return ""; // write nothing for zero
		} else if(convertedNumber.getIntegerPart().equals("1")) { 
			return CURRENCY; // write nothing for zero
		} else {
			return CURRENCY_PLURAL; // write nothing for zero
		}
	}
	
	/**
	 * Divides the number to a group of triples.
	 * Example: for 23459 the result is triples in two groups 23 459. For 1233009 -> 1 233 009
	 * 
	 * @param number the number to be divided
	 * @return the number divided in groups of triples
	 */
	private static ArrayList<String> getNumberAsTriples(String number) {
		ArrayList<String> numberAsTriples = new ArrayList<String>();
		
		int numberBeginIndex = (number.length() - 3) < 0 ? 0: (number.length() - 3);
		int numberEndIndex = number.length();
		
		while(numberEndIndex > 0) {
			numberAsTriples.add(number.substring(numberBeginIndex, numberEndIndex));
			numberEndIndex = numberBeginIndex;
			numberBeginIndex = (numberBeginIndex - 3) < 0 ? 0: (numberBeginIndex - 3);
		}
		
		return numberAsTriples;
	}	
}

