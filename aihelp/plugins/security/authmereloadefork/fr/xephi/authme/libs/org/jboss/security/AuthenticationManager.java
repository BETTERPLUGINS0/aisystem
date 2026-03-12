package fr.xephi.authme.libs.org.jboss.security;

import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;

public interface AuthenticationManager extends BaseSecurityManager {
   boolean isValid(Principal var1, Object var2);

   boolean isValid(Principal var1, Object var2, Subject var3);

   /** @deprecated */
   Subject getActiveSubject();

   Principal getTargetPrincipal(Principal var1, Map<String, Object> var2);

   void logout(Principal var1, Subject var2);
}
