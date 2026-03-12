package fr.xephi.authme.libs.org.jboss.security.javaee;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class AbstractWebAuthorizationHelper extends AbstractJavaEEHelper {
   protected boolean enableAudit = false;

   public boolean isEnableAudit() {
      return this.enableAudit;
   }

   public void setEnableAudit(boolean enableAudit) {
      this.enableAudit = enableAudit;
   }

   public abstract boolean checkResourcePermission(Map<String, Object> var1, ServletRequest var2, ServletResponse var3, Subject var4, String var5, String var6);

   public abstract boolean checkResourcePermission(Map<String, Object> var1, ServletRequest var2, ServletResponse var3, Subject var4, String var5, String var6, List<String> var7);

   public abstract boolean hasRole(String var1, Principal var2, String var3, Set<Principal> var4, String var5, Subject var6);

   public abstract boolean hasRole(String var1, Principal var2, String var3, Set<Principal> var4, String var5, Subject var6, List<String> var7);

   public abstract boolean hasUserDataPermission(Map<String, Object> var1, ServletRequest var2, ServletResponse var3, String var4, Subject var5);

   public abstract boolean hasUserDataPermission(Map<String, Object> var1, ServletRequest var2, ServletResponse var3, String var4, Subject var5, List<String> var6);
}
