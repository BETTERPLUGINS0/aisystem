package fr.xephi.authme.libs.org.jboss.security.authorization;

import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public abstract class AuthorizationContext {
   protected String securityDomainName = null;
   protected CallbackHandler callbackHandler = null;
   protected Map<String, Object> sharedState = new HashMap();
   public static final int PERMIT = 1;
   public static final int DENY = -1;

   public abstract int authorize(Resource var1) throws AuthorizationException;

   public abstract int authorize(Resource var1, Subject var2, RoleGroup var3) throws AuthorizationException;

   public String getSecurityDomain() {
      return this.securityDomainName;
   }
}
