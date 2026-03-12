package fr.xephi.authme.libs.org.apache.http.ssl;

import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public final class PrivateKeyDetails {
   private final String type;
   private final X509Certificate[] certChain;

   public PrivateKeyDetails(String type, X509Certificate[] certChain) {
      this.type = (String)Args.notNull(type, "Private key type");
      this.certChain = certChain;
   }

   public String getType() {
      return this.type;
   }

   public X509Certificate[] getCertChain() {
      return this.certChain;
   }

   public String toString() {
      return this.type + ':' + Arrays.toString(this.certChain);
   }
}
