package fr.xephi.authme.libs.org.apache.http.impl.auth;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;
import fr.xephi.authme.libs.org.apache.http.auth.AuthSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.auth.AuthSchemeProvider;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NTLMSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   public AuthScheme newInstance(HttpParams params) {
      return new NTLMScheme();
   }

   public AuthScheme create(HttpContext context) {
      return new NTLMScheme();
   }
}
