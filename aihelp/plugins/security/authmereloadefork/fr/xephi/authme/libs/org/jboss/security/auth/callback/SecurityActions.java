package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.callback.CallbackHandler;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;

class SecurityActions {
   static CallbackHandler getContextCallbackHandler() throws Exception {
      return System.getSecurityManager() == null ? SecurityActions.PolicyContextActions.NON_PRIVILEGED.getContextCallbackHandler() : SecurityActions.PolicyContextActions.PRIVILEGED.getContextCallbackHandler();
   }

   static SecurityContext getCurrentSecurityContext() {
      return (SecurityContext)AccessController.doPrivileged(new PrivilegedAction<SecurityContext>() {
         public SecurityContext run() {
            return SecurityContextAssociation.getSecurityContext();
         }
      });
   }

   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static Void setContextClassLoader(final ClassLoader cl) {
      return (Void)AccessController.doPrivileged(new PrivilegedAction<Void>() {
         public Void run() {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
         }
      });
   }

   interface PolicyContextActions {
      SecurityActions.PolicyContextActions PRIVILEGED = new SecurityActions.PolicyContextActions() {
         private final PrivilegedExceptionAction<CallbackHandler> exAction = new PrivilegedExceptionAction<CallbackHandler>() {
            public CallbackHandler run() throws Exception {
               return (CallbackHandler)PolicyContext.getContext("fr.xephi.authme.libs.org.jboss.security.auth.spi.CallbackHandler");
            }
         };

         public CallbackHandler getContextCallbackHandler() throws PolicyContextException {
            try {
               return (CallbackHandler)AccessController.doPrivileged(this.exAction);
            } catch (PrivilegedActionException var3) {
               Exception ex = var3.getException();
               if (ex instanceof PolicyContextException) {
                  throw (PolicyContextException)ex;
               } else {
                  throw new UndeclaredThrowableException(ex);
               }
            }
         }
      };
      SecurityActions.PolicyContextActions NON_PRIVILEGED = new SecurityActions.PolicyContextActions() {
         public CallbackHandler getContextCallbackHandler() throws PolicyContextException {
            return (CallbackHandler)PolicyContext.getContext("fr.xephi.authme.libs.org.jboss.security.auth.spi.CallbackHandler");
         }
      };

      CallbackHandler getContextCallbackHandler() throws PolicyContextException;
   }
}
