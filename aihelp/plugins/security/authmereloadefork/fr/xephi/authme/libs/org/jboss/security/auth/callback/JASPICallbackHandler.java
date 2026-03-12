package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.IdentityFactory;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRoleGroup;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;

public class JASPICallbackHandler extends JBossCallbackHandler {
   private static final long serialVersionUID = 1L;

   public JASPICallbackHandler() {
   }

   public JASPICallbackHandler(Principal principal, Object credential) {
      super(principal, credential);
   }

   protected void handleCallBack(Callback callback) throws UnsupportedCallbackException {
      SecurityContext currentSC;
      Subject currentSubject;
      Subject currentSubject;
      if (callback instanceof GroupPrincipalCallback) {
         GroupPrincipalCallback groupPrincipalCallback = (GroupPrincipalCallback)callback;
         currentSC = SecurityActions.getCurrentSecurityContext();
         if (currentSC == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullSecurityContext();
         }

         String[] rolesArray = groupPrincipalCallback.getGroups();
         int sizeOfRoles = rolesArray != null ? rolesArray.length : 0;
         if (sizeOfRoles > 0) {
            List<Role> rolesList = new ArrayList();

            for(int i = 0; i < sizeOfRoles; ++i) {
               Role role = new SimpleRole(rolesArray[i]);
               rolesList.add(role);
            }

            RoleGroup roles = new SimpleRoleGroup("Roles", rolesList);
            RoleGroup currentRoles = currentSC.getUtil().getRoles();
            if (currentRoles != null) {
               currentRoles.addAll(roles.getRoles());
            } else {
               currentSC.getUtil().setRoles(roles);
            }
         }

         currentSubject = groupPrincipalCallback.getSubject();
         if (currentSubject != null) {
            currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
            if (currentSubject != null) {
               currentSubject.getPrincipals().addAll(currentSubject.getPrincipals());
               currentSubject.getPublicCredentials().addAll(currentSubject.getPublicCredentials());
               currentSubject.getPrivateCredentials().addAll(currentSubject.getPrivateCredentials());
            }

            currentSC.getSubjectInfo().setAuthenticatedSubject(currentSubject);
         }
      } else if (callback instanceof CallerPrincipalCallback) {
         CallerPrincipalCallback callerPrincipalCallback = (CallerPrincipalCallback)callback;
         currentSC = SecurityActions.getCurrentSecurityContext();
         Subject subject = callerPrincipalCallback.getSubject();
         if (currentSC == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullSecurityContext();
         }

         if (subject != null) {
            Subject currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
            if (currentSubject != null) {
               subject.getPrincipals().addAll(currentSubject.getPrincipals());
               subject.getPublicCredentials().addAll(currentSubject.getPublicCredentials());
               subject.getPrivateCredentials().addAll(currentSubject.getPrivateCredentials());
            }

            currentSC.getSubjectInfo().setAuthenticatedSubject(subject);
         }

         Principal callerPrincipal = callerPrincipalCallback.getPrincipal();
         if (callerPrincipal == null && callerPrincipalCallback.getName() != null) {
            callerPrincipal = new SimplePrincipal(callerPrincipalCallback.getName());
         }

         if (callerPrincipal != null) {
            currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
            if (currentSubject != null) {
               currentSubject.getPrincipals().add(callerPrincipal);
            }

            Identity principalBasedIdentity = IdentityFactory.getIdentity((Principal)callerPrincipal, (Object)null);
            currentSC.getSubjectInfo().addIdentity(principalBasedIdentity);
         }
      } else if (callback instanceof PasswordValidationCallback) {
         PasswordValidationCallback passwordValidationCallback = (PasswordValidationCallback)callback;
         currentSC = SecurityActions.getCurrentSecurityContext();
         if (currentSC == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullSecurityContext();
         }

         String userName = passwordValidationCallback.getUsername();
         char[] password = passwordValidationCallback.getPassword();
         currentSubject = passwordValidationCallback.getSubject();
         if (currentSubject != null) {
            currentSubject = currentSC.getSubjectInfo().getAuthenticatedSubject();
            if (currentSubject != null) {
               currentSubject.getPrincipals().addAll(currentSubject.getPrincipals());
               currentSubject.getPublicCredentials().addAll(currentSubject.getPublicCredentials());
               currentSubject.getPrivateCredentials().addAll(currentSubject.getPrivateCredentials());
            }

            currentSC.getSubjectInfo().setAuthenticatedSubject(currentSubject);
            Identity identity = IdentityFactory.getIdentity(new SimplePrincipal(userName), password);
            currentSC.getSubjectInfo().addIdentity(identity);
         }
      } else {
         super.handleCallBack(callback);
      }

   }
}
