package fr.xephi.authme.libs.org.jboss.security.plugins.javaee;

import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.EJBResource;
import fr.xephi.authme.libs.org.jboss.security.callbacks.SecurityContextCallbackHandler;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.javaee.AbstractEJBAuthorizationHelper;
import fr.xephi.authme.libs.org.jboss.security.javaee.SecurityRoleRef;
import fr.xephi.authme.libs.org.jboss.security.javaee.exceptions.MissingArgumentsException;
import fr.xephi.authme.libs.org.jboss.security.javaee.exceptions.WrongEEResourceException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.Principal;
import java.util.HashMap;
import java.util.Set;
import javax.naming.InitialContext;
import javax.security.auth.Subject;

public class EJBAuthorizationHelper extends AbstractEJBAuthorizationHelper {
   protected String POLICY_REGISTRATION_JNDI = "java:/policyRegistration";

   public boolean authorize(String ejbName, Method ejbMethod, Principal ejbPrincipal, String invocationInterfaceString, CodeSource ejbCodeSource, Subject callerSubject, RunAs callerRunAs, String contextID, RoleGroup methodRoles) {
      if (ejbName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbName");
      } else if (ejbMethod == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbMethod");
      } else if (ejbCodeSource == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbCodeSource");
      } else if (contextID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else if (callerSubject == null && callerRunAs == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerSubject");
      } else {
         AuthorizationManager am = this.securityContext.getAuthorizationManager();
         if (am == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            HashMap map = new HashMap();

            try {
               if (this.policyRegistration == null) {
                  this.policyRegistration = this.getPolicyRegistrationFromJNDI();
               }
            } catch (Exception var18) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var18);
            }

            map.put("policyRegistration", this.policyRegistration);
            EJBResource ejbResource = new EJBResource(map);
            ejbResource.setEjbVersion(this.version);
            ejbResource.setPolicyContextID(contextID);
            ejbResource.setCallerRunAsIdentity(callerRunAs);
            ejbResource.setEjbName(ejbName);
            ejbResource.setEjbMethod(ejbMethod);
            ejbResource.setPrincipal(ejbPrincipal);
            ejbResource.setEjbMethodInterface(invocationInterfaceString);
            ejbResource.setCodeSource(ejbCodeSource);
            ejbResource.setCallerRunAsIdentity(callerRunAs);
            ejbResource.setCallerSubject(callerSubject);
            ejbResource.setEjbMethodRoles(methodRoles);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = am.getSubjectRoles(callerSubject, sch);
            boolean isAuthorized = false;

            try {
               int check = am.authorize(ejbResource, callerSubject, (RoleGroup)callerRoles);
               isAuthorized = check == 1;
               this.authorizationAudit(isAuthorized ? "Success" : "Failure", ejbResource, (Exception)null);
            } catch (Exception var17) {
               isAuthorized = false;
               PicketBoxLogger.LOGGER.debugAuthorizationError(var17);
               this.authorizationAudit("Error", ejbResource, var17);
            }

            return isAuthorized;
         }
      }
   }

   public boolean isCallerInRole(String roleName, String ejbName, Principal ejbPrincipal, Subject callerSubject, String contextID, Set<SecurityRoleRef> securityRoleRefs) {
      return this.isCallerInRole(roleName, ejbName, ejbPrincipal, callerSubject, contextID, securityRoleRefs, false);
   }

   public boolean isCallerInRole(Resource resource, String roleName) throws WrongEEResourceException, MissingArgumentsException {
      boolean isAuthorized = false;
      EJBResource ejbResource = (EJBResource)resource;
      if (roleName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleName");
      } else if (ejbResource.getEjbName() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbName");
      } else if (ejbResource.getPolicyContextID() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else {
         AuthorizationManager am = this.securityContext.getAuthorizationManager();
         Subject callerSubject = ejbResource.getCallerSubject();
         if (am == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            try {
               if (this.policyRegistration == null) {
                  this.policyRegistration = this.getPolicyRegistrationFromJNDI();
               }
            } catch (Exception var11) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var11);
            }

            ejbResource.add("policyRegistration", this.policyRegistration);
            ejbResource.add("roleName", roleName);
            ejbResource.add("roleRefPermissionCheck", Boolean.TRUE);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = am.getSubjectRoles(callerSubject, sch);

            try {
               int check = am.authorize(ejbResource, callerSubject, (RoleGroup)callerRoles);
               isAuthorized = check == 1;
            } catch (Exception var10) {
               isAuthorized = false;
               PicketBoxLogger.LOGGER.debugFailureExecutingMethod("isCallerInRole", var10);
               this.authorizationAudit("Error", ejbResource, var10);
            }

            return isAuthorized;
         }
      }
   }

   public boolean isCallerInRole(String roleName, String ejbName, Principal ejbPrincipal, Subject callerSubject, String contextID, Set<SecurityRoleRef> securityRoleRefs, boolean enforceEJBRestrictions) {
      if (roleName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleName");
      } else if (ejbName == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbName");
      } else if (contextID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else {
         boolean isAuthorized = false;
         AuthorizationManager am = this.securityContext.getAuthorizationManager();
         if (am == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            HashMap map = new HashMap();

            try {
               if (this.policyRegistration == null) {
                  this.policyRegistration = this.getPolicyRegistrationFromJNDI();
               }
            } catch (Exception var17) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var17);
            }

            map.put("policyRegistration", this.policyRegistration);
            map.put("roleName", roleName);
            map.put("roleRefPermissionCheck", Boolean.TRUE);
            EJBResource ejbResource = new EJBResource(map);
            ejbResource.setPolicyContextID(contextID);
            RunAs callerRunAs = SecurityActions.getIncomingRunAs(this.securityContext);
            ejbResource.setEjbVersion(this.version);
            ejbResource.setEjbName(ejbName);
            ejbResource.setPrincipal(ejbPrincipal);
            ejbResource.setCallerRunAsIdentity(callerRunAs);
            ejbResource.setSecurityRoleReferences(securityRoleRefs);
            ejbResource.setEnforceEJBRestrictions(enforceEJBRestrictions);
            ejbResource.setCallerSubject(callerSubject);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = am.getSubjectRoles(callerSubject, sch);

            try {
               int check = am.authorize(ejbResource, callerSubject, (RoleGroup)callerRoles);
               isAuthorized = check == 1;
            } catch (Exception var16) {
               isAuthorized = false;
               PicketBoxLogger.LOGGER.debugFailureExecutingMethod("isCallerInRole", var16);
               this.authorizationAudit("Error", ejbResource, var16);
            }

            return isAuthorized;
         }
      }
   }

   public String getEJBVersion() {
      return this.version;
   }

   public void setEJBVersion(String ejbVersion) {
      if (!"1.1".equalsIgnoreCase(ejbVersion) && !"2.0".equalsIgnoreCase(ejbVersion) && !"3.0".equalsIgnoreCase(ejbVersion)) {
         throw PicketBoxMessages.MESSAGES.invalidEJBVersion(ejbVersion);
      } else {
         this.version = ejbVersion;
      }
   }

   public boolean authorize(Resource resource) throws WrongEEResourceException, MissingArgumentsException {
      if (!(resource instanceof EJBResource)) {
         throw PicketBoxMessages.MESSAGES.invalidType(EJBResource.class.getName());
      } else {
         EJBResource ejbResource = (EJBResource)resource;
         this.validateEJBResource(ejbResource);
         AuthorizationManager am = this.securityContext.getAuthorizationManager();
         if (am == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullProperty("AuthorizationManager");
         } else {
            try {
               if (this.policyRegistration == null) {
                  this.policyRegistration = this.getPolicyRegistrationFromJNDI();
               }
            } catch (Exception var10) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var10);
            }

            Subject callerSubject = ejbResource.getCallerSubject();
            ejbResource.add("policyRegistration", this.policyRegistration);
            SecurityContextCallbackHandler sch = new SecurityContextCallbackHandler(this.securityContext);
            RoleGroup callerRoles = am.getSubjectRoles(callerSubject, sch);
            boolean isAuthorized = false;

            try {
               int check = am.authorize(ejbResource, callerSubject, (RoleGroup)callerRoles);
               isAuthorized = check == 1;
               this.authorizationAudit(isAuthorized ? "Success" : "Failure", ejbResource, (Exception)null);
            } catch (Exception var9) {
               isAuthorized = false;
               PicketBoxLogger.LOGGER.debugAuthorizationError(var9);
               this.authorizationAudit("Error", ejbResource, var9);
            }

            return isAuthorized;
         }
      }
   }

   private void validateEJBResource(EJBResource ejbResource) throws MissingArgumentsException {
      if (ejbResource.getEjbName() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbName");
      } else if (ejbResource.getEjbMethod() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbMethod");
      } else if (ejbResource.getCodeSource() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("ejbCodeSource");
      } else if (ejbResource.getPolicyContextID() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else if (ejbResource.getCallerSubject() == null && ejbResource.getCallerRunAsIdentity() == null) {
         throw new MissingArgumentsException(PicketBoxMessages.MESSAGES.missingCallerInfoMessage());
      }
   }

   private PolicyRegistration getPolicyRegistrationFromJNDI() throws Exception {
      return (PolicyRegistration)(new InitialContext()).lookup(this.POLICY_REGISTRATION_JNDI);
   }
}
