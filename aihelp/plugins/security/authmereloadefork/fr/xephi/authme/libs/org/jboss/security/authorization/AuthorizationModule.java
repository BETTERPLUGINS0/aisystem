package fr.xephi.authme.libs.org.jboss.security.authorization;

import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public interface AuthorizationModule {
   boolean abort() throws AuthorizationException;

   boolean commit() throws AuthorizationException;

   void initialize(Subject var1, CallbackHandler var2, Map<String, Object> var3, Map<String, Object> var4, RoleGroup var5);

   int authorize(Resource var1);

   boolean destroy();
}
