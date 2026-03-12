package fr.xephi.authme.libs.org.apache.http.io;

import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.config.MessageConstraints;

public interface HttpMessageParserFactory<T extends HttpMessage> {
   HttpMessageParser<T> create(SessionInputBuffer var1, MessageConstraints var2);
}
