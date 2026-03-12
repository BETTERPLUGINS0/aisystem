package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultException;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Set;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class CallerIdentityLoginModule extends AbstractPasswordCredentialLoginModule {
   private String userName;
   private char[] password;
   private boolean addRunAsRoles;
   private Set<Principal> runAsRoles;

   public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options) {
      super.initialize(subject, handler, sharedState, options);
      this.userName = (String)options.get("userName");
      String pass = (String)options.get("password");
      if (pass != null) {
         if (SecurityVaultUtil.isVaultFormat(pass)) {
            try {
               pass = SecurityVaultUtil.getValueAsString(pass);
            } catch (SecurityVaultException var7) {
               throw new RuntimeException(var7);
            }

            this.password = pass.toCharArray();
         } else {
            this.password = pass.toCharArray();
         }
      }

      String flag = (String)options.get("addRunAsRoles");
      this.addRunAsRoles = Boolean.valueOf(flag);
      PicketBoxLogger.LOGGER.debugModuleOption("userName", this.userName);
      PicketBoxLogger.LOGGER.debugModuleOption("password", this.password != null ? "****" : null);
      PicketBoxLogger.LOGGER.debugModuleOption("addRunAsRoles", this.addRunAsRoles);
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      String username = this.userName;

      try {
         Principal user = GetPrincipalInfoAction.getPrincipal();
         char[] userPassword = GetPrincipalInfoAction.getCredential();
         if (userPassword != null) {
            this.password = userPassword;
            if (SecurityVaultUtil.isVaultFormat(this.password)) {
               this.password = SecurityVaultUtil.getValue(this.password);
            }
         }

         if (user != null) {
            username = user.getName();
            PicketBoxLogger.LOGGER.traceCurrentCallingPrincipal(username, Thread.currentThread().getName());
            RunAsIdentity runAs = GetPrincipalInfoAction.peekRunAsIdentity();
            if (runAs != null) {
               this.runAsRoles = runAs.getRunAsRoles();
            }
         }
      } catch (Throwable var5) {
         throw PicketBoxMessages.MESSAGES.unableToGetPrincipalOrCredsForAssociation();
      }

      this.userName = username;
      if (super.login()) {
         return true;
      } else {
         this.sharedState.put("javax.security.auth.login.name", username);
         super.loginOk = true;
         return true;
      }
   }

   public boolean commit() throws LoginException {
      this.sharedState.put("javax.security.auth.login.name", this.userName);
      if (this.addRunAsRoles && this.runAsRoles != null) {
         SubjectActions.addRoles(this.subject, this.runAsRoles);
      }

      PasswordCredential cred = new PasswordCredential(this.userName, this.password);
      SubjectActions.addCredentials(this.subject, cred);
      return super.commit();
   }

   protected Principal getIdentity() {
      PicketBoxLogger.LOGGER.traceBeginGetIdentity(this.userName);
      Principal principal = new SimplePrincipal(this.userName);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginGetRoleSets();
      return new Group[0];
   }
}
