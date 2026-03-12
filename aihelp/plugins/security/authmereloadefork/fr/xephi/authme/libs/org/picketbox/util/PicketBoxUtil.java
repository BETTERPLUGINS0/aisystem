package fr.xephi.authme.libs.org.picketbox.util;

import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;

public class PicketBoxUtil {
   public static RoleGroup getRolesFromSubject(Subject subject) {
      Set<Group> groupPrincipals = subject.getPrincipals(Group.class);
      if (groupPrincipals != null) {
         Iterator i$ = groupPrincipals.iterator();

         while(i$.hasNext()) {
            Group groupPrincipal = (Group)i$.next();
            if ("Roles".equals(groupPrincipal.getName())) {
               return new SimpleRoleGroup(groupPrincipal);
            }
         }
      }

      return null;
   }

   public static Principal getPrincipalFromSubject(Subject subject) {
      Set<Principal> principals = subject.getPrincipals();
      if (principals != null) {
         Iterator i$ = principals.iterator();

         while(i$.hasNext()) {
            Principal p = (Principal)i$.next();
            if (p instanceof Group) {
               return p;
            }
         }
      }

      return null;
   }
}
