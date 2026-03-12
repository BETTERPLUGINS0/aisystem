package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.AnybodyPrincipal;
import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.NobodyPrincipal;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityRolesAssociation;
import fr.xephi.authme.libs.org.jboss.security.SecurityUtil;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationContext;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.callbacks.SecurityContextCallback;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingContext;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingType;
import fr.xephi.authme.libs.org.jboss.security.plugins.authorization.JBossAuthorizationContext;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;

public class JBossAuthorizationManager implements AuthorizationManager {
   private final String securityDomain;
   private AuthorizationContext authorizationContext = null;
   private final Lock lock = new ReentrantLock();

   public JBossAuthorizationManager(String securityDomainName) {
      this.securityDomain = SecurityUtil.unprefixSecurityDomain(securityDomainName);
   }

   public int authorize(Resource resource) throws AuthorizationException {
      this.validateResource(resource);
      Subject subject = SubjectActions.getActiveSubject();
      return this.internalAuthorization(resource, subject, (RoleGroup)null);
   }

   public int authorize(Resource resource, Subject subject) throws AuthorizationException {
      return this.internalAuthorization(resource, subject, (RoleGroup)null);
   }

   public int authorize(Resource resource, Subject subject, RoleGroup role) throws AuthorizationException {
      this.validateResource(resource);
      return this.internalAuthorization(resource, subject, role);
   }

   public int authorize(Resource resource, Subject subject, Group roleGroup) throws AuthorizationException {
      this.validateResource(resource);
      return this.internalAuthorization(resource, subject, this.getRoleGroup(roleGroup));
   }

   public boolean doesUserHaveRole(Principal principal, Set<Principal> rolePrincipals) {
      boolean hasRole = false;
      RoleGroup roles = this.getCurrentRoles(principal);
      PicketBoxLogger.LOGGER.traceBeginDoesUserHaveRole(principal, roles != null ? roles.toString() : "");
      if (roles != null) {
         Principal role;
         for(Iterator iter = rolePrincipals.iterator(); !hasRole && iter.hasNext(); hasRole = this.doesRoleGroupHaveRole(role, roles)) {
            role = (Principal)iter.next();
         }

         PicketBoxLogger.LOGGER.traceEndDoesUserHaveRole(hasRole);
      }

      return hasRole;
   }

   public boolean doesUserHaveRole(Principal principal, Principal role) {
      boolean hasRole = false;
      RoleGroup roles = this.getCurrentRoles(principal);
      hasRole = this.doesRoleGroupHaveRole(role, roles);
      return hasRole;
   }

   public Set<Principal> getUserRoles(Principal principal) {
      RoleGroup userRoles = this.getCurrentRoles(principal);
      return this.getRolesAsSet(userRoles);
   }

   protected boolean doesRoleGroupHaveRole(Principal role, RoleGroup userRoles) {
      if (role instanceof NobodyPrincipal) {
         return false;
      } else {
         boolean isMember = userRoles.containsRole(new SimpleRole(role.getName()));
         if (!isMember) {
            isMember = role instanceof AnybodyPrincipal;
         }

         return isMember;
      }
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("[AuthorizationManager:class=").append(this.getClass().getName());
      buf.append(":").append(this.securityDomain).append(":");
      buf.append("]");
      return buf.toString();
   }

   public void setAuthorizationContext(AuthorizationContext authorizationContext) {
      if (authorizationContext == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("authorizationContext");
      } else {
         String sc = authorizationContext.getSecurityDomain();
         if (!this.securityDomain.equals(sc)) {
            throw PicketBoxMessages.MESSAGES.unexpectedSecurityDomainInContext(this.securityDomain);
         } else {
            this.lock.lock();

            try {
               this.authorizationContext = authorizationContext;
            } finally {
               this.lock.unlock();
            }

         }
      }
   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }

   public Group getTargetRoles(Principal targetPrincipal, Map<String, Object> contextMap) {
      throw new UnsupportedOperationException();
   }

   private HashSet<Principal> getRolesAsSet(RoleGroup roles) {
      HashSet<Principal> userRoles = null;
      if (roles != null) {
         userRoles = new HashSet();
         List<Role> rolesList = roles.getRoles();
         Iterator i$ = rolesList.iterator();

         while(i$.hasNext()) {
            Role r = (Role)i$.next();
            userRoles.add(new SimplePrincipal(r.getRoleName()));
         }
      }

      return userRoles;
   }

   public RoleGroup getSubjectRoles(Subject authenticatedSubject, CallbackHandler cbh) {
      if (authenticatedSubject == null) {
         return null;
      } else {
         SecurityContextCallback scb = new SecurityContextCallback();

         try {
            cbh.handle(new Callback[]{scb});
         } catch (Exception var8) {
            throw new RuntimeException(var8);
         }

         SecurityContext sc = scb.getSecurityContext();
         Principal callerPrincipal = null;
         RunAs callerRunAs = sc.getIncomingRunAs();
         if (callerRunAs != null) {
            callerPrincipal = new SimplePrincipal(callerRunAs.getName());
         }

         RoleGroup roles = this.getCurrentRoles(callerPrincipal, authenticatedSubject, sc);
         if (roles == null) {
            roles = new SimpleRoleGroup("Roles");
         }

         return (RoleGroup)roles;
      }
   }

   private RoleGroup getCurrentRoles(Principal principal) {
      Subject subject = SubjectActions.getActiveSubject();
      SecurityContext sc = SubjectActions.getSecurityContext();
      if (sc == null) {
         sc = new JBossSecurityContext(this.securityDomain);
         SubjectActions.setSecurityContext((SecurityContext)sc);
      }

      return this.getCurrentRoles(principal, subject, (SecurityContext)sc);
   }

   private RoleGroup getCurrentRoles(Principal principal, Subject subject, SecurityContext securityContext) {
      if (subject == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("subject");
      } else if (securityContext == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityContext");
      } else {
         Group subjectRoles = this.getGroupFromSubject(subject);
         boolean emptyContextRoles = false;
         RoleGroup userRoles = securityContext.getUtil().getRoles();
         if (userRoles == null || "true".equalsIgnoreCase(SubjectActions.getRefreshSecurityContextRoles())) {
            emptyContextRoles = true;
         }

         userRoles = this.copyGroups(userRoles, subjectRoles);
         if (subjectRoles != userRoles || emptyContextRoles) {
            MappingManager mm = securityContext.getMappingManager();
            MappingContext<RoleGroup> mc = mm.getMappingContext(MappingType.ROLE.name());
            RoleGroup mappedUserRoles = userRoles;
            if (mc != null && mc.hasModules()) {
               Map<String, Object> contextMap = new HashMap();
               contextMap.put("Roles", userRoles);
               if (principal != null) {
                  contextMap.put("Principal", principal);
               }

               contextMap.put("deploymentPrincipalRolesMap", SecurityRolesAssociation.getSecurityRoles());
               contextMap.put("PrincipalsSet", subject.getPrincipals());
               PicketBoxLogger.LOGGER.traceRolesBeforeMapping(userRoles != null ? userRoles.toString() : "");
               if (userRoles == null) {
                  userRoles = this.getEmptyRoleGroup();
               }

               mc.performMapping(contextMap, userRoles);
               mappedUserRoles = (RoleGroup)mc.getMappingResult().getMappedObject();
               PicketBoxLogger.LOGGER.traceRolesAfterMapping(userRoles.toString());
            }

            securityContext.getData().put("Roles", mappedUserRoles);
         }

         if (securityContext.getUtil().getRoles() == null) {
            securityContext.getUtil().setRoles(userRoles);
         }

         return userRoles;
      }
   }

   private RoleGroup copyGroups(RoleGroup source, Group toCopy) {
      if (toCopy == null) {
         return source;
      } else {
         if (source == null && toCopy != null) {
            source = this.getEmptyRoleGroup();
         }

         Enumeration en = toCopy.members();

         while(en.hasMoreElements()) {
            source.addRole(new SimpleRole(((Principal)en.nextElement()).getName()));
         }

         return source;
      }
   }

   private int internalAuthorization(Resource resource, Subject subject, RoleGroup role) throws AuthorizationException {
      if (this.authorizationContext == null) {
         this.setAuthorizationContext(new JBossAuthorizationContext(this.securityDomain));
      }

      return this.authorizationContext.authorize(resource, subject, role);
   }

   private Group getGroupFromSubject(Subject theSubject) {
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

   private RoleGroup getRoleGroup(Group roleGroup) {
      if (roleGroup == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("roleGroup");
      } else {
         SimpleRoleGroup srg = new SimpleRoleGroup(roleGroup.getName());
         Enumeration principals = roleGroup.members();

         while(principals.hasMoreElements()) {
            srg.addRole(new SimpleRole(((Principal)principals.nextElement()).getName()));
         }

         return srg;
      }
   }

   private void validateResource(Resource resource) {
      if (resource == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource");
      } else if (resource.getMap() == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource.contextMap");
      }
   }

   private RoleGroup getEmptyRoleGroup() {
      return new SimpleRoleGroup("Roles");
   }
}
