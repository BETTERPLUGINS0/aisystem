package fr.xephi.authme.libs.org.jboss.security.identity;

import java.io.Serializable;

public interface Role extends Serializable {
   String getRoleName();

   RoleType getType();

   boolean containsAll(Role var1);

   Role getParent();
}
