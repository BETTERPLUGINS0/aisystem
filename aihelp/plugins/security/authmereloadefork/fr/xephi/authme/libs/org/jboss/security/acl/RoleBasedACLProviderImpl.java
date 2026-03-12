package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RoleBasedACLProviderImpl extends ACLProviderImpl {
   public <T> Set<T> getEntitlements(Class<T> clazz, Resource resource, Identity identity) throws AuthorizationException {
      if (identity.getRole() == null) {
         return super.getEntitlements(clazz, resource, identity);
      } else if (!EntitlementEntry.class.equals(clazz)) {
         return null;
      } else {
         Set<EntitlementEntry> entitlements = new HashSet();
         List<Role> roles = new ArrayList();
         this.getAllRoles(identity.getRole(), roles);
         Iterator i$ = roles.iterator();

         while(i$.hasNext()) {
            Role role = (Role)i$.next();
            ACLPermission permission = super.getInitialPermissions(resource, role.getRoleName());
            if (permission != null) {
               super.fillEntitlements(entitlements, resource, role.getRoleName(), permission);
            }
         }

         return entitlements;
      }
   }

   public boolean isAccessGranted(Resource resource, Identity identity, ACLPermission permission) throws AuthorizationException {
      if (identity.getRole() == null) {
         return super.isAccessGranted(resource, identity, permission);
      } else if (super.strategy != null) {
         ACL acl = this.strategy.getACL(resource);
         if (acl != null) {
            List<Role> roles = new ArrayList();
            this.getAllRoles(identity.getRole(), roles);
            Iterator i$ = roles.iterator();

            ACLEntry entry;
            do {
               if (!i$.hasNext()) {
                  return false;
               }

               Role role = (Role)i$.next();
               entry = acl.getEntry(role.getRoleName());
            } while(entry == null || !entry.checkPermission(permission));

            return true;
         } else {
            throw new AuthorizationException(PicketBoxMessages.MESSAGES.unableToLocateACLForResourceMessage(resource != null ? resource.toString() : null));
         }
      } else {
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.unableToLocateACLWithNoStrategyMessage());
      }
   }

   protected void getAllRoles(Role role, List<Role> roles) {
      if (role instanceof RoleGroup) {
         RoleGroup group = (RoleGroup)role;
         Iterator i$ = group.getRoles().iterator();

         while(i$.hasNext()) {
            Role nestedRole = (Role)i$.next();
            this.getAllRoles(nestedRole, roles);
         }
      } else {
         roles.add(role);
      }

   }
}
