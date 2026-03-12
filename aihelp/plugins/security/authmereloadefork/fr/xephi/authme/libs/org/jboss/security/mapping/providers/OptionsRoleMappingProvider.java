package fr.xephi.authme.libs.org.jboss.security.mapping.providers;

import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class OptionsRoleMappingProvider implements MappingProvider<RoleGroup> {
   private static final String REPLACE_ROLES_STRING = "replaceRoles";
   private static final String ROLES_MAP = "rolesMap";
   private MappingResult<RoleGroup> result;
   private Map<String, Object> options = null;
   private Properties roleMapProperties = new Properties();
   private boolean REPLACE_ROLES = false;

   public void init(Map<String, Object> opt) {
      this.options = opt;
      if (this.options != null) {
         if (this.options.containsKey("replaceRoles")) {
            this.REPLACE_ROLES = "true".equalsIgnoreCase((String)this.options.get("replaceRoles"));
         }

         if (this.options.containsKey("rolesMap")) {
            this.roleMapProperties = (Properties)this.options.get("rolesMap");
         }
      }

   }

   public void setMappingResult(MappingResult<RoleGroup> res) {
      this.result = res;
   }

   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject) {
      ArrayList<Role> removeMembers = new ArrayList();
      ArrayList<Role> addMembers = new ArrayList();
      List<Role> rolesList = mappedObject.getRoles();
      Iterator i$;
      Role r;
      if (rolesList != null) {
         i$ = rolesList.iterator();

         label52:
         while(true) {
            String commaSeparatedRoles;
            do {
               if (!i$.hasNext()) {
                  break label52;
               }

               r = (Role)i$.next();
               commaSeparatedRoles = this.roleMapProperties.getProperty(r.getRoleName());
            } while(commaSeparatedRoles == null);

            String[] tokens = MappingProviderUtil.getRolesFromCommaSeparatedString(commaSeparatedRoles);
            int len = tokens != null ? tokens.length : 0;

            for(int i = 0; i < len; ++i) {
               if (this.REPLACE_ROLES) {
                  removeMembers.add(r);
               }

               addMembers.add(new SimpleRole(tokens[i]));
            }
         }
      }

      i$ = removeMembers.iterator();

      while(i$.hasNext()) {
         r = (Role)i$.next();
         mappedObject.removeRole(r);
      }

      i$ = addMembers.iterator();

      while(i$.hasNext()) {
         r = (Role)i$.next();
         mappedObject.addRole(r);
      }

      this.result.setMappedObject(mappedObject);
   }

   public boolean supports(Class<?> p) {
      return RoleGroup.class.isAssignableFrom(p);
   }
}
