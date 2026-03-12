package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;

public class EntitlementEntry {
   private final Resource resource;
   private final ACLPermission permission;
   private final String identityOrRole;

   public EntitlementEntry(Resource resource, ACLPermission permission, String identityOrRole) {
      if (resource == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource");
      } else if (permission == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("permission");
      } else {
         this.resource = resource;
         this.permission = permission;
         this.identityOrRole = identityOrRole;
      }
   }

   public Resource getResource() {
      return this.resource;
   }

   public ACLPermission getPermission() {
      return this.permission;
   }

   public String getIdentityOrRole() {
      return this.identityOrRole;
   }
}
