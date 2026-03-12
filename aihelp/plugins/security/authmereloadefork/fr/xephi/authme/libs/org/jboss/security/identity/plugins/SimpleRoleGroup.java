package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleType;
import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleRoleGroup extends SimpleRole implements RoleGroup {
   private static final long serialVersionUID = 1L;
   private ArrayList<Role> roles = new ArrayList();
   private static final String ROLES_IDENTIFIER = "Roles";

   public SimpleRoleGroup(String roleName) {
      super(roleName);
   }

   public SimpleRoleGroup(String roleName, List<Role> roles) {
      super(roleName);
      if (this.roles == null) {
         this.roles = new ArrayList();
      }

      this.addAll(roles);
   }

   public SimpleRoleGroup(Group rolesGroup) {
      super(rolesGroup.getName());
      Enumeration principals = rolesGroup.members();

      while(principals.hasMoreElements()) {
         SimpleRole role = new SimpleRole(((Principal)principals.nextElement()).getName());
         this.addRole(role);
      }

   }

   public SimpleRoleGroup(Set<Principal> rolesAsPrincipals) {
      super("Roles");
      Iterator i$ = rolesAsPrincipals.iterator();

      while(i$.hasNext()) {
         Principal p = (Principal)i$.next();
         SimpleRole role = new SimpleRole(p.getName());
         this.addRole(role);
      }

   }

   public RoleType getType() {
      return RoleType.group;
   }

   public synchronized void addRole(Role role) {
      if (!this.roles.contains(role)) {
         this.roles.add(role);
      }

   }

   public synchronized void addAll(List<Role> roles) {
      if (roles != null) {
         Iterator i$ = roles.iterator();

         while(i$.hasNext()) {
            Role role = (Role)i$.next();
            if (!this.roles.contains(role)) {
               this.roles.add(role);
            }
         }
      }

   }

   public synchronized void removeRole(Role role) {
      this.roles.remove(role);
   }

   public synchronized void clearRoles() {
      this.roles.clear();
   }

   public List<Role> getRoles() {
      return Collections.unmodifiableList(this.roles);
   }

   public synchronized Object clone() throws CloneNotSupportedException {
      SimpleRoleGroup clone = (SimpleRoleGroup)super.clone();
      if (clone != null) {
         clone.roles = (ArrayList)this.roles.clone();
      }

      return clone;
   }

   public boolean containsAll(Role anotherRole) {
      boolean isContained = false;
      if (anotherRole.getType() == RoleType.simple) {
         synchronized(this) {
            Iterator i$ = this.roles.iterator();

            do {
               if (!i$.hasNext()) {
                  return false;
               }

               Role r = (Role)i$.next();
               isContained = r.containsAll(anotherRole);
            } while(!isContained);

            return true;
         }
      } else {
         RoleGroup anotherRG = (RoleGroup)anotherRole;
         CopyOnWriteArrayList<Role> anotherRoles = new CopyOnWriteArrayList(anotherRG.getRoles());
         Iterator i$ = anotherRoles.iterator();

         Role r;
         do {
            if (!i$.hasNext()) {
               return true;
            }

            r = (Role)i$.next();
         } while(this.containsAll(r));

         return false;
      }
   }

   public boolean containsAtleastOneRole(RoleGroup anotherRole) {
      if (anotherRole == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("anotherRole");
      } else {
         CopyOnWriteArrayList<Role> roleList = new CopyOnWriteArrayList(anotherRole.getRoles());
         Iterator i$ = roleList.iterator();

         Role r;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            r = (Role)i$.next();
         } while(!this.containsAll(r));

         return true;
      }
   }

   public synchronized boolean containsRole(Role role) {
      Iterator i$ = this.roles.iterator();

      Role r;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         r = (Role)i$.next();
      } while(!r.containsAll(role));

      return true;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getRoleName());
      builder.append("(");
      synchronized(this) {
         Iterator i$ = this.roles.iterator();

         while(true) {
            if (!i$.hasNext()) {
               break;
            }

            Role role = (Role)i$.next();
            builder.append(role.toString()).append(",");
         }
      }

      builder.append(")");
      return builder.toString();
   }
}
