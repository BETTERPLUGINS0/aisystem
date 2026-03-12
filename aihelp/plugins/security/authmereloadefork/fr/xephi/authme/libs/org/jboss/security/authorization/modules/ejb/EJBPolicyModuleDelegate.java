package fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.RunAsIdentity;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.AuthorizationModuleDelegate;
import fr.xephi.authme.libs.org.jboss.security.authorization.resources.EJBResource;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import fr.xephi.authme.libs.org.jboss.security.javaee.SecurityRoleRef;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;

public class EJBPolicyModuleDelegate extends AuthorizationModuleDelegate {
   protected String ejbName = null;
   protected Method ejbMethod = null;
   protected Principal ejbPrincipal = null;
   private RoleGroup methodRoles = null;
   private String methodInterface = null;
   protected RunAs callerRunAs = null;
   protected String roleName = null;
   private Boolean roleRefCheck;
   protected Set<SecurityRoleRef> securityRoleReferences;
   private final Role ANYBODY_ROLE;
   protected boolean ejbRestrictions;

   public EJBPolicyModuleDelegate() {
      this.roleRefCheck = Boolean.FALSE;
      this.securityRoleReferences = null;
      this.ANYBODY_ROLE = new SimpleRole("<ANYBODY>");
      this.ejbRestrictions = false;
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
            this.roleName = (String)map.get("roleName");
            this.roleRefCheck = (Boolean)map.get("roleRefPermissionCheck");
            this.callerRunAs = ejbResource.getCallerRunAsIdentity();
            this.ejbMethod = ejbResource.getEjbMethod();
            this.ejbName = ejbResource.getEjbName();
            this.ejbPrincipal = ejbResource.getPrincipal();
            this.methodInterface = ejbResource.getEjbMethodInterface();
            this.methodRoles = ejbResource.getEjbMethodRoles();
            this.securityRoleReferences = ejbResource.getSecurityRoleReferences();
            this.ejbRestrictions = ejbResource.isEnforceEJBRestrictions();
            return this.roleRefCheck == Boolean.TRUE ? this.checkRoleRef(role) : this.process(role);
         }
      }
   }

   private int process(RoleGroup principalRole) {
      boolean allowed = true;
      if (this.ejbMethod == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullProperty("ejbMethod");
      } else {
         String method;
         if (this.methodRoles == null) {
            method = this.ejbMethod.getName();
            PicketBoxLogger.LOGGER.traceNoMethodPermissions(method, this.methodInterface);
            return -1;
         } else {
            PicketBoxLogger.LOGGER.debugEJBPolicyModuleDelegateState(this.ejbMethod.getName(), this.methodInterface, this.methodRoles.toString());
            if (!this.methodRoles.containsAll(this.ANYBODY_ROLE)) {
               if (this.callerRunAs == null) {
                  if (principalRole == null) {
                     throw PicketBoxMessages.MESSAGES.invalidNullProperty("principalRole");
                  }

                  if (!this.methodRoles.containsAtleastOneRole(principalRole)) {
                     method = this.ejbMethod.getName();
                     PicketBoxLogger.LOGGER.debugInsufficientMethodPermissions(this.ejbPrincipal, this.ejbName, method, this.methodInterface, this.methodRoles.toString(), principalRole.toString(), (String)null);
                     allowed = false;
                  }
               } else if (this.callerRunAs instanceof RunAsIdentity) {
                  RunAsIdentity callerRunAsIdentity = (RunAsIdentity)this.callerRunAs;
                  RoleGroup srg = new SimpleRoleGroup(callerRunAsIdentity.getRunAsRoles());
                  if (!srg.containsAtleastOneRole(this.methodRoles)) {
                     String method = this.ejbMethod.getName();
                     PicketBoxLogger.LOGGER.debugInsufficientMethodPermissions(this.ejbPrincipal, this.ejbName, method, this.methodInterface, this.methodRoles.toString(), (String)null, callerRunAsIdentity.getRunAsRoles().toString());
                     allowed = false;
                  }
               }
            }

            return allowed ? 1 : -1;
         }
      }
   }

   protected int checkRoleRef(RoleGroup principalRole) {
      if (this.ejbPrincipal == null && this.callerRunAs == null) {
         return -1;
      } else {
         boolean matchFound = false;
         Iterator it = this.securityRoleReferences.iterator();

         while(it.hasNext()) {
            SecurityRoleRef meta = (SecurityRoleRef)it.next();
            if (meta.getName().equals(this.roleName)) {
               this.roleName = meta.getLink();
               matchFound = true;
               break;
            }
         }

         if (!matchFound && this.ejbRestrictions) {
            throw PicketBoxMessages.MESSAGES.noMatchingRoleFoundInDescriptor(this.roleName);
         } else {
            Role deploymentrole = new SimpleRole(this.roleName);
            boolean allowed = false;
            if (this.callerRunAs == null) {
               allowed = principalRole.containsRole(deploymentrole);
            } else if (this.callerRunAs instanceof RunAsIdentity) {
               RunAsIdentity callerRunAsIdentity = (RunAsIdentity)this.callerRunAs;
               SimpleRoleGroup srg = new SimpleRoleGroup(callerRunAsIdentity.getRunAsRoles());
               allowed = srg.containsRole(deploymentrole);
            }

            return allowed ? 1 : -1;
         }
      }
   }
}
