package fr.xephi.authme.libs.org.jboss.security.identity.extensions;

import fr.xephi.authme.libs.org.jboss.security.identity.IdentityFactory;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import java.security.Principal;
import java.security.acl.Group;

public class CredentialIdentityFactory extends IdentityFactory {
   public static final CredentialIdentity NULL_IDENTITY = createIdentity((Principal)null, (Object)null);
   private static CredentialIdentityFactory _instance = null;

   protected CredentialIdentityFactory() {
   }

   public static CredentialIdentityFactory getInstance() {
      if (_instance == null) {
         _instance = new CredentialIdentityFactory();
      }

      return _instance;
   }

   public static CredentialIdentity<Object> createIdentity(Principal principal, Object cred) {
      return principal == null && cred == null ? NULL_IDENTITY : createIdentity(principal, cred, (Role)null);
   }

   public static CredentialIdentity<Object> createIdentity(final Principal principal, final Object cred, final Role roles) {
      return new CredentialIdentity<Object>() {
         private static final long serialVersionUID = 1L;

         public Object getCredential() {
            return cred;
         }

         public void setCredential(Object credential) {
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
            builder.append("CredentialIdentity[principal=").append(principal);
            builder.append(";roles=").append(roles).append("]");
            return builder.toString();
         }
      };
   }
}
