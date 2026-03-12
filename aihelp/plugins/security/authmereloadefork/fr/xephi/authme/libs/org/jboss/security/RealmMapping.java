package fr.xephi.authme.libs.org.jboss.security;

import java.security.Principal;
import java.util.Set;

public interface RealmMapping {
   Principal getPrincipal(Principal var1);

   boolean doesUserHaveRole(Principal var1, Set<Principal> var2);

   Set<Principal> getUserRoles(Principal var1);
}
