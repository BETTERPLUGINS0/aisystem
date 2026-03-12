package fr.xephi.authme.libs.waffle.jaas;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAccount;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.PrincipalFormat;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsAuthProviderImpl;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class WindowsLoginModule implements LoginModule {
   private static final Logger LOGGER = LoggerFactory.getLogger(WindowsLoginModule.class);
   private String username;
   private boolean debug;
   private Subject subject;
   private CallbackHandler callbackHandler;
   private IWindowsAuthProvider auth = new WindowsAuthProviderImpl();
   private Set<Principal> principals;
   private PrincipalFormat principalFormat;
   private PrincipalFormat roleFormat;
   private boolean allowGuestLogin;

   public WindowsLoginModule() {
      this.principalFormat = PrincipalFormat.FQN;
      this.roleFormat = PrincipalFormat.FQN;
      this.allowGuestLogin = true;
   }

   public void initialize(Subject initSubject, CallbackHandler initCallbackHandler, Map<String, ?> initSharedState, Map<String, ?> initOptions) {
      this.subject = initSubject;
      this.callbackHandler = initCallbackHandler;
      Iterator var5 = initOptions.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<String, ?> option = (Entry)var5.next();
         if ("debug".equalsIgnoreCase((String)option.getKey())) {
            this.debug = Boolean.parseBoolean((String)option.getValue());
         } else if ("principalFormat".equalsIgnoreCase((String)option.getKey())) {
            this.principalFormat = PrincipalFormat.valueOf(((String)option.getValue()).toUpperCase(Locale.ENGLISH));
         } else if ("roleFormat".equalsIgnoreCase((String)option.getKey())) {
            this.roleFormat = PrincipalFormat.valueOf(((String)option.getValue()).toUpperCase(Locale.ENGLISH));
         }
      }

   }

   public boolean login() throws LoginException {
      if (this.callbackHandler == null) {
         throw new LoginException("Missing callback to gather information from the user.");
      } else {
         NameCallback usernameCallback = new NameCallback("user name: ");
         PasswordCallback passwordCallback = new PasswordCallback("password: ", false);
         Callback[] callbacks = new Callback[]{usernameCallback, passwordCallback};

         String userName;
         String password;
         try {
            this.callbackHandler.handle(callbacks);
            userName = usernameCallback.getName();
            password = passwordCallback.getPassword() == null ? "" : new String(passwordCallback.getPassword());
            passwordCallback.clearPassword();
         } catch (IOException var17) {
            LOGGER.trace((String)"", (Throwable)var17);
            throw new LoginException(var17.toString());
         } catch (UnsupportedCallbackException var18) {
            LOGGER.trace((String)"", (Throwable)var18);
            throw new LoginException("Callback {} not available to gather authentication information from the user.".replace("{}", var18.getCallback().getClass().getName()));
         }

         IWindowsIdentity windowsIdentity;
         try {
            windowsIdentity = this.auth.logonUser(userName, password);
         } catch (Exception var16) {
            LOGGER.trace((String)"", (Throwable)var16);
            throw new LoginException(var16.getMessage());
         }

         try {
            if (!this.allowGuestLogin && windowsIdentity.isGuest()) {
               LOGGER.debug((String)"guest login disabled: {}", (Object)windowsIdentity.getFqn());
               throw new LoginException("Guest login disabled");
            }

            this.principals = new LinkedHashSet();
            this.principals.addAll(getUserPrincipals(windowsIdentity, this.principalFormat));
            if (this.roleFormat != PrincipalFormat.NONE) {
               IWindowsAccount[] var7 = windowsIdentity.getGroups();
               int var8 = var7.length;

               for(int var9 = 0; var9 < var8; ++var9) {
                  IWindowsAccount group = var7[var9];
                  this.principals.addAll(getRolePrincipals(group, this.roleFormat));
               }
            }

            this.username = windowsIdentity.getFqn();
            LOGGER.debug((String)"successfully logged in {} ({})", (Object)this.username, (Object)windowsIdentity.getSidString());
         } finally {
            windowsIdentity.dispose();
         }

         return true;
      }
   }

   public boolean abort() throws LoginException {
      return this.logout();
   }

   public boolean commit() throws LoginException {
      if (this.principals == null) {
         return false;
      } else if (this.subject.isReadOnly()) {
         throw new LoginException("Subject cannot be read-only.");
      } else {
         Set<Principal> principalsSet = this.subject.getPrincipals();
         principalsSet.addAll(this.principals);
         LOGGER.debug((String)"committing {} principals", (Object)this.subject.getPrincipals().size());
         if (this.debug) {
            Iterator var2 = principalsSet.iterator();

            while(var2.hasNext()) {
               Principal principal = (Principal)var2.next();
               LOGGER.debug((String)" principal: {}", (Object)principal.getName());
            }
         }

         return true;
      }
   }

   public boolean logout() throws LoginException {
      if (this.subject.isReadOnly()) {
         throw new LoginException("Subject cannot be read-only.");
      } else {
         this.subject.getPrincipals().clear();
         if (this.username != null) {
            LOGGER.debug((String)"logging out {}", (Object)this.username);
         }

         return true;
      }
   }

   public boolean isDebug() {
      return this.debug;
   }

   public IWindowsAuthProvider getAuth() {
      return this.auth;
   }

   public void setAuth(IWindowsAuthProvider provider) {
      this.auth = provider;
   }

   private static List<Principal> getUserPrincipals(IWindowsIdentity windowsIdentity, PrincipalFormat principalFormat) {
      List<Principal> principalsList = new ArrayList();
      switch(principalFormat) {
      case FQN:
         principalsList.add(new UserPrincipal(windowsIdentity.getFqn()));
         break;
      case SID:
         principalsList.add(new UserPrincipal(windowsIdentity.getSidString()));
         break;
      case BOTH:
         principalsList.add(new UserPrincipal(windowsIdentity.getFqn()));
         principalsList.add(new UserPrincipal(windowsIdentity.getSidString()));
      case NONE:
      }

      return principalsList;
   }

   private static List<Principal> getRolePrincipals(IWindowsAccount group, PrincipalFormat principalFormat) {
      List<Principal> principalsList = new ArrayList();
      switch(principalFormat) {
      case FQN:
         principalsList.add(new RolePrincipal(group.getFqn()));
         break;
      case SID:
         principalsList.add(new RolePrincipal(group.getSidString()));
         break;
      case BOTH:
         principalsList.add(new RolePrincipal(group.getFqn()));
         principalsList.add(new RolePrincipal(group.getSidString()));
      case NONE:
      }

      return principalsList;
   }

   public boolean isAllowGuestLogin() {
      return this.allowGuestLogin;
   }

   public void setAllowGuestLogin(boolean value) {
      this.allowGuestLogin = value;
   }
}
