package fr.xephi.authme.libs.org.apache.http.message;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.RequestLine;
import fr.xephi.authme.libs.org.apache.http.StatusLine;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;

public interface LineParser {
   ProtocolVersion parseProtocolVersion(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

   boolean hasProtocolVersion(CharArrayBuffer var1, ParserCursor var2);

   RequestLine parseRequestLine(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

   StatusLine parseStatusLine(CharArrayBuffer var1, ParserCursor var2) throws ParseException;

   Header parseHeader(CharArrayBuffer var1) throws ParseException;
}
