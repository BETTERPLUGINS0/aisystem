package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.ConnectionClosedException;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.HttpRequestFactory;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.RequestLine;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;
import fr.xephi.authme.libs.org.apache.http.message.ParserCursor;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

/** @deprecated */
@Deprecated
public class HttpRequestParser extends AbstractMessageParser<HttpMessage> {
   private final HttpRequestFactory requestFactory;
   private final CharArrayBuffer lineBuf;

   public HttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, HttpParams params) {
      super(buffer, parser, params);
      this.requestFactory = (HttpRequestFactory)Args.notNull(requestFactory, "Request factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
      this.lineBuf.clear();
      int readLen = sessionBuffer.readLine(this.lineBuf);
      if (readLen == -1) {
         throw new ConnectionClosedException("Client closed connection");
      } else {
         ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
         RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
         return this.requestFactory.newHttpRequest(requestline);
      }
   }
}
