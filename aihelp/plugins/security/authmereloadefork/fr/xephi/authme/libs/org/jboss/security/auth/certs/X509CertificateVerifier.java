package fr.xephi.authme.libs.org.jboss.security.auth.certs;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

public interface X509CertificateVerifier {
   boolean verify(X509Certificate var1, String var2, KeyStore var3, KeyStore var4);
}
