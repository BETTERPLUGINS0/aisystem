package fr.xephi.authme.libs.org.postgresql.ssl.jdbc4;

import fr.xephi.authme.libs.org.postgresql.jdbc.SslMode;
import fr.xephi.authme.libs.org.postgresql.ssl.PGjdbcHostnameVerifier;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import java.net.IDN;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/** @deprecated */
@Deprecated
public class LibPQFactory extends fr.xephi.authme.libs.org.postgresql.ssl.LibPQFactory implements HostnameVerifier {
   private final SslMode sslMode;

   /** @deprecated */
   @Deprecated
   public LibPQFactory(Properties info) throws PSQLException {
      super(info);
      this.sslMode = SslMode.of(info);
   }

   /** @deprecated */
   @Deprecated
   public static boolean verifyHostName(String hostname, String pattern) {
      String canonicalHostname;
      if (hostname.startsWith("[") && hostname.endsWith("]")) {
         canonicalHostname = hostname.substring(1, hostname.length() - 1);
      } else {
         try {
            canonicalHostname = IDN.toASCII(hostname);
         } catch (IllegalArgumentException var4) {
            return false;
         }
      }

      return PGjdbcHostnameVerifier.INSTANCE.verifyHostName(canonicalHostname, pattern);
   }

   /** @deprecated */
   @Deprecated
   public boolean verify(String hostname, SSLSession session) {
      return !this.sslMode.verifyPeerName() ? true : PGjdbcHostnameVerifier.INSTANCE.verify(hostname, session);
   }
}
