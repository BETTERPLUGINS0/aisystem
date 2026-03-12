package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.config.MessageConstraints;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultHttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParser;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.BasicLineParser;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultHttpResponseParserFactory implements HttpMessageParserFactory<HttpResponse> {
   public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
   private final LineParser lineParser;
   private final HttpResponseFactory responseFactory;

   public DefaultHttpResponseParserFactory(LineParser lineParser, HttpResponseFactory responseFactory) {
      this.lineParser = (LineParser)(lineParser != null ? lineParser : BasicLineParser.INSTANCE);
      this.responseFactory = (HttpResponseFactory)(responseFactory != null ? responseFactory : DefaultHttpResponseFactory.INSTANCE);
   }

   public DefaultHttpResponseParserFactory() {
      this((LineParser)null, (HttpResponseFactory)null);
   }

   public HttpMessageParser<HttpResponse> create(SessionInputBuffer buffer, MessageConstraints constraints) {
      return new DefaultHttpResponseParser(buffer, this.lineParser, this.responseFactory, constraints);
   }
}
