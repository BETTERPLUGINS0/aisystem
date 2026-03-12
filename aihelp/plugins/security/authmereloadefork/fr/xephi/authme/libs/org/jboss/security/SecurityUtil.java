package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Set;
import javax.naming.InitialContext;
import javax.security.auth.Subject;

public class SecurityUtil {
   private static String LEGACY_JAAS_CONTEXT_ROOT = "java:/jaas/";

   public static String unprefixSecurityDomain(String securityDomain) {
      String result = null;
      if (securityDomain != null) {
         if (securityDomain.startsWith("java:jboss/jaas/")) {
            result = securityDomain.substring("java:jboss/jaas/".length());
         } else if (securityDomain.startsWith("java:jboss/jbsx/")) {
            result = securityDomain.substring("java:jboss/jbsx/".length());
         } else if (securityDomain.startsWith(LEGACY_JAAS_CONTEXT_ROOT)) {
            result = securityDomain.substring(LEGACY_JAAS_CONTEXT_ROOT.length());
         } else {
            result = securityDomain;
         }
      }

      return result;
   }

   public static Group getSubjectRoles(Subject theSubject) {
      if (theSubject == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("theSubject");
      } else {
         Set<Group> subjectGroups = theSubject.getPrincipals(Group.class);
         Iterator<Group> iter = subjectGroups.iterator();
         Group roles = null;

         while(iter.hasNext()) {
            Group grp = (Group)iter.next();
            String name = grp.getName();
            if (name.equals("Roles")) {
               roles = grp;
            }
         }

         return roles;
      }
   }

   public static ApplicationPolicy getApplicationPolicy(String domainName) {
      return SecurityConfiguration.getApplicationPolicy(domainName);
   }

   public static AuthenticationManager getAuthenticationManager(String securityDomain, String baseContext) {
      String securityMgrURL = "/securityMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext)) {
         lookupURL = securityDomain + securityMgrURL;
      } else {
         lookupURL = baseContext + "/" + securityDomain + securityMgrURL;
      }

      AuthenticationManager am = null;

      try {
         InitialContext ic = new InitialContext();
         am = (AuthenticationManager)ic.lookup(lookupURL);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      return am;
   }

   public static AuthorizationManager getAuthorizationManager(String securityDomain, String baseContext) {
      String authorizationMgrURL = "/authorizationMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext)) {
         lookupURL = securityDomain + authorizationMgrURL;
      } else {
         lookupURL = baseContext + "/" + securityDomain + authorizationMgrURL;
      }

      AuthorizationManager am = null;

      try {
         InitialContext ic = new InitialContext();
         am = (AuthorizationManager)ic.lookup(lookupURL);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      return am;
   }

   public static AuditManager getAuditManager(String securityDomain, String baseContext) {
      String auditMgrURL = "/auditMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext)) {
         lookupURL = securityDomain + auditMgrURL;
      } else {
         lookupURL = baseContext + "/" + securityDomain + auditMgrURL;
      }

      AuditManager am = null;

      try {
         InitialContext ic = new InitialContext();
         am = (AuditManager)ic.lookup(lookupURL);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      return am;
   }

   public static IdentityTrustManager getIdentityTrustManager(String securityDomain, String baseContext) {
      String identityTrustMgrURL = "/identityTrustMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext)) {
         lookupURL = securityDomain + identityTrustMgrURL;
      } else {
         lookupURL = baseContext + "/" + securityDomain + identityTrustMgrURL;
      }

      IdentityTrustManager am = null;

      try {
         InitialContext ic = new InitialContext();
         am = (IdentityTrustManager)ic.lookup(lookupURL);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      return am;
   }

   public static MappingManager getMappingManager(String securityDomain, String baseContext) {
      String mappingManagerURL = "/mappingMgr";
      String lookupURL = null;
      if (securityDomain.startsWith(baseContext)) {
         lookupURL = securityDomain + mappingManagerURL;
      } else {
         lookupURL = baseContext + "/" + securityDomain + mappingManagerURL;
      }

      MappingManager am = null;

      try {
         InitialContext ic = new InitialContext();
         am = (MappingManager)ic.lookup(lookupURL);
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      return am;
   }

   public static PolicyRegistration getPolicyRegistration() {
      String lookupURL = "java:/policyRegistration";
      PolicyRegistration registration = null;

      try {
         InitialContext ic = new InitialContext();
         registration = (PolicyRegistration)ic.lookup(lookupURL);
      } catch (Exception var3) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var3);
      }

      return registration;
   }
}
