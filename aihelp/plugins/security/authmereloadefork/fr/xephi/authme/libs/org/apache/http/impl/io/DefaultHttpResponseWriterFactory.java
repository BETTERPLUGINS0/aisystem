package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
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
public class DefaultHttpResponseWriterFactory implements HttpMessageWriterFactory<HttpResponse> {
   public static final DefaultHttpResponseWriterFactory INSTANCE = new DefaultHttpResponseWriterFactory();
   private final LineFormatter lineFormatter;

   public DefaultHttpResponseWriterFactory(LineFormatter lineFormatter) {
      this.lineFormatter = (LineFormatter)(lineFormatter != null ? lineFormatter : BasicLineFormatter.INSTANCE);
   }

   public DefaultHttpResponseWriterFactory() {
      this((LineFormatter)null);
   }

   public HttpMessageWriter<HttpResponse> create(SessionOutputBuffer buffer) {
      return new DefaultHttpResponseWriter(buffer, this.lineFormatter);
   }
}
