package fr.xephi.authme.libs.org.jboss.security.auth.certs;

import fr.xephi.authme.libs.org.jboss.security.CertificatePrincipal;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.io.Serializable;
import java.security.Principal;
import java.security.cert.X509Certificate;

public class SubjectDNMapping implements CertificatePrincipal {
   public Principal toPrincipal(X509Certificate[] certs) {
      Principal subject = certs[0].getSubjectDN();
      if (!(subject instanceof Serializable)) {
         String name = ((Principal)subject).getName();
         subject = new SimplePrincipal(name);
      }

      return (Principal)subject;
   }

   public Principal toPrinicipal(X509Certificate[] certs) {
      return this.toPrincipal(certs);
   }
}
