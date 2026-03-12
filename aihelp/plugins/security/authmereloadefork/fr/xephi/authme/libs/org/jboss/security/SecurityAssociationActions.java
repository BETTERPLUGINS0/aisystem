package fr.xephi.authme.libs.org.jboss.security;

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;

class SecurityAssociationActions {
   static void clearSecurityContext(SecurityContext sc) {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            SecurityContextAssociation.clearSecurityContext();
            return null;
         }
      });
   }

   static void setSecurityContext(final SecurityContext sc) {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            SecurityContextAssociation.setSecurityContext(sc);
            return null;
         }
      });
   }

   static SecurityContext getSecurityContext() {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedAction<SecurityContext>() {
         public SecurityContext run() {
            return SecurityContextAssociation.getSecurityContext();
         }
      });
   }

   static void pushSecurityContext(final Principal p, final Object cred, final Subject subject, final String securityDomain) {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            SecurityContext sc;
            try {
               sc = SecurityContextFactory.createSecurityContext(p, cred, subject, securityDomain);
            } catch (Exception var3) {
               throw new RuntimeException(var3);
            }

            SecurityAssociationActions.setSecurityContext(sc);
            return null;
         }
      });
   }

   static void popSecurityContext() {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            SecurityContext sc = SecurityAssociationActions.getSecurityContext();
            if (sc != null) {
               sc.getUtil().createSubjectInfo((Principal)null, (Object)null, (Subject)null);
            }

            return null;
         }
      });
   }

   static void setPrincipalInfo(Principal principal, Object credential, Subject subject) {
      SecurityAssociationActions.SetPrincipalInfoAction action = new SecurityAssociationActions.SetPrincipalInfoAction(principal, credential, subject);
      AccessController.doPrivileged(action);
   }

   static void popPrincipalInfo() {
      SecurityAssociationActions.PopPrincipalInfoAction action = new SecurityAssociationActions.PopPrincipalInfoAction();
      AccessController.doPrivileged(action);
   }

   static Boolean getServer() {
      return (Boolean)AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
         public Boolean run() {
            return !SecurityContextAssociation.isClient();
         }
      });
   }

   static void setClient() {
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
         public Object run() {
            SecurityContextAssociation.setClient();
            return null;
         }
      });
   }

   static void clear() {
      AccessController.doPrivileged(SecurityAssociationActions.ClearAction.ACTION);
   }

   static Subject getSubject() {
      Subject subject = (Subject)AccessController.doPrivileged(SecurityAssociationActions.GetSubjectAction.ACTION);
      return subject;
   }

   static Principal getPrincipal() {
      Principal principal = (Principal)AccessController.doPrivileged(SecurityAssociationActions.GetPrincipalAction.ACTION);
      return principal;
   }

   static Object getCredential() {
      Object credential = AccessController.doPrivileged(SecurityAssociationActions.GetCredentialAction.ACTION);
      return credential;
   }

   static SecurityContext createSecurityContext(final String securityDomain) throws PrivilegedActionException {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedExceptionAction<SecurityContext>() {
         public SecurityContext run() throws Exception {
            return SecurityContextFactory.createSecurityContext(securityDomain);
         }
      });
   }

   private static class GetCredentialAction implements PrivilegedAction<Object> {
      static PrivilegedAction<Object> ACTION = new SecurityAssociationActions.GetCredentialAction();

      public Object run() {
         Object credential = SecurityContextAssociation.getCredential();
         return credential;
      }
   }

   private static class GetPrincipalAction implements PrivilegedAction<Principal> {
      static PrivilegedAction<Principal> ACTION = new SecurityAssociationActions.GetPrincipalAction();

      public Principal run() {
         Principal principal = SecurityContextAssociation.getPrincipal();
         return principal;
      }
   }

   private static class GetSubjectAction implements PrivilegedAction<Subject> {
      static PrivilegedAction<Subject> ACTION = new SecurityAssociationActions.GetSubjectAction();

      public Subject run() {
         Subject subject = SecurityContextAssociation.getSubject();
         return subject;
      }
   }

   private static class ClearAction implements PrivilegedAction<Object> {
      static PrivilegedAction<Object> ACTION = new SecurityAssociationActions.ClearAction();

      public Object run() {
         if (!SecurityAssociationActions.getServer()) {
            SecurityContextAssociation.clearSecurityContext();
         }

         return null;
      }
   }

   private static class PopPrincipalInfoAction implements PrivilegedAction<Object> {
      private PopPrincipalInfoAction() {
      }

      public Object run() {
         if (!SecurityAssociationActions.getServer()) {
            SecurityAssociationActions.popSecurityContext();
         }

         return null;
      }

      // $FF: synthetic method
      PopPrincipalInfoAction(Object x0) {
         this();
      }
   }

   private static class SetPrincipalInfoAction implements PrivilegedAction<Object> {
      Principal principal;
      Object credential;
      Subject subject;

      SetPrincipalInfoAction(Principal principal, Object credential, Subject subject) {
         this.principal = principal;
         this.credential = credential;
         this.subject = subject;
      }

      public Object run() {
         SecurityContext sc = null;

         try {
            sc = SecurityContextFactory.createSecurityContext(this.principal, this.credential, this.subject, "CLIENT_LOGIN_MODULE");
         } catch (Exception var3) {
            throw new RuntimeException(var3);
         }

         SecurityAssociationActions.setSecurityContext(sc);
         this.credential = null;
         this.principal = null;
         this.subject = null;
         return null;
      }
   }
}
