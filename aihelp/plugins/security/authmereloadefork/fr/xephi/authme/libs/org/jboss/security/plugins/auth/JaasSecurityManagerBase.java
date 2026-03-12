package fr.xephi.authme.libs.org.jboss.security.plugins.auth;

import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RealmMapping;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextAssociation;
import fr.xephi.authme.libs.org.jboss.security.SecurityUtil;
import fr.xephi.authme.libs.org.jboss.security.SubjectSecurityManager;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.JBossCallbackHandler;
import fr.xephi.authme.libs.org.jboss.security.auth.login.BaseAuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocator;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocatorFactory;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class JaasSecurityManagerBase implements SubjectSecurityManager, RealmMapping {
   private String securityDomain;
   private CallbackHandler handler;
   private transient Method setSecurityInfo;
   private boolean deepCopySubjectOption;
   private AuthorizationManager authorizationManager;

   public JaasSecurityManagerBase() {
      this("other", new JBossCallbackHandler());
   }

   public JaasSecurityManagerBase(String securityDomain, CallbackHandler handler) {
      this.deepCopySubjectOption = false;
      this.securityDomain = SecurityUtil.unprefixSecurityDomain(securityDomain);
      this.handler = handler;
      (new StringBuilder()).append(this.getClass().getName()).append('.').append(securityDomain).toString();
      Class[] sig = new Class[]{Principal.class, Object.class};

      try {
         this.setSecurityInfo = handler.getClass().getMethod("setSecurityInfo", sig);
      } catch (Exception var6) {
         throw new UndeclaredThrowableException(var6, PicketBoxMessages.MESSAGES.unableToFindSetSecurityInfoMessage());
      }
   }

   public void setDeepCopySubjectOption(Boolean flag) {
      this.deepCopySubjectOption = flag;
   }

   public void setAuthorizationManager(AuthorizationManager authorizationManager) {
      this.authorizationManager = authorizationManager;
   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }

   public Subject getActiveSubject() {
      Subject subj = null;
      SecurityContext sc = SecurityContextAssociation.getSecurityContext();
      if (sc != null) {
         subj = sc.getUtil().getSubject();
      }

      return subj;
   }

   public boolean isValid(Principal principal, Object credential) {
      return this.isValid(principal, credential, (Subject)null);
   }

   public boolean isValid(Principal principal, Object credential, Subject activeSubject) {
      PicketBoxLogger.LOGGER.traceBeginIsValid(principal, (String)null);
      boolean isValid = false;
      if (!isValid) {
         isValid = this.authenticate(principal, credential, activeSubject);
      }

      PicketBoxLogger.LOGGER.traceEndIsValid(isValid);
      return isValid;
   }

   public Principal getPrincipal(Principal principal) {
      return principal;
   }

   public boolean doesUserHaveRole(Principal principal, Set<Principal> rolePrincipals) {
      if (this.authorizationManager == null) {
         this.authorizationManager = SecurityUtil.getAuthorizationManager(this.securityDomain, "java:jboss/jaas/");
      }

      if (this.authorizationManager == null) {
         PicketBoxLogger.LOGGER.debugNullAuthorizationManager(this.securityDomain);
         return false;
      } else {
         return this.authorizationManager.doesUserHaveRole(principal, rolePrincipals);
      }
   }

   public Set<Principal> getUserRoles(Principal principal) {
      if (this.authorizationManager == null) {
         this.authorizationManager = SecurityUtil.getAuthorizationManager(this.securityDomain, "java:jboss/jaas/");
      }

      if (this.authorizationManager == null) {
         PicketBoxLogger.LOGGER.debugNullAuthorizationManager(this.securityDomain);
         return null;
      } else {
         return this.authorizationManager.getUserRoles(principal);
      }
   }

   public Principal getTargetPrincipal(Principal anotherDomainPrincipal, Map<String, Object> contextMap) {
      throw new UnsupportedOperationException();
   }

   private boolean authenticate(Principal principal, Object credential, Subject theSubject) {
      ApplicationPolicy theAppPolicy = SecurityConfiguration.getApplicationPolicy(this.securityDomain);
      if (theAppPolicy != null) {
         BaseAuthenticationInfo authInfo = theAppPolicy.getAuthenticationInfo();
         String jbossModuleName = authInfo.getJBossModuleName();
         if (jbossModuleName != null) {
            ClassLoader currentTccl = SubjectActions.getContextClassLoader();
            ClassLoaderLocator theCLL = ClassLoaderLocatorFactory.get();
            if (theCLL != null) {
               ClassLoader newTCCL = theCLL.get(jbossModuleName);
               if (newTCCL != null) {
                  boolean var10;
                  try {
                     SubjectActions.setContextClassLoader(newTCCL);
                     var10 = this.proceedWithJaasLogin(principal, credential, theSubject);
                  } finally {
                     SubjectActions.setContextClassLoader(currentTccl);
                  }

                  return var10;
               }
            }
         }
      }

      return this.proceedWithJaasLogin(principal, credential, theSubject);
   }

   private boolean proceedWithJaasLogin(Principal principal, Object credential, Subject theSubject) {
      Subject subject = null;
      boolean authenticated = false;
      LoginException authException = null;

      try {
         LoginContext lc = this.defaultLogin(principal, credential);
         subject = lc.getSubject();
         if (subject != null) {
            if (theSubject != null) {
               SubjectActions.copySubject(subject, theSubject, false, this.deepCopySubjectOption);
            }

            authenticated = true;
         }
      } catch (LoginException var8) {
         if (principal != null && principal.getName() != null) {
            PicketBoxLogger.LOGGER.debugFailedLogin(var8);
         }

         authException = var8;
      }

      SubjectActions.setContextInfo("fr.xephi.authme.libs.org.jboss.security.exception", authException);
      return authenticated;
   }

   private LoginContext defaultLogin(Principal principal, Object credential) throws LoginException {
      Object[] securityInfo = new Object[]{principal, credential};
      CallbackHandler theHandler = null;

      LoginException lc;
      try {
         theHandler = (CallbackHandler)this.handler.getClass().newInstance();
         this.setSecurityInfo.invoke(theHandler, securityInfo);
      } catch (Throwable var7) {
         lc = new LoginException(PicketBoxMessages.MESSAGES.unableToFindSetSecurityInfoMessage());
         lc.initCause(var7);
         throw lc;
      }

      Subject subject = new Subject();
      lc = null;
      PicketBoxLogger.LOGGER.traceDefaultLoginPrincipal(principal);
      LoginContext lc = SubjectActions.createLoginContext(this.securityDomain, subject, theHandler);
      lc.login();
      PicketBoxLogger.LOGGER.traceDefaultLoginSubject(lc.toString(), SubjectActions.toString(subject));
      return lc;
   }

   public void logout(Principal principal, Subject subject) {
      if (subject == null) {
         subject = new Subject();
      }

      LoginContext context = null;
      Object[] securityInfo = new Object[]{principal, null};
      CallbackHandler theHandler = null;

      try {
         theHandler = (CallbackHandler)this.handler.getClass().newInstance();
         this.setSecurityInfo.invoke(theHandler, securityInfo);
         context = SubjectActions.createLoginContext(this.securityDomain, subject, theHandler);
      } catch (Throwable var9) {
         LoginException le = new LoginException(PicketBoxMessages.MESSAGES.unableToInitializeLoginContext(var9));
         le.initCause(var9);
         SubjectActions.setContextInfo("fr.xephi.authme.libs.org.jboss.security.exception", le);
         return;
      }

      try {
         context.logout();
         PicketBoxLogger.LOGGER.traceLogoutSubject(context.toString(), SubjectActions.toString(subject));
      } catch (LoginException var8) {
         SubjectActions.setContextInfo("fr.xephi.authme.libs.org.jboss.security.exception", var8);
      }

   }
}
