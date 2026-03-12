package fr.xephi.authme.libs.waffle.jaas;

import java.io.Serializable;
import java.security.Principal;

public class UserPrincipal implements Principal, Serializable {
   private static final long serialVersionUID = 1L;
   private final String fqn;

   public UserPrincipal(String newFqn) {
      this.fqn = newFqn;
   }

   public String getName() {
      return this.fqn;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o instanceof UserPrincipal ? this.getName().equals(((UserPrincipal)o).getName()) : false;
      }
   }

   public int hashCode() {
      return this.getName().hashCode();
   }
}
