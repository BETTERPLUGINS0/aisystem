package fr.xephi.authme.libs.org.jboss.security.auth.certs;

import fr.xephi.authme.libs.org.jboss.security.CertificatePrincipal;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Locale;

public class SubjectCNMapping implements CertificatePrincipal {
   public Principal toPrincipal(X509Certificate[] certs) {
      Principal cn = null;
      Principal subject = certs[0].getSubjectDN();
      String dn = subject.getName().toLowerCase(Locale.ENGLISH);
      int index = dn.indexOf("cn=");
      if (index >= 0) {
         int comma = dn.indexOf(44, index);
         if (comma < 0) {
            comma = dn.length();
         }

         String name = dn.substring(index + 3, comma);
         cn = new SimplePrincipal(name);
      } else {
         cn = subject;
      }

      return (Principal)cn;
   }

   public Principal toPrinicipal(X509Certificate[] certs) {
      return this.toPrincipal(certs);
   }
}
