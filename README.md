# Number to Words Converter

This is number / currency to words converter. 

It is implemented with Java and currently supports only bulgarian language.

It was implemented due to a requirement in one project I was working.

We needed currencies to words convertion library.

I could not find an open source code so I decided to implement one and share it.

My intention in the future is to add more languages.

The repository contains an eclipse project with very few files. When compiled the application could be used

via calling NWConverter.java. Here are two examples:

Calling:

System.out.println(NWConverter.convertNumber("34.98", NWConverter.Language.BULGARIAN));
System.out.println(NWConverter.convertCurrency("34.98", NWConverter.Language.BULGARIAN));

will print:

тридесет и четири цяло и деветдесет и осем

тридесет и четири лева и деветдесет и осем стотинки

---------

As it is seen it supports numbers and currencies. 

In the features it could be extended with adding support for more currencies, not just levas,

and of course more languages.


-----------------------------------------------------------------------------------------------------------------

Това е конвертор за цифри/номера и суми в думи.
Може да се ползва в приложения, които генерират фактури
или във финансови/счедоводни системи изискващи исписването на сумите с думи.
