package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineFormatter;
import java.io.IOException;

public class DefaultHttpRequestWriter extends AbstractMessageWriter<HttpRequest> {
   public DefaultHttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
      super(buffer, formatter);
   }

   public DefaultHttpRequestWriter(SessionOutputBuffer buffer) {
      this(buffer, (LineFormatter)null);
   }

   protected void writeHeadLine(HttpRequest message) throws IOException {
      this.lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }
}
