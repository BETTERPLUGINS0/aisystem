package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;

class GetPrincipalInfoAction {
   private static char[] getPassword() {
      Object credential = SecurityContextAssociation.getCredential();
      char[] password = null;
      if (credential instanceof char[]) {
         password = (char[])((char[])credential);
      } else {
         String tmp;
         if (credential instanceof byte[]) {
            try {
               tmp = new String((byte[])((byte[])credential), "UTF-8");
               password = tmp.toCharArray();
            } catch (UnsupportedEncodingException var3) {
               throw new SecurityException(var3.getMessage());
            }
         } else if (credential != null) {
            tmp = credential.toString();
            password = tmp.toCharArray();
         }
      }

      return password;
   }

   static Principal getPrincipal() {
      Principal principal;
      if (System.getSecurityManager() == null) {
         principal = GetPrincipalInfoAction.PrincipalActions.NON_PRIVILEGED.getPrincipal();
      } else {
         principal = GetPrincipalInfoAction.PrincipalActions.PRIVILEGED.getPrincipal();
      }

      return principal;
   }

   static char[] getCredential() {
      char[] credential;
      if (System.getSecurityManager() == null) {
         credential = GetPrincipalInfoAction.PrincipalActions.NON_PRIVILEGED.getCredential();
      } else {
         credential = GetPrincipalInfoAction.PrincipalActions.PRIVILEGED.getCredential();
      }

      return credential;
   }

   static RunAsIdentity peekRunAsIdentity() {
      return System.getSecurityManager() == null ? (RunAsIdentity)GetPrincipalInfoAction.PrincipalActions.NON_PRIVILEGED.peek() : (RunAsIdentity)GetPrincipalInfoAction.PrincipalActions.PRIVILEGED.peek();
   }

   interface PrincipalActions {
      GetPrincipalInfoAction.PrincipalActions PRIVILEGED = new GetPrincipalInfoAction.PrincipalActions() {
         private final PrivilegedAction<RunAs> peekAction = new PrivilegedAction<RunAs>() {
            public RunAs run() {
               return SecurityContextAssociation.peekRunAsIdentity();
            }
         };
         private final PrivilegedAction<Principal> getPrincipalAction = new PrivilegedAction<Principal>() {
            public Principal run() {
               return SecurityContextAssociation.getPrincipal();
            }
         };
         private final PrivilegedAction<Object> getCredentialAction = new PrivilegedAction<Object>() {
            public Object run() {
               return GetPrincipalInfoAction.getPassword();
            }
         };

         public RunAsIdentity peek() {
            return (RunAsIdentity)AccessController.doPrivileged(this.peekAction);
         }

         public Principal getPrincipal() {
            return (Principal)AccessController.doPrivileged(this.getPrincipalAction);
         }

         public char[] getCredential() {
            return (char[])((char[])AccessController.doPrivileged(this.getCredentialAction));
         }
      };
      GetPrincipalInfoAction.PrincipalActions NON_PRIVILEGED = new GetPrincipalInfoAction.PrincipalActions() {
         public RunAs peek() {
            return SecurityContextAssociation.peekRunAsIdentity();
         }

         public Principal getPrincipal() {
            return SecurityContextAssociation.getPrincipal();
         }

         public char[] getCredential() {
            return GetPrincipalInfoAction.getPassword();
         }
      };

      Principal getPrincipal();

      char[] getCredential();

      RunAs peek();
   }
}
