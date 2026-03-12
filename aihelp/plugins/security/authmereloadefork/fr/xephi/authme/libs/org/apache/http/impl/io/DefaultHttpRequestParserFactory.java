package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestFactory;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.config.MessageConstraints;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultHttpRequestFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParser;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.BasicLineParser;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultHttpRequestParserFactory implements HttpMessageParserFactory<HttpRequest> {
   public static final DefaultHttpRequestParserFactory INSTANCE = new DefaultHttpRequestParserFactory();
   private final LineParser lineParser;
   private final HttpRequestFactory requestFactory;

   public DefaultHttpRequestParserFactory(LineParser lineParser, HttpRequestFactory requestFactory) {
      this.lineParser = (LineParser)(lineParser != null ? lineParser : BasicLineParser.INSTANCE);
      this.requestFactory = (HttpRequestFactory)(requestFactory != null ? requestFactory : DefaultHttpRequestFactory.INSTANCE);
   }

   public DefaultHttpRequestParserFactory() {
      this((LineParser)null, (HttpRequestFactory)null);
   }

   public HttpMessageParser<HttpRequest> create(SessionInputBuffer buffer, MessageConstraints constraints) {
      return new DefaultHttpRequestParser(buffer, this.lineParser, this.requestFactory, constraints);
   }
}
