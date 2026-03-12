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

public class ClientLoginModule implements LoginModule {
   private static final String MULTI_TREADED = "multi-threaded";
   private static final String RESTORE_LOGIN_IDENTITY = "restore-login-identity";
   private static final String PASSWORD_STACKING = "password-stacking";
   private static final String PRINCIPAL_CLASS = "principalClass";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"multi-threaded", "restore-login-identity", "password-stacking", "principalClass", "jboss.security.security_domain"};
   private Subject subject;
   private CallbackHandler callbackHandler;
   private Principal loginPrincipal;
   private Object loginCredential;
   private Map<String, ?> sharedState;
   private boolean useFirstPass;
   private boolean restoreLoginIdentity;
   private SecurityContext cachedSecurityContext;

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
      String flag = (String)options.get("multi-threaded");
      if (Boolean.valueOf(flag)) {
         PicketBoxLogger.LOGGER.debugModuleOption("multi-threaded", flag);
      }

      if (flag != null && flag.length() > 0 && "false".equalsIgnoreCase(flag)) {
         SecurityAssociationActions.setClient();
      }

      flag = (String)options.get("restore-login-identity");
      this.restoreLoginIdentity = Boolean.valueOf(flag);
      PicketBoxLogger.LOGGER.debugModuleOption("restore-login-identity", flag);
      String passwordStacking = (String)options.get("password-stacking");
      this.useFirstPass = passwordStacking != null;
      PicketBoxLogger.LOGGER.debugModuleOption("password-stacking", passwordStacking);
      this.cachedSecurityContext = SecurityAssociationActions.getSecurityContext();
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      if (this.useFirstPass) {
         try {
            Object name = this.sharedState.get("javax.security.auth.login.name");
            if (!(name instanceof Principal)) {
               String username = name != null ? name.toString() : "";
               this.loginPrincipal = new SimplePrincipal(username);
            } else {
               this.loginPrincipal = (Principal)name;
            }

            this.loginCredential = this.sharedState.get("javax.security.auth.login.password");
            return true;
         } catch (Exception var9) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var9);
         }
      }

      if (this.callbackHandler == null) {
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();
      } else {
         PasswordCallback pc = new PasswordCallback(PicketBoxMessages.MESSAGES.enterPasswordMessage(), false);
         NameCallback nc = new NameCallback(PicketBoxMessages.MESSAGES.enterUsernameMessage(), "guest");
         Callback[] callbacks = new Callback[]{nc, pc};

         LoginException ex;
         try {
            char[] password = null;
            this.callbackHandler.handle(callbacks);
            String username = nc.getName();
            this.loginPrincipal = new SimplePrincipal(username);
            char[] tmpPassword = pc.getPassword();
            if (tmpPassword != null) {
               password = new char[tmpPassword.length];
               System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
               pc.clearPassword();
            }

            this.loginCredential = password;
            PicketBoxLogger.LOGGER.traceObtainedAuthInfoFromHandler(this.loginPrincipal, this.loginCredential != null ? this.loginCredential.getClass() : null);
         } catch (IOException var7) {
            ex = new LoginException(var7.getLocalizedMessage());
            ex.initCause(var7);
            throw ex;
         } catch (UnsupportedCallbackException var8) {
            ex = new LoginException(var8.getLocalizedMessage());
            ex.initCause(var8);
            throw ex;
         }

         PicketBoxLogger.LOGGER.traceEndLogin(true);
         return true;
      }
   }

   public boolean commit() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginCommit(true);
      SecurityAssociationActions.setPrincipalInfo(this.loginPrincipal, this.loginCredential, this.subject);
      Set<Principal> principals = this.subject.getPrincipals();
      if (!principals.contains(this.loginPrincipal)) {
         principals.add(this.loginPrincipal);
      }

      return true;
   }

   public boolean abort() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginAbort();
      if (this.restoreLoginIdentity) {
         SecurityAssociationActions.setSecurityContext(this.cachedSecurityContext);
      } else {
         SecurityAssociationActions.clear();
      }

      return true;
   }

   public boolean logout() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogout();
      if (this.restoreLoginIdentity) {
         SecurityAssociationActions.setSecurityContext(this.cachedSecurityContext);
      } else {
         SecurityAssociationActions.clear();
      }

      Set<Principal> principals = this.subject.getPrincipals();
      principals.remove(this.loginPrincipal);
      return true;
   }
}
