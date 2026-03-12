package fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.EJBResource;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.PolicyDecisionPoint;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.RequestContext;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.ResponseContext;
import java.util.Map;
import javax.security.auth.Subject;

public class EJBXACMLPolicyModuleDelegate extends EJBPolicyModuleDelegate {
   private String policyContextID;

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
            if (this.policyRegistration == null) {
               throw PicketBoxMessages.MESSAGES.invalidNullProperty("policyRegistration");
            } else {
               this.callerRunAs = ejbResource.getCallerRunAsIdentity();
               this.ejbName = ejbResource.getEjbName();
               this.ejbMethod = ejbResource.getEjbMethod();
               this.ejbPrincipal = ejbResource.getPrincipal();
               this.policyContextID = ejbResource.getPolicyContextID();
               if (this.policyContextID == null) {
                  throw PicketBoxMessages.MESSAGES.invalidNullProperty("contextID");
               } else {
                  this.securityRoleReferences = ejbResource.getSecurityRoleReferences();
                  this.roleName = (String)map.get("roleName");
                  Boolean roleRefCheck = this.checkBooleanValue((Boolean)map.get("roleRefPermissionCheck"));
                  return roleRefCheck ? this.checkRoleRef(role) : this.process(role);
               }
            }
         }
      }
   }

   private int process(RoleGroup callerRoles) {
      int result = true;
      EJBXACMLUtil util = new EJBXACMLUtil();

      int result;
      try {
         RequestContext requestCtx = util.createXACMLRequest(this.ejbName, this.ejbMethod, this.ejbPrincipal, callerRoles);
         PolicyDecisionPoint pdp = util.getPDP(this.policyRegistration, this.policyContextID);
         if (pdp == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("PDP");
         }

         ResponseContext response = pdp.evaluate(requestCtx);
         result = response.getDecision() == 0 ? 1 : -1;
      } catch (Exception var7) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var7);
         result = -1;
      }

      return result;
   }

   private Boolean checkBooleanValue(Boolean bool) {
      return bool == null ? Boolean.FALSE : bool;
   }
}
