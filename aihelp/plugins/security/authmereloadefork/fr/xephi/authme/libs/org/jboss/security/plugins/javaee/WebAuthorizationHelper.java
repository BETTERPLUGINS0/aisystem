package fr.xephi.authme.libs.org.jboss.security.plugins.javaee;

import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.WebResource;
import fr.xephi.authme.libs.org.jboss.security.callbacks.SecurityContextCallbackHandler;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import fr.xephi.authme.libs.org.jboss.security.javaee.AbstractWebAuthorizationHelper;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class WebAuthorizationHelper extends AbstractWebAuthorizationHelper {
   public boolean checkResourcePermission(Map<String, Object> contextMap, ServletRequest request, ServletResponse response, Subject callerSubject, String contextID, String canonicalRequestURI) {
      return this.checkResourcePermission(contextMap, request, response, callerSubject, contextID, canonicalRequestURI, (List)null);
   }

   public boolean checkResourcePermission(Map<String, Object> contextMap, ServletRequest request, ServletResponse response, Subject callerSubject, String contextID, String canonicalRequestURI, List<String> roles) {
      if (contextID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else if (request == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("request");
      } else if (response == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("response");
      } else if (canonicalRequestURI == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("canonicalRequestURI");
      } else {
         AuthorizationManager authzMgr = this.securityContext.getAuthorizationManager();
         if (authzMgr == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            boolean isAuthorized = false;
            WebResource webResource = new WebResource(Collections.unmodifiableMap(contextMap));
            webResource.setPolicyContextID(contextID);
            webResource.setServletRequest(request);
            webResource.setServletResponse(response);
            webResource.setCallerSubject(callerSubject);
            webResource.setCanonicalRequestURI(canonicalRequestURI);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = null;
            String level;
            if (roles == null) {
               callerRoles = authzMgr.getSubjectRoles(callerSubject, sch);
            } else {
               callerRoles = new SimpleRoleGroup("Roles");
               Iterator i$ = roles.iterator();

               while(i$.hasNext()) {
                  level = (String)i$.next();
                  ((RoleGroup)callerRoles).addRole(new SimpleRole(level));
               }
            }

            try {
               int permit = authzMgr.authorize(webResource, callerSubject, (RoleGroup)callerRoles);
               isAuthorized = permit == 1;
               level = permit == 1 ? "Success" : "Failure";
               if (this.enableAudit) {
                  this.authorizationAudit(level, webResource, (Exception)null);
               }
            } catch (AuthorizationException var15) {
               isAuthorized = false;
               PicketBoxLogger.LOGGER.debugFailureExecutingMethod("hasResourcePermission", var15);
               if (this.enableAudit) {
                  this.authorizationAudit("Error", webResource, var15);
               }
            }

            return isAuthorized;
         }
      }
   }

   public boolean hasRole(String roleName, Principal principal, String servletName, Set<Principal> principalRoles, String contextID, Subject callerSubject) {
      return this.hasRole(roleName, principal, servletName, principalRoles, contextID, callerSubject, (List)null);
   }

   public boolean hasRole(String roleName, Principal principal, String servletName, Set<Principal> principalRoles, String contextID, Subject callerSubject, List<String> roles) {
      if (roleName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleName");
      } else if (contextID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else if (callerSubject == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerSubject");
      } else {
         AuthorizationManager authzMgr = this.securityContext.getAuthorizationManager();
         if (authzMgr == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            boolean hasTheRole = false;
            Map<String, Object> map = new HashMap();
            map.put("roleName", roleName);
            map.put("roleRefPermissionCheck", Boolean.TRUE);
            map.put("principal.roles", principalRoles);
            map.put("policyRegistration", this.getPolicyRegistration());
            WebResource webResource = new WebResource(Collections.unmodifiableMap(map));
            webResource.setPolicyContextID(contextID);
            webResource.setPrincipal(principal);
            webResource.setServletName(servletName);
            webResource.setCallerSubject(callerSubject);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = null;
            String level;
            if (roles == null) {
               callerRoles = authzMgr.getSubjectRoles(callerSubject, sch);
            } else {
               callerRoles = new SimpleRoleGroup("Roles");
               Iterator i$ = roles.iterator();

               while(i$.hasNext()) {
                  level = (String)i$.next();
                  ((RoleGroup)callerRoles).addRole(new SimpleRole(level));
               }
            }

            try {
               int permit = authzMgr.authorize(webResource, callerSubject, (RoleGroup)callerRoles);
               hasTheRole = permit == 1;
               level = hasTheRole ? "Success" : "Failure";
               if (this.enableAudit) {
                  this.authorizationAudit(level, webResource, (Exception)null);
               }
            } catch (AuthorizationException var16) {
               hasTheRole = false;
               PicketBoxLogger.LOGGER.debugFailureExecutingMethod("hasRole", var16);
               if (this.enableAudit) {
                  this.authorizationAudit("Error", webResource, var16);
               }
            }

            return hasTheRole;
         }
      }
   }

   public boolean hasUserDataPermission(Map<String, Object> contextMap, ServletRequest request, ServletResponse response, String contextID, Subject callerSubject) {
      return this.hasUserDataPermission(contextMap, request, response, contextID, callerSubject, (List)null);
   }

   public boolean hasUserDataPermission(Map<String, Object> contextMap, ServletRequest request, ServletResponse response, String contextID, Subject callerSubject, List<String> roles) {
      if (contextID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else if (callerSubject == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerSubject");
      } else if (request == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("request");
      } else if (response == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("response");
      } else {
         AuthorizationManager authzMgr = this.securityContext.getAuthorizationManager();
         if (authzMgr == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            boolean hasPerm = false;
            contextMap.put("policyRegistration", this.getPolicyRegistration());
            WebResource webResource = new WebResource(Collections.unmodifiableMap(contextMap));
            webResource.setPolicyContextID(contextID);
            webResource.setServletRequest(request);
            webResource.setServletResponse(response);
            webResource.setCallerSubject(callerSubject);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = null;
            String level;
            if (roles == null) {
               callerRoles = authzMgr.getSubjectRoles(callerSubject, sch);
            } else {
               callerRoles = new SimpleRoleGroup("Roles");
               Iterator i$ = roles.iterator();

               while(i$.hasNext()) {
                  level = (String)i$.next();
                  ((RoleGroup)callerRoles).addRole(new SimpleRole(level));
               }
            }

            try {
               int permit = authzMgr.authorize(webResource, callerSubject, (RoleGroup)callerRoles);
               hasPerm = permit == 1;
               level = hasPerm ? "Success" : "Failure";
               if (this.enableAudit) {
                  this.authorizationAudit(level, webResource, (Exception)null);
               }
            } catch (AuthorizationException var14) {
               hasPerm = false;
               PicketBoxLogger.LOGGER.debugFailureExecutingMethod("hasUserDataPermission", var14);
               if (this.enableAudit) {
                  this.authorizationAudit("Error", webResource, var14);
               }
            }

            return hasPerm;
         }
      }
   }
}
