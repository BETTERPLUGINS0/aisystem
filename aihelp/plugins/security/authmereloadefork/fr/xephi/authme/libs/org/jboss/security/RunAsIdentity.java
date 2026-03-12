package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RunAsIdentity extends SimplePrincipal implements Cloneable, RunAs {
   private static final long serialVersionUID = -3236178735180485083L;
   private HashSet<Principal> runAsRoles;
   private HashSet<Principal> principalsSet;
   private static final String ANOYMOUS_PRINCIPAL = "anonymous";

   public RunAsIdentity(String roleName, String principalName) {
      super(principalName != null ? principalName : "anonymous");
      this.runAsRoles = new HashSet();
      if (roleName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleName");
      } else {
         this.runAsRoles.add(new SimplePrincipal(roleName));
      }
   }

   public RunAsIdentity(String roleName, String principalName, Set<String> extraRoleNames) {
      this(roleName, principalName);
      if (extraRoleNames != null) {
         Iterator it = extraRoleNames.iterator();

         while(it.hasNext()) {
            String extraRoleName = (String)it.next();
            this.runAsRoles.add(new SimplePrincipal(extraRoleName));
         }
      }

   }

   public Set<Principal> getRunAsRoles() {
      return new HashSet(this.runAsRoles);
   }

   public RoleGroup getRunAsRolesAsRoleGroup() {
      return new SimpleRoleGroup(this.runAsRoles);
   }

   public synchronized Set<Principal> getPrincipalsSet() {
      if (this.principalsSet == null) {
         this.principalsSet = new HashSet();
         this.principalsSet.add(this);
         SimpleGroup roles = new SimpleGroup("Roles");
         this.principalsSet.add(roles);
         Iterator iter = this.runAsRoles.iterator();

         while(iter.hasNext()) {
            Principal role = (Principal)iter.next();
            roles.addMember(role);
         }
      }

      return this.principalsSet;
   }

   public boolean doesUserHaveRole(Principal role) {
      return this.runAsRoles.contains(role);
   }

   public boolean doesUserHaveRole(Set<Principal> methodRoles) {
      Iterator it = methodRoles.iterator();

      Principal role;
      do {
         if (!it.hasNext()) {
            return false;
         }

         role = (Principal)it.next();
      } while(!this.doesUserHaveRole(role));

      return true;
   }

   public String toString() {
      return "[roles=" + this.runAsRoles + ",principal=" + this.getName() + "]";
   }

   public synchronized Object clone() throws CloneNotSupportedException {
      RunAsIdentity clone = (RunAsIdentity)super.clone();
      if (clone != null) {
         clone.principalsSet = this.principalsSet != null ? (HashSet)this.principalsSet.clone() : null;
         clone.runAsRoles = (HashSet)this.runAsRoles.clone();
      }

      return clone;
   }

   public <T> T getIdentity() {
      return this.getName();
   }

   public <T> T getProof() {
      return "JavaEE";
   }
}
