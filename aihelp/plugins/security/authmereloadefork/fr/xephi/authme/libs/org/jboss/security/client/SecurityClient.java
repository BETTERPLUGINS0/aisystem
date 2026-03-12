package fr.xephi.authme.libs.org.jboss.security.client;

import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public abstract class SecurityClient {
   protected Object userPrincipal = null;
   protected Object credential = null;
   protected CallbackHandler callbackHandler = null;
   protected String loginConfigName = null;
   protected String saslMechanism = null;
   protected String saslAuthorizationId = null;
   protected boolean jaasDesired = false;
   protected boolean saslDesired = false;
   protected boolean vmwideAssociation = false;

   public void login() throws LoginException {
      if (this.jaasDesired) {
         this.performJAASLogin();
      } else if (this.saslDesired) {
         this.peformSASLLogin();
      } else {
         this.performSimpleLogin();
      }

   }

   public void logout() {
      this.setSimple((Object)null, (Object)null);
      this.setJAAS((String)null, (CallbackHandler)null);
      this.setSASL((String)null, (String)null, (CallbackHandler)null);
      this.clearUpDesires();
      this.cleanUp();
   }

   public void setSimple(Object username, Object credential) {
      this.userPrincipal = username;
      this.credential = credential;
   }

   public void setJAAS(String configName, CallbackHandler cbh) {
      this.loginConfigName = configName;
      this.callbackHandler = cbh;
      this.clearUpDesires();
      this.jaasDesired = true;
   }

   public void setSASL(String mechanism, String authorizationId, CallbackHandler cbh) {
      this.saslMechanism = mechanism;
      this.saslAuthorizationId = authorizationId;
      this.callbackHandler = cbh;
      this.clearUpDesires();
      this.saslDesired = true;
   }

   protected abstract void performJAASLogin() throws LoginException;

   protected abstract void peformSASLLogin();

   protected abstract void performSimpleLogin();

   public boolean isVmwideAssociation() {
      return this.vmwideAssociation;
   }

   public void setVmwideAssociation(boolean vmwideAssociation) {
      this.vmwideAssociation = vmwideAssociation;
      if (vmwideAssociation) {
         SecurityContextAssociation.setClient();
      }

   }

   protected abstract void cleanUp();

   private void clearUpDesires() {
      this.jaasDesired = false;
      this.saslDesired = false;
   }
}
