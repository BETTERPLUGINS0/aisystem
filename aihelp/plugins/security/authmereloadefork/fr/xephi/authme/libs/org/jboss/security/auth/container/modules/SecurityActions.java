package fr.xephi.authme.libs.org.jboss.security.auth.container.modules;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

class SecurityActions {
   static ClassLoader getContextClassLoader() {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
         public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   static LoginContext createLoginContext(final String configName, final Subject subject, final CallbackHandler cbh) throws PrivilegedActionException {
      return (LoginContext)AccessController.doPrivileged(new PrivilegedExceptionAction<LoginContext>() {
         public LoginContext run() throws LoginException {
            return new LoginContext(configName, subject, cbh);
         }
      });
   }
}
