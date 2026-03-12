package fr.xephi.authme.libs.org.postgresql.ssl;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NonValidatingFactory extends WrappedFactory {
   public NonValidatingFactory(String arg) throws GeneralSecurityException {
      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init((KeyManager[])null, new TrustManager[]{new NonValidatingFactory.NonValidatingTM()}, (SecureRandom)null);
      this.factory = ctx.getSocketFactory();
   }

   public static class NonValidatingTM implements X509TrustManager {
      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }

      public void checkClientTrusted(X509Certificate[] certs, String authType) {
      }

      public void checkServerTrusted(X509Certificate[] certs, String authType) {
      }
   }
}
