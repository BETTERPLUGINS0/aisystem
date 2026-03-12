package fr.xephi.authme.libs.org.jboss.security.authorization.modules.web;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.AbstractJACCModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.WebResource;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import java.io.IOException;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.jacc.WebResourcePermission;
import javax.security.jacc.WebRoleRefPermission;
import javax.security.jacc.WebUserDataPermission;
import javax.servlet.http.HttpServletRequest;

public class WebJACCPolicyModuleDelegate extends AbstractJACCModuleDelegate {
   private Policy policy = Policy.getPolicy();
   private HttpServletRequest request = null;
   private CodeSource webCS = null;
   private String canonicalRequestURI = null;

   public int authorize(Resource resource, Subject callerSubject, RoleGroup role) {
      if (!(resource instanceof WebResource)) {
         throw PicketBoxMessages.MESSAGES.invalidType(WebResource.class.getName());
      } else {
         WebResource webResource = (WebResource)resource;
         Map<String, Object> map = resource.getMap();
         if (map == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("resourceMap");
         } else {
            this.request = (HttpServletRequest)webResource.getServletRequest();
            this.webCS = webResource.getCodeSource();
            this.canonicalRequestURI = webResource.getCanonicalRequestURI();
            String roleName = (String)map.get("roleName");
            Principal principal = (Principal)map.get("hasRole.Principal");
            Set<Principal> roles = (Set)map.get("principal.roles");
            String servletName = webResource.getServletName();
            Boolean resourceCheck = this.checkBooleanValue((Boolean)map.get("resourcePermissionCheck"));
            Boolean userDataCheck = this.checkBooleanValue((Boolean)map.get("userDataPermissionCheck"));
            Boolean roleRefCheck = this.checkBooleanValue((Boolean)map.get("roleRefPermissionCheck"));
            this.validatePermissionChecks(resourceCheck, userDataCheck, roleRefCheck);
            boolean decision = false;

            try {
               if (resourceCheck) {
                  decision = this.hasResourcePermission(callerSubject, role);
               } else if (userDataCheck) {
                  decision = this.hasUserDataPermission();
               } else if (roleRefCheck) {
                  decision = this.hasRole(principal, roleName, roles, servletName);
               } else {
                  PicketBoxLogger.LOGGER.debugInvalidWebJaccCheck();
               }
            } catch (IOException var15) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var15);
            }

            return decision ? 1 : -1;
         }
      }
   }

   public void setPolicyRegistrationManager(PolicyRegistration authzM) {
      this.policyRegistration = authzM;
   }

   private boolean checkPolicy(Permission perm, Principal requestPrincpal, Subject caller, Role role) {
      Principal[] principals = this.getPrincipals(caller, role);
      return this.checkPolicy(perm, principals);
   }

   private boolean checkPolicy(Permission perm, Principal[] principals) {
      ProtectionDomain pd = new ProtectionDomain(this.webCS, (PermissionCollection)null, (ClassLoader)null, principals);
      return this.policy.implies(pd, perm);
   }

   private Boolean checkBooleanValue(Boolean bool) {
      return bool == null ? Boolean.FALSE : bool;
   }

   private boolean hasResourcePermission(Subject caller, Role role) throws IOException {
      Principal requestPrincipal = this.request.getUserPrincipal();
      WebResourcePermission perm = new WebResourcePermission(this.canonicalRequestURI, this.request.getMethod());
      boolean allowed = this.checkPolicy(perm, requestPrincipal, caller, role);
      PicketBoxLogger.LOGGER.traceHasResourcePermission(perm.toString(), allowed);
      return allowed;
   }

   private boolean hasRole(Principal principal, String roleName, Set<Principal> roles, String servletName) {
      if (servletName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("servletName");
      } else {
         WebRoleRefPermission perm = new WebRoleRefPermission(servletName, roleName);
         Principal[] principals = new Principal[]{principal};
         if (roles != null) {
            principals = new Principal[roles.size()];
            roles.toArray(principals);
         }

         boolean allowed = this.checkPolicy(perm, principals);
         PicketBoxLogger.LOGGER.traceHasRolePermission(perm.toString(), allowed);
         return allowed;
      }
   }

   private boolean hasUserDataPermission() throws IOException {
      WebUserDataPermission perm = new WebUserDataPermission(this.canonicalRequestURI, this.request.getMethod());
      boolean ok = false;

      try {
         Principal[] principals = null;
         ok = this.checkPolicy(perm, (Principal[])principals);
      } catch (Exception var4) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var4);
      }

      PicketBoxLogger.LOGGER.traceHasUserDataPermission(perm.toString(), ok);
      return ok;
   }

   private void validatePermissionChecks(Boolean resourceCheck, Boolean userDataCheck, Boolean roleRefCheck) {
      if (resourceCheck == Boolean.TRUE && userDataCheck == Boolean.TRUE && roleRefCheck == Boolean.TRUE || resourceCheck == Boolean.TRUE && userDataCheck == Boolean.TRUE || userDataCheck == Boolean.TRUE && roleRefCheck == Boolean.TRUE) {
         throw PicketBoxMessages.MESSAGES.invalidPermissionChecks();
      }
   }
}
