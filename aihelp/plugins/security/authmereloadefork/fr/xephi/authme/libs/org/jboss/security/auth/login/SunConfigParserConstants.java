package fr.xephi.authme.libs.org.jboss.security.auth.login;

public interface SunConfigParserConstants {
   int EOF = 0;
   int SINGLE_LINE_COMMENT = 5;
   int MULTI_LINE_COMMENT = 6;
   int OPEN_BKT = 7;
   int CLOSE_BKT = 8;
   int SEMI_COLON = 9;
   int EQUALS = 10;
   int LONG = 11;
   int DOUBLE = 12;
   int FLOAT = 13;
   int STRING = 14;
   int CONTROL_FLAG = 15;
   int IDENTIFIER = 16;
   int CLASSNAME = 17;
   int LETTER = 18;
   int DIGIT = 19;
   int ANY = 20;
   int NOTSPACE_EQUALS = 21;
   int DEFAULT = 0;
   String[] tokenImage = new String[]{"<EOF>", "\" \"", "\"\\r\"", "\"\\t\"", "\"\\n\"", "<SINGLE_LINE_COMMENT>", "<MULTI_LINE_COMMENT>", "\"{\"", "\"}\"", "\";\"", "\"=\"", "<LONG>", "<DOUBLE>", "<FLOAT>", "<STRING>", "<CONTROL_FLAG>", "<IDENTIFIER>", "<CLASSNAME>", "<LETTER>", "<DIGIT>", "<ANY>", "<NOTSPACE_EQUALS>"};
}
