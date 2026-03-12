package fr.xephi.authme.libs.org.jboss.security.auth.certs;

import fr.xephi.authme.libs.org.jboss.security.CertificatePrincipal;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.X509Certificate;

public class SerialNumberIssuerDNMapping implements CertificatePrincipal {
   public Principal toPrincipal(X509Certificate[] certs) {
      BigInteger serialNumber = certs[0].getSerialNumber();
      Principal issuer = certs[0].getIssuerDN();
      SimplePrincipal principal = new SimplePrincipal(serialNumber + " " + issuer);
      return principal;
   }

   public Principal toPrinicipal(X509Certificate[] certs) {
      return this.toPrincipal(certs);
   }
}
