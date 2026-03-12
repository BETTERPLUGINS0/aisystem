package fr.xephi.authme.libs.org.mariadb.jdbc.client.tls;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class MariaDbX509TrustingManager implements X509TrustManager {
   public void checkClientTrusted(X509Certificate[] x509Certificates, String string) {
   }

   public void checkServerTrusted(X509Certificate[] x509Certificates, String string) {
   }

   public X509Certificate[] getAcceptedIssuers() {
      return null;
   }
}
