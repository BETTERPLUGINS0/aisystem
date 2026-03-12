package fr.xephi.authme.libs.org.apache.http.impl.cookie;

import fr.xephi.authme.libs.org.apache.http.HeaderElement;
import fr.xephi.authme.libs.org.apache.http.NameValuePair;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.message.BasicHeaderElement;
import fr.xephi.authme.libs.org.apache.http.message.BasicNameValuePair;
import fr.xephi.authme.libs.org.apache.http.message.ParserCursor;
import fr.xephi.authme.libs.org.apache.http.message.TokenParser;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;
import java.util.ArrayList;
import java.util.BitSet;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NetscapeDraftHeaderParser {
   public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
   private static final char PARAM_DELIMITER = ';';
   private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(61, 59);
   private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(59);
   private final TokenParser tokenParser;

   public NetscapeDraftHeaderParser() {
      this.tokenParser = TokenParser.INSTANCE;
   }

   public HeaderElement parseHeader(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
      Args.notNull(buffer, "Char array buffer");
      Args.notNull(cursor, "Parser cursor");
      NameValuePair nvp = this.parseNameValuePair(buffer, cursor);
      ArrayList params = new ArrayList();

      while(!cursor.atEnd()) {
         NameValuePair param = this.parseNameValuePair(buffer, cursor);
         params.add(param);
      }

      return new BasicHeaderElement(nvp.getName(), nvp.getValue(), (NameValuePair[])params.toArray(new NameValuePair[params.size()]));
   }

   private NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
      String name = this.tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
      if (cursor.atEnd()) {
         return new BasicNameValuePair(name, (String)null);
      } else {
         int delim = buffer.charAt(cursor.getPos());
         cursor.updatePos(cursor.getPos() + 1);
         if (delim != '=') {
            return new BasicNameValuePair(name, (String)null);
         } else {
            String value = this.tokenParser.parseToken(buffer, cursor, VALUE_DELIMS);
            if (!cursor.atEnd()) {
               cursor.updatePos(cursor.getPos() + 1);
            }

            return new BasicNameValuePair(name, value);
         }
      }
   }
}
