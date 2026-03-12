package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriter;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriterFactory;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.BasicLineFormatter;
import fr.xephi.authme.libs.org.apache.http.message.LineFormatter;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultHttpRequestWriterFactory implements HttpMessageWriterFactory<HttpRequest> {
   public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();
   private final LineFormatter lineFormatter;

   public DefaultHttpRequestWriterFactory(LineFormatter lineFormatter) {
      this.lineFormatter = (LineFormatter)(lineFormatter != null ? lineFormatter : BasicLineFormatter.INSTANCE);
   }

   public DefaultHttpRequestWriterFactory() {
      this((LineFormatter)null);
   }

   public HttpMessageWriter<HttpRequest> create(SessionOutputBuffer buffer) {
      return new DefaultHttpRequestWriter(buffer, this.lineFormatter);
   }
}
