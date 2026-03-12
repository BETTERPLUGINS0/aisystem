package fr.xephi.authme.libs.org.jboss.security.identity.extensions;

import fr.xephi.authme.libs.org.jboss.security.identity.IdentityFactory;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;

public class CertificateIdentityFactory extends IdentityFactory {
   private static CertificateIdentityFactory _instance = null;

   protected CertificateIdentityFactory() {
   }

   public static CertificateIdentityFactory getInstance() {
      if (_instance == null) {
         _instance = new CertificateIdentityFactory();
      }

      return _instance;
   }

   public CertificateIdentity createIdentity(final Principal principal, final X509Certificate[] certs, final Role roles) {
      return new CertificateIdentity() {
         private static final long serialVersionUID = 1L;

         public X509Certificate[] getCredential() {
            return certs;
         }

         public void setCredential(X509Certificate[] certsx) {
         }

         public Group asGroup() {
            return null;
         }

         public Principal asPrincipal() {
            return principal;
         }

         public String getName() {
            return principal.getName();
         }

         public Role getRole() {
            return roles;
         }

         public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("CertificateIdentity[").append(" Certs=").append(certs).append("]");
            return builder.toString();
         }
      };
   }
}
