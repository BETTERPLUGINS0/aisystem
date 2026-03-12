package fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.AbstractJACCModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.EJBResource;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;

public class EJBJACCPolicyModuleDelegate extends AbstractJACCModuleDelegate {
   private String ejbName = null;
   private Method ejbMethod = null;
   private String methodInterface = null;
   private CodeSource ejbCS = null;
   private String roleName = null;
   private Boolean roleRefCheck;
   private RunAsIdentity callerRunAs;

   public EJBJACCPolicyModuleDelegate() {
      this.roleRefCheck = Boolean.FALSE;
   }

   public int authorize(Resource resource, Subject callerSubject, RoleGroup role) {
      if (!(resource instanceof EJBResource)) {
         throw PicketBoxMessages.MESSAGES.invalidType(EJBResource.class.getName());
      } else {
         EJBResource ejbResource = (EJBResource)resource;
         Map<String, Object> map = resource.getMap();
         if (map == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("resourceMap");
         } else {
            this.policyRegistration = (PolicyRegistration)map.get("policyRegistration");
            this.ejbCS = ejbResource.getCodeSource();
            this.ejbMethod = ejbResource.getEjbMethod();
            this.ejbName = ejbResource.getEjbName();
            this.methodInterface = ejbResource.getEjbMethodInterface();
            RunAs runAs = ejbResource.getCallerRunAsIdentity();
            if (runAs instanceof RunAsIdentity) {
               this.callerRunAs = (RunAsIdentity)RunAsIdentity.class.cast(runAs);
            }

            this.roleName = (String)map.get("roleName");
            this.roleRefCheck = (Boolean)map.get("roleRefPermissionCheck");
            return this.roleRefCheck == Boolean.TRUE ? this.checkRoleRef(callerSubject, role) : this.process(callerSubject, role);
         }
      }
   }

   private int process(Subject callerSubject, Role role) {
      EJBMethodPermission methodPerm = new EJBMethodPermission(this.ejbName, this.methodInterface, this.ejbMethod);
      boolean policyDecision = this.checkWithPolicy(methodPerm, callerSubject, role);
      if (!policyDecision) {
         PicketBoxLogger.LOGGER.debugJACCDeniedAccess(methodPerm.toString(), callerSubject, role != null ? role.toString() : null);
      }

      return policyDecision ? 1 : -1;
   }

   private int checkRoleRef(Subject callerSubject, RoleGroup callerRoles) {
      EJBRoleRefPermission ejbRoleRefPerm = new EJBRoleRefPermission(this.ejbName, this.roleName);
      boolean policyDecision = this.checkWithPolicy(ejbRoleRefPerm, callerSubject, callerRoles);
      if (!policyDecision) {
         PicketBoxLogger.LOGGER.debugJACCDeniedAccess(ejbRoleRefPerm.toString(), callerSubject, callerRoles != null ? callerRoles.toString() : null);
      }

      return policyDecision ? 1 : -1;
   }

   private boolean checkWithPolicy(Permission ejbPerm, Subject subject, Role role) {
      ProtectionDomain pd;
      if (this.callerRunAs == null) {
         Principal[] principals = this.getPrincipals(subject, role);
         pd = new ProtectionDomain(this.ejbCS, (PermissionCollection)null, (ClassLoader)null, principals);
         return Policy.getPolicy().implies(pd, ejbPerm);
      } else {
         Set<Principal> principals = this.callerRunAs.getRunAsRoles();
         pd = new ProtectionDomain(this.ejbCS, (PermissionCollection)null, (ClassLoader)null, (Principal[])principals.toArray(new Principal[principals.size()]));
         return Policy.getPolicy().implies(pd, ejbPerm);
      }
   }
}
