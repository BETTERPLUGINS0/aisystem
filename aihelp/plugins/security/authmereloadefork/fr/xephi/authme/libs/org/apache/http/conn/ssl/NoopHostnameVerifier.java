package fr.xephi.authme.libs.org.apache.http.conn.ssl;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class NoopHostnameVerifier implements HostnameVerifier {
   public static final NoopHostnameVerifier INSTANCE = new NoopHostnameVerifier();

   public boolean verify(String s, SSLSession sslSession) {
      return true;
   }

   public final String toString() {
      return "NO_OP";
   }
}
