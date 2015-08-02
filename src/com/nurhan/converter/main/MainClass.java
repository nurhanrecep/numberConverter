package com.nurhan.converter.main;

import com.nurhan.converter.NWConverter;

public class MainClass {

	public static void main(String[] args) throws Exception {		 
		System.out.println(NWConverter.convertNumber("34.98", NWConverter.Language.BULGARIAN));
		System.out.println(NWConverter.convertCurrency("34.98", NWConverter.Language.BULGARIAN));
	}
}
