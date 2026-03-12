package fr.xephi.authme.libs.org.jboss.security.mapping.providers;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DeploymentRoleToRolesMappingProvider implements MappingProvider<RoleGroup> {
   private MappingResult<RoleGroup> result;

   public void init(Map<String, Object> options) {
   }

   public void setMappingResult(MappingResult<RoleGroup> res) {
      this.result = res;
   }

   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject) {
      if (contextMap != null && !contextMap.isEmpty()) {
         Principal principal = (Principal)contextMap.get("Principal");
         Map<String, Set<String>> roleToRolesMap = (Map)contextMap.get("deploymentPrincipalRolesMap");
         Set<Principal> subjectPrincipals = (Set)contextMap.get("PrincipalsSet");
         PicketBoxLogger.LOGGER.debugMappingProviderOptions(principal, roleToRolesMap, subjectPrincipals);
         if (roleToRolesMap != null && !roleToRolesMap.isEmpty()) {
            RoleGroup newRoles = new SimpleRoleGroup("Roles");
            RoleGroup assignedRoles = (SimpleRoleGroup)contextMap.get("Roles");
            Iterator i$ = assignedRoles.getRoles().iterator();

            while(i$.hasNext()) {
               Role r = (Role)i$.next();
               boolean mappedRoleIncluded = false;
               Iterator i$ = roleToRolesMap.keySet().iterator();

               while(i$.hasNext()) {
                  String mappedRole = (String)i$.next();
                  if (((Set)roleToRolesMap.get(mappedRole)).contains(r.getRoleName())) {
                     newRoles.addRole(new SimpleRole(mappedRole));
                     mappedRoleIncluded = true;
                  }
               }

               if (!mappedRoleIncluded) {
                  newRoles.addRole(r);
               }
            }

            mappedObject.clearRoles();
            mappedObject.addAll(newRoles.getRoles());
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
}
