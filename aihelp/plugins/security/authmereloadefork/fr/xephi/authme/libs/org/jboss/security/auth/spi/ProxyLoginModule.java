package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import org.jboss.logging.Logger;

public class ProxyLoginModule implements LoginModule {
   private static final String MODULE_NAME = "moduleName";
   private static final String PRINCIPAL_CLASS = "principalClass";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"moduleName", "principalClass", "jboss.security.security_domain"};
   protected Logger log;
   private String moduleName;
   private LoginModule delegate;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.log = Logger.getLogger(this.getClass());
      HashSet<String> validOptions = new HashSet(Arrays.asList(ALL_VALID_OPTIONS));
      Iterator i$ = options.keySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         if (!validOptions.contains(key)) {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }

      this.moduleName = (String)options.get("moduleName");
      if (this.moduleName != null) {
         ClassLoader loader = SecurityActions.getContextClassLoader();

         try {
            Class<?> clazz = loader.loadClass(this.moduleName);
            this.delegate = (LoginModule)clazz.newInstance();
         } catch (Throwable var8) {
            var8.printStackTrace();
            return;
         }

         this.delegate.initialize(subject, callbackHandler, sharedState, options);
      }
   }

   public boolean login() throws LoginException {
      if (this.moduleName == null) {
         throw new LoginException(PicketBoxMessages.MESSAGES.missingRequiredModuleOptionMessage("moduleName"));
      } else if (this.delegate == null) {
         throw PicketBoxMessages.MESSAGES.failedToInstantiateDelegateModule(this.moduleName);
      } else {
         return this.delegate.login();
      }
   }

   public boolean commit() throws LoginException {
      boolean ok = false;
      if (this.delegate != null) {
         ok = this.delegate.commit();
      }

      return ok;
   }

   public boolean abort() throws LoginException {
      boolean ok = true;
      if (this.delegate != null) {
         ok = this.delegate.abort();
      }

      return ok;
   }

   public boolean logout() throws LoginException {
      boolean ok = true;
      if (this.delegate != null) {
         ok = this.delegate.logout();
      }

      return ok;
   }
}
