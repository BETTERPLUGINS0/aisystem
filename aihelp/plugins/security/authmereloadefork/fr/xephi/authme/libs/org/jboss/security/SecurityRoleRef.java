package fr.xephi.authme.libs.org.jboss.security;

public class SecurityRoleRef extends fr.xephi.authme.libs.org.jboss.security.javaee.SecurityRoleRef {
   public SecurityRoleRef() {
   }

   public SecurityRoleRef(String name, String link, String description) {
      super(name, link, description);
   }

   public SecurityRoleRef(String name, String link) {
      super(name, link);
   }
}
