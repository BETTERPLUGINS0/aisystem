package fr.xephi.authme.libs.org.jboss.security.authorization.resources;

import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.lang.reflect.Method;
import java.util.Map;

public class EJBResource extends JavaEEResource {
   private Method ejbMethod = null;
   private String ejbName = null;
   private String ejbMethodInterface = null;
   private RoleGroup ejbMethodRoles = null;
   public static final String EJB_VERSION_1_1 = "1.1";
   public static final String EJB_VERSION_2_0 = "2.0";
   public static final String EJB_VERSION_3_0 = "3.0";
   private String version = "2.0";
   private boolean enforceEJBRestrictions = false;

   public EJBResource(Map<String, Object> map) {
      this.map = map;
   }

   public ResourceType getLayer() {
      return ResourceType.EJB;
   }

   public String getEjbName() {
      return this.ejbName;
   }

   public void setEjbName(String ejbName) {
      this.ejbName = ejbName;
   }

   public Method getEjbMethod() {
      return this.ejbMethod;
   }

   public void setEjbMethod(Method ejbMethod) {
      this.ejbMethod = ejbMethod;
   }

   public String getEjbMethodInterface() {
      return this.ejbMethodInterface;
   }

   public void setEjbMethodInterface(String ejbMethodInterface) {
      this.ejbMethodInterface = ejbMethodInterface;
   }

   public RoleGroup getEjbMethodRoles() {
      return this.ejbMethodRoles;
   }

   public void setEjbMethodRoles(RoleGroup ejbMethodRoles) {
      this.ejbMethodRoles = ejbMethodRoles;
   }

   public boolean isEnforceEJBRestrictions() {
      return this.enforceEJBRestrictions;
   }

   public void setEnforceEJBRestrictions(boolean enforceEJBRestrictions) {
      this.enforceEJBRestrictions = enforceEJBRestrictions;
   }

   public String getEjbVersion() {
      return this.version;
   }

   public void setEjbVersion(String version) {
      this.version = version;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("[").append(this.getClass().getName()).append(":contextMap=").append(this.map).append(":method=").append(this.ejbMethod).append(":ejbMethodInterface=").append(this.ejbMethodInterface).append(":ejbName=").append(this.ejbName).append(":ejbPrincipal=").append(this.principal).append(":MethodRoles=").append(this.ejbMethodRoles).append(":securityRoleReferences=").append(this.securityRoleReferences).append(":callerSubject=").append(this.callerSubject).append(":callerRunAs=").append(this.callerRunAsIdentity).append(":callerRunAs=").append(this.callerRunAsIdentity).append(":ejbRestrictionEnforcement=").append(this.enforceEJBRestrictions).append(":ejbVersion=").append(this.version).append("]");
      return buf.toString();
   }
}
