package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import java.security.Principal;
import java.security.acl.Group;

public class SimpleIdentity implements Identity {
   private static final long serialVersionUID = 1L;
   private final String name;
   private Role role;

   public SimpleIdentity(String name) {
      this.name = name;
   }

   public SimpleIdentity(String name, String roleName) {
      this.name = name;
      this.role = new SimpleRole(roleName);
   }

   public SimpleIdentity(String name, Role role) {
      this.name = name;
      this.role = role;
   }

   public Group asGroup() {
      try {
         Group gp = IdentityFactory.createGroup("Roles");
         gp.addMember(IdentityFactory.createPrincipal(this.role.getRoleName()));
         return gp;
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public Principal asPrincipal() {
      try {
         return IdentityFactory.createPrincipal(this.name);
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   public String getName() {
      return this.name;
   }

   public Role getRole() {
      return this.role;
   }

   public boolean equals(Object obj) {
      if (obj instanceof SimpleIdentity) {
         SimpleIdentity other = (SimpleIdentity)obj;
         if (this.name != null) {
            return this.name.equals(other.name);
         } else {
            return other.name == null;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }
}
