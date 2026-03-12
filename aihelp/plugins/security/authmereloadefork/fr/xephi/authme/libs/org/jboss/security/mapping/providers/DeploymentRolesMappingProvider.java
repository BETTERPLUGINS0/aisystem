package fr.xephi.authme.libs.org.jboss.security.mapping.providers;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DeploymentRolesMappingProvider implements MappingProvider<RoleGroup> {
   private MappingResult<RoleGroup> result;

   public void init(Map<String, Object> options) {
   }

   public void setMappingResult(MappingResult<RoleGroup> res) {
      this.result = res;
   }

   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject) {
      if (contextMap != null && !contextMap.isEmpty()) {
         Principal principal = (Principal)contextMap.get("Principal");
         Map<String, Set<String>> principalRolesMap = (Map)contextMap.get("deploymentPrincipalRolesMap");
         Set<Principal> subjectPrincipals = (Set)contextMap.get("PrincipalsSet");
         PicketBoxLogger.LOGGER.debugMappingProviderOptions(principal, principalRolesMap, subjectPrincipals);
         if (principalRolesMap != null && !principalRolesMap.isEmpty()) {
            if (principal != null) {
               mappedObject = this.mapGroup(principal, principalRolesMap, mappedObject);
            }

            if (subjectPrincipals != null) {
               Iterator i$ = subjectPrincipals.iterator();

               while(i$.hasNext()) {
                  Principal p = (Principal)i$.next();
                  if (!(p instanceof Group)) {
                     mappedObject = this.mapGroup(p, principalRolesMap, mappedObject);
                  }
               }
            }

            this.result.setMappedObject(mappedObject);
         } else {
            this.result.setMappedObject(mappedObject);
         }
      } else {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");
      }
   }

   public boolean supports(Class<?> p) {
      return RoleGroup.class.isAssignableFrom(p);
   }

   private RoleGroup mapGroup(Principal principal, Map<String, Set<String>> principalRolesMap, RoleGroup mappedObject) {
      Set<String> roleset = (Set)principalRolesMap.get(principal.getName());
      if (roleset != null) {
         RoleGroup newRoles = new SimpleRoleGroup("Roles");
         if (roleset != null) {
            Iterator i$ = roleset.iterator();

            while(i$.hasNext()) {
               String r = (String)i$.next();
               newRoles.addRole(new SimpleRole(r));
            }
         }

         mappedObject.clearRoles();
         mappedObject.addAll(newRoles.getRoles());
      }

      return mappedObject;
   }
}
