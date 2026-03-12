package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class AbstractRolesMappingProvider implements MappingProvider<RoleGroup> {
   protected MappingResult<RoleGroup> result;

   public boolean supports(Class<?> p) {
      return RoleGroup.class.isAssignableFrom(p);
   }

   public void setMappingResult(MappingResult<RoleGroup> result) {
      this.result = result;
   }

   protected Principal getCallerPrincipal(Map<String, Object> map) {
      Principal principal = (Principal)map.get("Principal");
      Principal callerPrincipal = null;
      if (principal == null) {
         Set<Principal> principals = (Set)map.get("PrincipalsSet");
         if (principals != null && !principals.isEmpty()) {
            Iterator i$ = principals.iterator();

            while(i$.hasNext()) {
               Principal p = (Principal)i$.next();
               if (!(p instanceof Group) && principal == null) {
                  principal = p;
               }

               if (p instanceof Group) {
                  Group g = (Group)Group.class.cast(p);
                  if (g.getName().equals("CallerPrincipal") && callerPrincipal == null) {
                     Enumeration<? extends Principal> e = g.members();
                     if (e.hasMoreElements()) {
                        callerPrincipal = (Principal)e.nextElement();
                     }
                  }
               }
            }
         }
      }

      return callerPrincipal == null ? principal : callerPrincipal;
   }
}
