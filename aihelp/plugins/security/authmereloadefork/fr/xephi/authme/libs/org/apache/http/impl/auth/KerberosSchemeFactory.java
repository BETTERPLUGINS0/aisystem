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
public class KerberosSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   private final boolean stripPort;
   private final boolean useCanonicalHostname;

   public KerberosSchemeFactory(boolean stripPort, boolean useCanonicalHostname) {
      this.stripPort = stripPort;
      this.useCanonicalHostname = useCanonicalHostname;
   }

   public KerberosSchemeFactory(boolean stripPort) {
      this.stripPort = stripPort;
      this.useCanonicalHostname = true;
   }

   public KerberosSchemeFactory() {
      this(true, true);
   }

   public boolean isStripPort() {
      return this.stripPort;
   }

   public boolean isUseCanonicalHostname() {
      return this.useCanonicalHostname;
   }

   public AuthScheme newInstance(HttpParams params) {
      return new KerberosScheme(this.stripPort, this.useCanonicalHostname);
   }

   public AuthScheme create(HttpContext context) {
      return new KerberosScheme(this.stripPort, this.useCanonicalHostname);
   }
}
