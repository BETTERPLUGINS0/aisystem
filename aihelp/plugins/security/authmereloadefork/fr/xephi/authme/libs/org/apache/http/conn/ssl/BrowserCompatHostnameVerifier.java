package fr.xephi.authme.libs.org.apache.http.conn.ssl;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import javax.net.ssl.SSLException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BrowserCompatHostnameVerifier extends AbstractVerifier {
   public static final BrowserCompatHostnameVerifier INSTANCE = new BrowserCompatHostnameVerifier();

   public final void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
      this.verify(host, cns, subjectAlts, false);
   }

   public final String toString() {
      return "BROWSER_COMPATIBLE";
   }
}
