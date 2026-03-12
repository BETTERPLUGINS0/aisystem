package fr.xephi.authme.libs.org.apache.http.conn.ssl;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class AllowAllHostnameVerifier extends AbstractVerifier {
   public static final AllowAllHostnameVerifier INSTANCE = new AllowAllHostnameVerifier();

   public final void verify(String host, String[] cns, String[] subjectAlts) {
   }

   public final String toString() {
      return "ALLOW_ALL";
   }
}
