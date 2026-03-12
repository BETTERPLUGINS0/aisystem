package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.util.StringPropertyReplacer;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

public class PropertiesRolesMappingProvider extends AbstractRolesMappingProvider {
   protected String rolesRsrcName = "roles.properties";
   protected Properties roles;

   public void init(Map<String, Object> options) {
      if (options != null) {
         String option = (String)options.get("rolesProperties");
         if (option != null) {
            this.rolesRsrcName = StringPropertyReplacer.replaceProperties(option);
         }

         try {
            this.roles = this.loadRoles();
         } catch (IOException var4) {
            throw new IllegalStateException(var4);
         }
      }

   }

   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject) {
      if (contextMap != null && !contextMap.isEmpty()) {
         Principal principal = this.getCallerPrincipal(contextMap);
         if (principal != null) {
            String username = principal.getName();
            Util.addRolesToGroup(username, mappedObject, this.roles);
            this.result.setMappedObject(mappedObject);
         }

      } else {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");
      }
   }

   protected Properties loadRoles() throws IOException {
      return Util.loadProperties(this.rolesRsrcName);
   }
}
