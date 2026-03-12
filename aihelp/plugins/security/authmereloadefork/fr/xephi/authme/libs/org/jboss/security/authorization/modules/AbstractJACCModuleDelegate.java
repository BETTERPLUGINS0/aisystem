package fr.xephi.authme.libs.org.jboss.security.authorization.modules;

import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.Subject;

public abstract class AbstractJACCModuleDelegate extends AuthorizationModuleDelegate {
   public abstract int authorize(Resource var1, Subject var2, RoleGroup var3);

   protected Principal[] getPrincipals(Subject subject, Role role) {
      Set<Principal> principalsSet = null;
      if (role != null) {
         principalsSet = this.getPrincipalSetFromRole(role);
      }

      Principal[] arr = null;
      if (principalsSet != null) {
         arr = new Principal[principalsSet.size()];
         principalsSet.toArray(arr);
      }

      return arr;
   }

   private Set<Principal> getPrincipalSetFromRole(Role role) {
      Set<Principal> principalsSet = new HashSet();
      if (role instanceof RoleGroup) {
         RoleGroup rg = (RoleGroup)role;
         List<Role> rolesList = rg.getRoles();
         Iterator i$ = rolesList.iterator();

         while(i$.hasNext()) {
            Role r = (Role)i$.next();
            principalsSet.add(new SimplePrincipal(r.getRoleName()));
         }
      } else {
         principalsSet.add(new SimplePrincipal(role.getRoleName()));
      }

      return principalsSet;
   }
}
