package fr.xephi.authme.libs.org.jboss.security.authorization.modules.web;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.WebResource;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.PolicyDecisionPoint;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.RequestContext;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.ResponseContext;
import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.servlet.http.HttpServletRequest;

public class WebXACMLPolicyModuleDelegate extends AuthorizationModuleDelegate {
   private String policyContextID = null;

   public int authorize(Resource resource, Subject subject, RoleGroup role) {
      if (!(resource instanceof WebResource)) {
         throw PicketBoxMessages.MESSAGES.invalidType(WebResource.class.getName());
      } else {
         WebResource webResource = (WebResource)resource;
         Map<String, Object> map = resource.getMap();
         if (map == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("resourceMap");
         } else {
            HttpServletRequest request = (HttpServletRequest)webResource.getServletRequest();
            this.policyRegistration = (PolicyRegistration)map.get("policyRegistration");
            if (this.policyRegistration == null) {
               throw PicketBoxMessages.MESSAGES.invalidNullProperty("policyRegistration");
            } else {
               this.policyContextID = webResource.getPolicyContextID();
               Boolean userDataCheck = this.checkBooleanValue((Boolean)map.get("userDataPermissionCheck"));
               Boolean roleRefCheck = this.checkBooleanValue((Boolean)map.get("roleRefPermissionCheck"));
               if (!userDataCheck && !roleRefCheck) {
                  if (request == null) {
                     throw PicketBoxMessages.MESSAGES.invalidNullProperty("servletRequest");
                  } else {
                     return this.process(request, role);
                  }
               } else {
                  return 1;
               }
            }
         }
      }
   }

   private Boolean checkBooleanValue(Boolean bool) {
      return bool == null ? Boolean.FALSE : bool;
   }

   private int process(HttpServletRequest request, RoleGroup callerRoles) {
      Principal userP = request.getUserPrincipal();
      if (userP == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("userPrincipal");
      } else {
         int result = true;
         WebXACMLUtil util = new WebXACMLUtil();

         int result;
         try {
            RequestContext requestCtx = util.createXACMLRequest(request, callerRoles);
            if (this.policyContextID == null) {
               this.policyContextID = PolicyContext.getContextID();
            }

            PolicyDecisionPoint pdp = util.getPDP(this.policyRegistration, this.policyContextID);
            ResponseContext response = pdp.evaluate(requestCtx);
            result = response.getDecision() == 0 ? 1 : -1;
         } catch (Exception var9) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var9);
            result = -1;
         }

         return result;
      }
   }
}
