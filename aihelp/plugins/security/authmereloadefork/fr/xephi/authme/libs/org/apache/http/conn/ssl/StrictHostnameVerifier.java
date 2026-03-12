package fr.xephi.authme.libs.org.apache.http.conn.ssl;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import javax.net.ssl.SSLException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class StrictHostnameVerifier extends AbstractVerifier {
   public static final StrictHostnameVerifier INSTANCE = new StrictHostnameVerifier();

   public final void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
      this.verify(host, cns, subjectAlts, true);
   }

   public final String toString() {
      return "STRICT";
   }
}
