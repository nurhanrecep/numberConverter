package com.nurhan.converter.bean;

/**
 * The number to be converted.
 * 
 * @author Nurhan R. Mustafa
 * @version 1.1
 */
public class ConverterNumber {
	/** true - is a currency value, false - is a regular number */
	private boolean currency;

	/** the original number to be converted to words */
	private String originalNumber;

	/**
	 * the part before the decimal point, without the minus sign (if there is
	 * such). for example, for 34.55 the value of integerPart will be 34. 
	 * The 0 values are represented as empty string
	 */
	private String integerPart;

	/**
	 * the fractional part after the decimal point (if there is such) for
	 * example, for 34.55 the value of fractionalPart will be 55. The 0 values
	 * are represented as empty string
	 */
	private String fractionalPart;

	/** true if is a floating number */
	private boolean floatingNumber;

	/** true if is a negative number */
	private boolean negativeNumber;

	/** the number represented in word(s) */
	private StringBuilder wordRepresentation;

	public ConverterNumber(String number, boolean isCurrency) {
		this.originalNumber = number;
		this.currency = isCurrency;
		this.wordRepresentation = new StringBuilder("");
		initNumberFields();
	}

	/**
	 * Initialize current instance fields.
	 */
	private void initNumberFields() {
		String originalNumberNewForm = this.originalNumber.trim();

		if (originalNumberNewForm.startsWith("-")) { 
			this.negativeNumber = true;
			originalNumberNewForm = originalNumberNewForm.substring(1); // remove the minus sign
		} else {
			this.negativeNumber = false;
		}

		if (originalNumberNewForm.contains(".")) { // if the number is floating, get the integer and fraction parts
			this.integerPart = cleanZeros(originalNumberNewForm.substring(0, originalNumberNewForm.indexOf(".")));
			this.fractionalPart = cleanZeros(originalNumberNewForm.substring(
					originalNumberNewForm.indexOf(".") + 1,
					originalNumberNewForm.length()));
		} else {
			this.integerPart = cleanZeros(originalNumberNewForm);
			this.fractionalPart = "";
		}

		if (this.fractionalPart.equals("")) { // if the number is not floating or the floating part is 0
			this.floatingNumber = false;
		} else {
			this.floatingNumber = true;
		}

		if (this.integerPart.equals("") && this.fractionalPart.equals("")) { // if both are empty, that eans this is a 0 number or 0.0
			this.negativeNumber = false;
			this.integerPart = "0";
		}
	}

	/**
	 * Clears the zeros which are (could be) in front of the number. 
	 * exp: for 0034.5 returns 34.5, for -00076 returns -76.
	 * 
	 * @param numberAsString an original number to be translated
	 * @return the number without the zeros which are (could be) in front of the number.
	 */
	private String cleanZeros(String numberAsString) {
		int firstNotZeroNumberIndex = -1;
		int numberLength = numberAsString.length();

		for (int i = 0; i < numberLength; i++) { // find the first not zero digit's place in the number
			if (numberAsString.charAt(i) != '0') {
				break;
			} else {
				firstNotZeroNumberIndex = i;
			}
		}

		return numberAsString.substring(firstNotZeroNumberIndex + 1);
	}

	public boolean isCurrency() {
		return currency;
	}

	public void setCurrency(boolean currency) {
		this.currency = currency;
	}

	public String getOriginalNumber() {
		return originalNumber;
	}

	public void setOriginalNumber(String originalNumber) {
		this.originalNumber = originalNumber;
		this.wordRepresentation = new StringBuilder("");
		initNumberFields();
	}

	public String getIntegerPart() {
		return integerPart;
	}

	public void setIntegerPart(String integerPart) {
		this.integerPart = integerPart;
	}

	public String getFractionalPart() {
		return fractionalPart;
	}

	public void setFractionalPart(String fractionalPart) {
		this.fractionalPart = fractionalPart;
	}

	public boolean isFloatingNumber() {
		return floatingNumber;
	}

	public void setFloatingNumber(boolean floatingNumber) {
		this.floatingNumber = floatingNumber;
	}

	public boolean isNegativeNumber() {
		return negativeNumber;
	}

	public void setNegativeNumber(boolean negativeNumber) {
		this.negativeNumber = negativeNumber;
	}

	public void appendWord(String word) {
		this.wordRepresentation.append(word);
	}
	
	public String getNumberInWordRepresentation() {
		return this.wordRepresentation.toString();
	}
}
