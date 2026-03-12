package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.AnybodyPrincipal;
import fr.xephi.authme.libs.org.jboss.security.RealmMapping;
import fr.xephi.authme.libs.org.jboss.security.SubjectSecurityManager;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.MessageInfo;

public class NullSecurityManager implements SubjectSecurityManager, RealmMapping, Serializable {
   static final long serialVersionUID = -5942994627247826747L;
   private String securityDomain;

   public NullSecurityManager(String securityDomain) {
      this.securityDomain = securityDomain;
   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }

   public Subject getActiveSubject() {
      return null;
   }

   public boolean isValid(Principal principal, Object credential) {
      return true;
   }

   public boolean isValid(Principal principal, Object credential, Subject activeSubject) {
      return true;
   }

   public boolean isValid(MessageInfo requestMessage, Subject clientSubject, String layer) {
      return true;
   }

   public boolean isValid(MessageInfo requestMessage, Subject clientSubject, String layer, CallbackHandler handler) {
      return true;
   }

   public Principal getTargetPrincipal(Principal anotherDomainPrincipal, Map<String, Object> contextMap) {
      return anotherDomainPrincipal;
   }

   public Principal getPrincipal(Principal principal) {
      return principal;
   }

   public boolean doesUserHaveRole(Principal principal, Set<Principal> roleNames) {
      boolean hasRole = true;
      return hasRole;
   }

   public Set<Principal> getUserRoles(Principal principal) {
      HashSet<Principal> roles = new HashSet();
      roles.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
      return roles;
   }

   public void logout(Principal principal, Subject subject) {
   }
}
