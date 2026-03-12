package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.ReasonPhraseCatalog;
import fr.xephi.authme.libs.org.apache.http.StatusLine;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.message.BasicHttpResponse;
import fr.xephi.authme.libs.org.apache.http.message.BasicStatusLine;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.util.Locale;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultHttpResponseFactory implements HttpResponseFactory {
   public static final DefaultHttpResponseFactory INSTANCE = new DefaultHttpResponseFactory();
   protected final ReasonPhraseCatalog reasonCatalog;

   public DefaultHttpResponseFactory(ReasonPhraseCatalog catalog) {
      this.reasonCatalog = (ReasonPhraseCatalog)Args.notNull(catalog, "Reason phrase catalog");
   }

   public DefaultHttpResponseFactory() {
      this(EnglishReasonPhraseCatalog.INSTANCE);
   }

   public HttpResponse newHttpResponse(ProtocolVersion ver, int status, HttpContext context) {
      Args.notNull(ver, "HTTP version");
      Locale loc = this.determineLocale(context);
      String reason = this.reasonCatalog.getReason(status, loc);
      StatusLine statusline = new BasicStatusLine(ver, status, reason);
      return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
   }

   public HttpResponse newHttpResponse(StatusLine statusline, HttpContext context) {
      Args.notNull(statusline, "Status line");
      return new BasicHttpResponse(statusline, this.reasonCatalog, this.determineLocale(context));
   }

   protected Locale determineLocale(HttpContext context) {
      return Locale.getDefault();
   }
}
