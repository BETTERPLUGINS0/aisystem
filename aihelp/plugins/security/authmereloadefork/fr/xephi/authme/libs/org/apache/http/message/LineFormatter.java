package fr.xephi.authme.libs.org.apache.http.message;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.RequestLine;
import fr.xephi.authme.libs.org.apache.http.StatusLine;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;

public interface LineFormatter {
   CharArrayBuffer appendProtocolVersion(CharArrayBuffer var1, ProtocolVersion var2);

   CharArrayBuffer formatRequestLine(CharArrayBuffer var1, RequestLine var2);

   CharArrayBuffer formatStatusLine(CharArrayBuffer var1, StatusLine var2);

   CharArrayBuffer formatHeader(CharArrayBuffer var1, Header var2);
}
