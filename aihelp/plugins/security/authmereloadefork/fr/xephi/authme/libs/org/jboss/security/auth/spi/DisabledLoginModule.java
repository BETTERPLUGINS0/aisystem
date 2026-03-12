package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import org.jboss.logging.Logger;

public class DisabledLoginModule implements LoginModule {
   private static final String[] ALL_VALID_OPTIONS = new String[]{"jboss.security.security_domain"};
   private static Logger log = Logger.getLogger(DisabledLoginModule.class);
   protected String securityDomain;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      HashSet<String> validOptions = new HashSet(Arrays.asList(ALL_VALID_OPTIONS));
      Iterator i$ = options.keySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         if (!validOptions.contains(key)) {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }

      this.securityDomain = (String)options.get("jboss.security.security_domain");
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.errorUsingDisabledDomain(this.securityDomain != null ? this.securityDomain : "");
      return false;
   }

   public boolean commit() throws LoginException {
      return false;
   }

   public boolean abort() throws LoginException {
      return false;
   }

   public boolean logout() throws LoginException {
      return false;
   }
}
