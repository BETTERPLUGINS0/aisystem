package fr.xephi.authme.libs.org.jboss.security.identity;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.extensions.CertificateIdentityFactory;
import fr.xephi.authme.libs.org.jboss.security.identity.extensions.CredentialIdentityFactory;
import java.security.Principal;
import java.security.cert.X509Certificate;

public class IdentityFactory {
   public static IdentityFactory getFactory(IdentityType type) {
      if (type == IdentityType.CREDENTIAL) {
         return CredentialIdentityFactory.getInstance();
      } else if (type == IdentityType.CERTIFICATE) {
         return CertificateIdentityFactory.getInstance();
      } else {
         throw PicketBoxMessages.MESSAGES.identityTypeFactoryNotImplemented(type != null ? type.name() : null);
      }
   }

   public static Identity getIdentity(Principal principal, Object credential) {
      return CredentialIdentityFactory.createIdentity(principal, credential);
   }

   public static Identity getIdentity(Principal principal, X509Certificate[] certs, Role roles) {
      CertificateIdentityFactory identityFactory = CertificateIdentityFactory.getInstance();
      return identityFactory.createIdentity(principal, certs, roles);
   }
}
