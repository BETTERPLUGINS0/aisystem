package fr.xephi.authme.libs.org.jboss.security.javaee;

import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.javaee.exceptions.MissingArgumentsException;
import fr.xephi.authme.libs.org.jboss.security.javaee.exceptions.WrongEEResourceException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.Principal;
import java.util.Set;
import javax.security.auth.Subject;

public abstract class AbstractEJBAuthorizationHelper extends AbstractJavaEEHelper {
   protected String version;

   public abstract String getEJBVersion();

   public abstract void setEJBVersion(String var1);

   public abstract boolean authorize(Resource var1) throws WrongEEResourceException;

   /** @deprecated */
   public abstract boolean authorize(String var1, Method var2, Principal var3, String var4, CodeSource var5, Subject var6, RunAs var7, String var8, RoleGroup var9);

   public abstract boolean isCallerInRole(Resource var1, String var2) throws WrongEEResourceException, MissingArgumentsException;

   /** @deprecated */
   public abstract boolean isCallerInRole(String var1, String var2, Principal var3, Subject var4, String var5, Set<SecurityRoleRef> var6);

   /** @deprecated */
   public abstract boolean isCallerInRole(String var1, String var2, Principal var3, Subject var4, String var5, Set<SecurityRoleRef> var6, boolean var7);
}
