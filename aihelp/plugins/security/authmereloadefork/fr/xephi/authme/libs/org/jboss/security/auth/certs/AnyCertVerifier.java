package fr.xephi.authme.libs.org.jboss.security.auth.certs;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class AnyCertVerifier implements X509CertificateVerifier {
   public boolean verify(X509Certificate cert, String alias, KeyStore keyStore, KeyStore trustStore) {
      return true;
   }
}
