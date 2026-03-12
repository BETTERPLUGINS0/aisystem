package fr.xephi.authme.libs.org.jboss.security;

import java.security.Principal;
import java.security.cert.X509Certificate;

public interface CertificatePrincipal {
   /** @deprecated */
   Principal toPrinicipal(X509Certificate[] var1);

   Principal toPrincipal(X509Certificate[] var1);
}
