package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.HttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.NoHttpResponseException;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.StatusLine;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;
import fr.xephi.authme.libs.org.apache.http.message.ParserCursor;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

/** @deprecated */
@Deprecated
public class HttpResponseParser extends AbstractMessageParser<HttpMessage> {
   private final HttpResponseFactory responseFactory;
   private final CharArrayBuffer lineBuf;

   public HttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
      super(buffer, parser, params);
      this.responseFactory = (HttpResponseFactory)Args.notNull(responseFactory, "Response factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
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
