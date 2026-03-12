package fr.xephi.authme.libs.org.picketbox.datasource.security;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultException;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class ConfiguredIdentityLoginModule extends AbstractPasswordCredentialLoginModule {
   private String principalName;
   private String userName;
   private String password;

   public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options) {
      super.initialize(subject, handler, sharedState, options);
      this.principalName = (String)options.get("principal");
      if (this.principalName == null) {
         throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("principal"));
      } else {
         this.userName = (String)options.get("userName");
         if (this.userName == null) {
            this.userName = (String)options.get("username");
            if (this.userName == null) {
               throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("username"));
            }
         }

         this.password = (String)options.get("password");
         if (this.password == null) {
            PicketBoxLogger.LOGGER.warnModuleCreationWithEmptyPassword();
            this.password = "";
         } else if (SecurityVaultUtil.isVaultFormat(this.password)) {
            try {
               this.password = SecurityVaultUtil.getValueAsString(this.password);
            } catch (SecurityVaultException var6) {
               throw new RuntimeException(var6);
            }
         }

         PicketBoxLogger.LOGGER.debugModuleOption("principal", this.principalName);
         PicketBoxLogger.LOGGER.debugModuleOption("username", this.userName);
         PicketBoxLogger.LOGGER.debugModuleOption("password", this.password);
      }
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      if (super.login()) {
         return true;
      } else {
         Principal principal = new SimplePrincipal(this.principalName);
         SubjectActions.addPrincipals(this.subject, principal);
         this.sharedState.put("javax.security.auth.login.name", this.principalName);
         PasswordCredential cred = new PasswordCredential(this.userName, this.password.toCharArray());
         SubjectActions.addCredentials(this.subject, cred);
         super.loginOk = true;
         return true;
      }
   }

   protected Principal getIdentity() {
      PicketBoxLogger.LOGGER.traceBeginGetIdentity(this.principalName);
      Principal principal = new SimplePrincipal(this.principalName);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginGetRoleSets();
      return new Group[0];
   }
}
