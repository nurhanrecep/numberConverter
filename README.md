# numberConvertor
This is number to word converter application. It is implemented with Java and currently supports only bulgarian language.
It was implemented due to a requirement in one project for currencies to words convertion when generating invoice payments.
My intention in the future is to add more languages.

The repository contains an eclipse project with very few files. When compiled the application could be used
via calling NWConverter.java. Here are some examples:

Calling:

System.out.println(NWConverter.convertNumber("34.98", NWConverter.Language.BULGARIAN));
System.out.println(NWConverter.convertCurrency("34.98", NWConverter.Language.BULGARIAN));

will print:

тридесет и четири цяло и деветдесет и осем
тридесет и четири лева и деветдесет и осем стотинки


