package fr.xephi.authme.libs.org.apache.http.message;

import fr.xephi.authme.libs.org.apache.http.HeaderElement;
import fr.xephi.authme.libs.org.apache.http.NameValuePair;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;

public interface HeaderValueParser {
   HeaderElement[] parseElements(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

   HeaderElement parseHeaderElement(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

   NameValuePair[] parseParameters(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

   NameValuePair parseNameValuePair(CharArrayBuffer var1, ParserCursor var2) throws ParseException;
}
