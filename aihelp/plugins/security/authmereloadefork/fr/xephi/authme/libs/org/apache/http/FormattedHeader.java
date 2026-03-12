package fr.xephi.authme.libs.org.apache.http;

import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;

public interface FormattedHeader extends Header {
   CharArrayBuffer getBuffer();

   int getValuePos();
}
