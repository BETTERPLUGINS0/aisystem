package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.Consts;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.io.HttpTransportMetrics;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.util.CharArrayBuffer;
import java.io.IOException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class LoggingSessionOutputBuffer implements SessionOutputBuffer {
   private final SessionOutputBuffer out;
   private final Wire wire;
   private final String charset;

   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset) {
      this.out = out;
      this.wire = wire;
      this.charset = charset != null ? charset : Consts.ASCII.name();
   }

   public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire) {
      this(out, wire, (String)null);
   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.out.write(b, off, len);
      if (this.wire.enabled()) {
         this.wire.output(b, off, len);
      }

   }

   public void write(int b) throws IOException {
      this.out.write(b);
      if (this.wire.enabled()) {
         this.wire.output(b);
      }

   }

   public void write(byte[] b) throws IOException {
      this.out.write(b);
      if (this.wire.enabled()) {
         this.wire.output(b);
      }

   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void writeLine(CharArrayBuffer buffer) throws IOException {
      this.out.writeLine(buffer);
      if (this.wire.enabled()) {
         String s = new String(buffer.buffer(), 0, buffer.length());
         String tmp = s + "\r\n";
         this.wire.output(tmp.getBytes(this.charset));
      }

   }

   public void writeLine(String s) throws IOException {
      this.out.writeLine(s);
      if (this.wire.enabled()) {
         String tmp = s + "\r\n";
         this.wire.output(tmp.getBytes(this.charset));
      }

   }

   public HttpTransportMetrics getMetrics() {
      return this.out.getMetrics();
   }
}
