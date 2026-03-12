package fr.xephi.authme.libs.org.jboss.security.auth.certs;

import fr.xephi.authme.libs.org.jboss.security.CertificatePrincipal;
import java.security.Principal;
import java.security.cert.X509Certificate;

public class SubjectX500Principal implements CertificatePrincipal {
   public Principal toPrincipal(X509Certificate[] certs) {
      Principal subject = certs[0].getSubjectX500Principal();
      return subject;
   }

   public Principal toPrinicipal(X509Certificate[] certs) {
      return this.toPrincipal(certs);
   }
}
