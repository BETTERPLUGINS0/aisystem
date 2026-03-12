package fr.xephi.authme.libs.org.jboss.security.identity.plugins;

import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleType;

public class SimpleRole implements Role, Cloneable {
   private static final long serialVersionUID = 1L;
   private final String roleName;
   private final Role parent;
   public static final String ANYBODY = "<ANYBODY>";
   public static final Role ANYBODY_ROLE = new SimpleRole("<ANYBODY>");

   public SimpleRole(String roleName) {
      this(roleName, (Role)null);
   }

   public SimpleRole(String roleName, Role parent) {
      this.roleName = roleName;
      this.parent = parent;
   }

   public String getRoleName() {
      return this.roleName;
   }

   public RoleType getType() {
      return RoleType.simple;
   }

   public boolean containsAll(Role anotherRole) {
      if (anotherRole.getType() == RoleType.simple) {
         return "<ANYBODY>".equals(this.roleName) ? true : this.roleName.equals(anotherRole.getRoleName());
      } else {
         return false;
      }
   }

   public Role getParent() {
      return this.parent;
   }

   protected Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public String toString() {
      return this.roleName;
   }

   public int hashCode() {
      int hashCode = this.roleName.hashCode();
      if (this.parent != null) {
         hashCode += this.parent.hashCode();
      }

      return hashCode;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof SimpleRole)) {
         return false;
      } else {
         SimpleRole other = (SimpleRole)obj;
         return this.parent != null ? this.roleName.equals(other.roleName) && this.parent.equals(other.parent) : this.roleName.equals(other.roleName) && other.parent == null;
      }
   }
}
