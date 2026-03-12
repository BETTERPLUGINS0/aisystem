package fr.xephi.authme.libs.org.apache.http.impl.auth;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;
import fr.xephi.authme.libs.org.apache.http.auth.AuthSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.auth.AuthSchemeProvider;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.nio.charset.Charset;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BasicSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   private final Charset charset;

   public BasicSchemeFactory(Charset charset) {
      this.charset = charset;
   }

   public BasicSchemeFactory() {
      this((Charset)null);
   }

   public AuthScheme newInstance(HttpParams params) {
      return new BasicScheme();
   }

   public AuthScheme create(HttpContext context) {
      return new BasicScheme(this.charset);
   }
}
