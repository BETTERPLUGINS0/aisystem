package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.plugins.HostThreadLocal;
import java.security.acl.Group;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class RemoteHostTrustLoginModule extends UsernamePasswordLoginModule {
   private static final String OPTION_TRUSTED_HOSTS = "trustedHosts";
   private static final String OPTION_ROLES = "roles";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"trustedHosts", "roles"};
   List<String> trustedHosts;
   private String roleNames;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      String tmp = (String)options.get("trustedHosts");
      this.trustedHosts = Arrays.asList(this.parseHosts(tmp));
      this.roleNames = (String)options.get("roles");
   }

   private String[] parseHosts(String commaDel) {
      return commaDel.split("\\,");
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
      String host = this.getRealHost();
      PicketBoxLogger.LOGGER.debugRealHostForTrust(host);
      return this.trustedHosts.contains(host);
   }

   protected String getUsersPassword() throws LoginException {
      return "trustme";
   }

   protected String getRealHost() {
      return HostThreadLocal.get();
   }

   protected Group[] getRoleSets() throws LoginException {
      SimpleGroup roles = new SimpleGroup("Roles");
      Group[] roleSets = new Group[]{roles};
      if (this.roleNames != null) {
         String[] tokens = this.roleNames.split(",");
         String[] arr$ = tokens;
         int len$ = tokens.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String token = arr$[i$];
            String roleName = token != null ? token.trim() : token;
            roles.addMember(new SimplePrincipal(roleName));
         }
      }

      return roleSets;
   }
}
