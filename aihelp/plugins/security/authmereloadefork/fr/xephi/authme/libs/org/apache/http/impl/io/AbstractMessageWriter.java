package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HeaderIterator;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriter;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.BasicLineFormatter;
import fr.xephi.authme.libs.org.apache.http.message.LineFormatter;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

public abstract class AbstractMessageWriter<T extends HttpMessage> implements HttpMessageWriter<T> {
   protected final SessionOutputBuffer sessionBuffer;
   protected final CharArrayBuffer lineBuf;
   protected final LineFormatter lineFormatter;

   /** @deprecated */
   @Deprecated
   public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
      Args.notNull(buffer, "Session input buffer");
      this.sessionBuffer = buffer;
      this.lineBuf = new CharArrayBuffer(128);
      this.lineFormatter = (LineFormatter)(formatter != null ? formatter : BasicLineFormatter.INSTANCE);
   }

   public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
      this.sessionBuffer = (SessionOutputBuffer)Args.notNull(buffer, "Session input buffer");
      this.lineFormatter = (LineFormatter)(formatter != null ? formatter : BasicLineFormatter.INSTANCE);
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected abstract void writeHeadLine(T var1) throws IOException;

   public void write(T message) throws IOException, HttpException {
      Args.notNull(message, "HTTP message");
      this.writeHeadLine(message);
      HeaderIterator it = message.headerIterator();

      while(it.hasNext()) {
         Header header = it.nextHeader();
         this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, header));
      }

      this.lineBuf.clear();
      this.sessionBuffer.writeLine(this.lineBuf);
   }
}
