package fr.xephi.authme.libs.org.jboss.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class AltClientLoginModule implements LoginModule {
   private static final String MULTI_TREADED = "multi-threaded";
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String PRINCIPAL_CLASS = "principalClass";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"multi-threaded", "password-stacking", "principalClass", "jboss.security.security_domain"};
   private Subject subject;
   private CallbackHandler callbackHandler;
   private Map<String, ?> sharedState;
   private boolean useFirstPass;
   private String username;
   private char[] password = null;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      HashSet<String> validOptions = new HashSet(Arrays.asList(ALL_VALID_OPTIONS));
      Iterator i$ = options.keySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         if (!validOptions.contains((String)key)) {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }

      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.sharedState = sharedState;
      PicketBoxLogger.LOGGER.debugModuleOption("jboss.security.security_domain", options.get("jboss.security.security_domain"));
      String mt = (String)options.get("multi-threaded");
      if (Boolean.valueOf(mt)) {
         PicketBoxLogger.LOGGER.debugModuleOption("multi-threaded", mt);
      }

      String passwordStacking = (String)options.get("password-stacking");
      this.useFirstPass = passwordStacking != null;
      PicketBoxLogger.LOGGER.debugModuleOption("password-stacking", passwordStacking);
   }

   public boolean login() throws LoginException {
      if (this.useFirstPass) {
         return true;
      } else if (this.callbackHandler == null) {
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();
      } else {
         PasswordCallback pc = new PasswordCallback(PicketBoxMessages.MESSAGES.enterPasswordMessage(), false);
         NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
         Callback[] callbacks = new Callback[]{nc, pc};

         try {
            this.callbackHandler.handle(callbacks);
            this.username = nc.getName();
            char[] tmpPassword = pc.getPassword();
            if (tmpPassword != null) {
               this.password = new char[tmpPassword.length];
               System.arraycopy(tmpPassword, 0, this.password, 0, tmpPassword.length);
               pc.clearPassword();
            }

            return true;
         } catch (IOException var6) {
            throw new LoginException(var6.toString());
         } catch (UnsupportedCallbackException var7) {
            LoginException le = new LoginException(var7.getLocalizedMessage());
            le.initCause(var7);
            throw le;
         }
      }
   }

   public boolean commit() throws LoginException {
      Set<Principal> principals = this.subject.getPrincipals();
      Principal p = null;
      Object credential = this.password;
      if (this.useFirstPass) {
         Object user = this.sharedState.get("javax.security.auth.login.name");
         if (!(user instanceof Principal)) {
            this.username = user != null ? user.toString() : "";
            p = new SimplePrincipal(this.username);
         } else {
            p = (Principal)user;
         }

         credential = this.sharedState.get("javax.security.auth.login.password");
      } else {
         p = new SimplePrincipal(this.username);
      }

      if (!principals.isEmpty()) {
         p = (Principal)principals.iterator().next();
      }

      SecurityAssociationActions.setPrincipalInfo((Principal)p, credential, this.subject);
      return true;
   }

   public boolean abort() throws LoginException {
      int length = this.password != null ? this.password.length : 0;

      for(int n = 0; n < length; ++n) {
         this.password[n] = 0;
      }

      SecurityAssociationActions.clear();
      return true;
   }

   public boolean logout() throws LoginException {
      SecurityAssociationActions.clear();
      return true;
   }
}
