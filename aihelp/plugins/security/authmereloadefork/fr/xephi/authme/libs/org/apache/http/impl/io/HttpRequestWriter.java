package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineFormatter;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.io.IOException;

/** @deprecated */
@Deprecated
public class HttpRequestWriter extends AbstractMessageWriter<HttpRequest> {
   public HttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
      super(buffer, formatter, params);
   }

   protected void writeHeadLine(HttpRequest message) throws IOException {
      this.lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }
}
