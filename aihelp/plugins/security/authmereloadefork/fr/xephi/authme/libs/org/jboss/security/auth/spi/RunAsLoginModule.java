package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.spi.LoginModule;

public class RunAsLoginModule implements LoginModule {
   private static final String ROLE_NAME = "roleName";
   private static final String PRINCIPLE_NAME = "principalName";
   private static final String PRINCIPAL_CLASS = "principalClass";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"roleName", "principalName", "principalClass", "jboss.security.security_domain"};
   private String roleName;
   private String principalName;
   private boolean pushedRole;

   public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options) {
      HashSet<String> validOptions = new HashSet(Arrays.asList(ALL_VALID_OPTIONS));
      Iterator i$ = options.keySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         if (!validOptions.contains(key)) {
            PicketBoxLogger.LOGGER.warnInvalidModuleOption((String)key);
         }
      }

      this.roleName = (String)options.get("roleName");
      if (this.roleName == null) {
         this.roleName = "nobody";
      }

      this.principalName = (String)options.get("principalName");
      if (this.principalName == null) {
         this.principalName = "nobody";
      }

   }

   public boolean login() {
      RunAsIdentity runAsRole = new RunAsIdentity(this.roleName, this.principalName);
      SecurityContextAssociation.pushRunAsIdentity(runAsRole);
      this.pushedRole = true;
      return true;
   }

   public boolean commit() {
      return this.abort();
   }

   public boolean abort() {
      if (!this.pushedRole) {
         return false;
      } else {
         SecurityContextAssociation.popRunAsIdentity();
         return true;
      }
   }

   public boolean logout() {
      return true;
   }
}
