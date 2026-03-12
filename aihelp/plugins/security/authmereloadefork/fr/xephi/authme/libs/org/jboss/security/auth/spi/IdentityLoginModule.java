package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class IdentityLoginModule extends AbstractServerLoginModule {
   private static final String PRINCIPAL = "principal";
   private static final String ROLES = "roles";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"principal", "roles"};
   private String principalName;
   private String roleNames;

   public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, handler, sharedState, options);
      this.principalName = (String)options.get("principal");
      if (this.principalName == null) {
         this.principalName = "guest";
      }

      this.roleNames = (String)options.get("roles");
   }

   public boolean login() throws LoginException {
      if (super.login()) {
         return true;
      } else {
         Principal principal = new SimplePrincipal(this.principalName);
         this.subject.getPrincipals().add(principal);
         this.sharedState.put("javax.security.auth.login.name", this.principalName);
         super.loginOk = true;
         return true;
      }
   }

   protected Principal getIdentity() {
      Principal principal = new SimplePrincipal(this.principalName);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException {
      SimpleGroup roles = new SimpleGroup("Roles");
      Group[] roleSets = new Group[]{roles};
      if (this.roleNames != null) {
         StringTokenizer tokenizer = new StringTokenizer(this.roleNames, ",");

         while(tokenizer.hasMoreTokens()) {
            String roleName = tokenizer.nextToken();
            roles.addMember(new SimplePrincipal(roleName));
         }
      }

      return roleSets;
   }
}
