package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.acl.Group;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

/** @deprecated */
@Deprecated
public class JaasSecurityDomainIdentityLoginModule extends AbstractPasswordCredentialLoginModule {
   private String username;
   private String password;
   private ObjectName jaasSecurityDomain;

   public void initialize(Subject subject, CallbackHandler handler, Map sharedState, Map options) {
      super.initialize(subject, handler, sharedState, options);
      this.username = (String)options.get("username");
      if (this.username == null) {
         this.username = (String)options.get("userName");
         if (this.username == null) {
            throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("username"));
         }
      }

      this.password = (String)options.get("password");
      if (this.password == null) {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("password"));
      } else {
         String name = (String)options.get("jaasSecurityDomain");
         if (name == null) {
            throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("jaasSecurityDomain"));
         } else {
            try {
               this.jaasSecurityDomain = new ObjectName(name);
            } catch (Exception var7) {
               throw new IllegalArgumentException(var7);
            }
         }
      }
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      if (super.login()) {
         return true;
      } else {
         super.loginOk = true;
         return true;
      }
   }

   public boolean commit() throws LoginException {
      Principal principal = new SimplePrincipal(this.username);
      SubjectActions.addPrincipals(this.subject, principal);
      this.sharedState.put("javax.security.auth.login.name", this.username);
      return true;
   }

   public boolean abort() {
      this.username = null;
      this.password = null;
      return true;
   }

   protected Principal getIdentity() {
      PicketBoxLogger.LOGGER.traceBeginGetIdentity(this.username);
      Principal principal = new SimplePrincipal(this.username);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException {
      Group[] empty = new Group[0];
      return empty;
   }

   private static class DecodeAction implements PrivilegedExceptionAction {
      String password;
      ObjectName jaasSecurityDomain;
      MBeanServer server;

      DecodeAction(String password, ObjectName jaasSecurityDomain, MBeanServer server) {
         this.password = password;
         this.jaasSecurityDomain = jaasSecurityDomain;
         this.server = server;
      }

      public Object run() throws Exception {
         Object[] args = new Object[]{this.password};
         String[] sig = new String[]{String.class.getName()};
         byte[] secret = (byte[])((byte[])this.server.invoke(this.jaasSecurityDomain, "decode64", args, sig));
         String secretPassword = new String(secret, "UTF-8");
         return secretPassword.toCharArray();
      }

      static char[] decode(String password, ObjectName jaasSecurityDomain, MBeanServer server) throws Exception {
         JaasSecurityDomainIdentityLoginModule.DecodeAction action = new JaasSecurityDomainIdentityLoginModule.DecodeAction(password, jaasSecurityDomain, server);

         try {
            char[] decode = (char[])((char[])AccessController.doPrivileged(action));
            return decode;
         } catch (PrivilegedActionException var5) {
            throw var5.getException();
         }
      }
   }
}
