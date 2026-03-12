package fr.xephi.authme.libs.org.picketbox.plugins.authorization;

import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationModule;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public class PicketBoxAuthorizationModule implements AuthorizationModule {
   private Set<String> rolesSet = new HashSet();
   private Subject subject = null;

   public boolean abort() throws AuthorizationException {
      return true;
   }

   public int authorize(Resource resource) {
      Set<Principal> principals = this.subject.getPrincipals();
      Iterator i$ = principals.iterator();

      while(true) {
         Group group;
         do {
            Principal p;
            do {
               if (!i$.hasNext()) {
                  return -1;
               }

               p = (Principal)i$.next();
            } while(!(p instanceof Group));

            group = (Group)p;
         } while(!group.getName().equalsIgnoreCase("Roles"));

         Enumeration roles = group.members();

         while(roles.hasMoreElements()) {
            Principal role = (Principal)roles.nextElement();
            if (this.rolesSet.contains(role.getName())) {
               return 1;
            }
         }
      }
   }

   public boolean commit() throws AuthorizationException {
      return true;
   }

   public boolean destroy() {
      return true;
   }

   public void initialize(Subject subject, CallbackHandler handler, Map<String, Object> sharedState, Map<String, Object> options, RoleGroup roles) {
      String configuredRoles = (String)options.get("roles");
      this.getRoles(configuredRoles);
      this.subject = subject;
   }

   private void getRoles(String commaStr) {
      StringTokenizer st = new StringTokenizer(commaStr, ",");

      while(st.hasMoreTokens()) {
         this.rolesSet.add(st.nextToken());
      }

   }
}
