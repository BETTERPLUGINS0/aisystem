package fr.xephi.authme.libs.org.jboss.security.identity;

import java.util.List;

public interface RoleGroup extends Role {
   List<Role> getRoles();

   void addRole(Role var1);

   void addAll(List<Role> var1);

   void clearRoles();

   void removeRole(Role var1);

   boolean containsRole(Role var1);

   boolean containsAtleastOneRole(RoleGroup var1);
}
