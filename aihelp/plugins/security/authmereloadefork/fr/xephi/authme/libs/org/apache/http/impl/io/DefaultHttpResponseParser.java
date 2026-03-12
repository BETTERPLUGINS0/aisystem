package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.NoHttpResponseException;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.StatusLine;
import fr.xephi.authme.libs.org.apache.http.config.MessageConstraints;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultHttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;
import fr.xephi.authme.libs.org.apache.http.message.ParserCursor;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

public class DefaultHttpResponseParser extends AbstractMessageParser<HttpResponse> {
   private final HttpResponseFactory responseFactory;
   private final CharArrayBuffer lineBuf;

   /** @deprecated */
   @Deprecated
   public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser lineParser, HttpResponseFactory responseFactory, HttpParams params) {
      super(buffer, lineParser, params);
      this.responseFactory = (HttpResponseFactory)Args.notNull(responseFactory, "Response factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser lineParser, HttpResponseFactory responseFactory, MessageConstraints constraints) {
      super(buffer, lineParser, constraints);
      this.responseFactory = (HttpResponseFactory)(responseFactory != null ? responseFactory : DefaultHttpResponseFactory.INSTANCE);
      this.lineBuf = new CharArrayBuffer(128);
   }

   public DefaultHttpResponseParser(SessionInputBuffer buffer, MessageConstraints constraints) {
      this(buffer, (LineParser)null, (HttpResponseFactory)null, (MessageConstraints)constraints);
   }

   public DefaultHttpResponseParser(SessionInputBuffer buffer) {
      this(buffer, (LineParser)null, (HttpResponseFactory)null, (MessageConstraints)MessageConstraints.DEFAULT);
   }

   protected HttpResponse parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
      this.lineBuf.clear();
      int readLen = sessionBuffer.readLine(this.lineBuf);
      if (readLen == -1) {
         throw new NoHttpResponseException("The target server failed to respond");
      } else {
         ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
         StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
         return this.responseFactory.newHttpResponse(statusline, (HttpContext)null);
      }
   }
}
