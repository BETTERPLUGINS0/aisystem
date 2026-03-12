package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineFormatter;
import java.io.IOException;

public class DefaultHttpResponseWriter extends AbstractMessageWriter<HttpResponse> {
   public DefaultHttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
      super(buffer, formatter);
   }

   public DefaultHttpResponseWriter(SessionOutputBuffer buffer) {
      super(buffer, (LineFormatter)null);
   }

   protected void writeHeadLine(HttpResponse message) throws IOException {
      this.lineFormatter.formatStatusLine(this.lineBuf, message.getStatusLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }
}
