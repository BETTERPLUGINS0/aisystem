package fr.xephi.authme.libs.org.jboss.security.plugins.authorization;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationContext;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationModule;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.authorization.config.AuthorizationModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.authorization.modules.DelegatingAuthorizationModule;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.AuthorizationInfo;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocator;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocatorFactory;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

public class JBossAuthorizationContext extends AuthorizationContext {
   private final String EJB;
   private final String WEB;
   private Subject authenticatedSubject;
   private ApplicationPolicy applicationPolicy;

   public JBossAuthorizationContext(String name) {
      this.EJB = "jboss-ejb-policy";
      this.WEB = "jboss-web-policy";
      this.authenticatedSubject = null;
      this.applicationPolicy = null;
      this.securityDomainName = name;
   }

   public JBossAuthorizationContext(String name, CallbackHandler handler) {
      this(name);
      this.callbackHandler = handler;
   }

   public JBossAuthorizationContext(String name, Subject subject, CallbackHandler handler) {
      this(name, handler);
      this.authenticatedSubject = subject;
   }

   public void setApplicationPolicy(ApplicationPolicy appPolicy) {
      if (appPolicy == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("appPolicy");
      } else {
         AuthorizationInfo authzInfo = appPolicy.getAuthorizationInfo();
         if (authzInfo == null) {
            throw PicketBoxMessages.MESSAGES.failedToObtainInfoFromAppPolicy("AuthorizationInfo");
         } else if (!authzInfo.getName().equals(this.securityDomainName)) {
            throw PicketBoxMessages.MESSAGES.unexpectedSecurityDomainInInfo("AuthorizationInfo", this.securityDomainName);
         } else {
            this.applicationPolicy = appPolicy;
         }
      }
   }

   public int authorize(Resource resource) throws AuthorizationException {
      return this.authorize(resource, this.authenticatedSubject, (RoleGroup)resource.getMap().get("securityContextRoles"));
   }

   public int authorize(final Resource resource, Subject subject, RoleGroup callerRoles) throws AuthorizationException {
      final List<AuthorizationModule> modules = new ArrayList();
      final ArrayList controlFlags = new ArrayList();

      try {
         this.authenticatedSubject = subject;
         this.initializeModules(resource, callerRoles, modules, controlFlags);
         AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
            public Object run() throws AuthorizationException {
               int result = JBossAuthorizationContext.this.invokeAuthorize(resource, modules, controlFlags);
               if (result == 1) {
                  JBossAuthorizationContext.this.invokeCommit(modules, controlFlags);
               }

               if (result == -1) {
                  JBossAuthorizationContext.this.invokeAbort(modules, controlFlags);
                  throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
               } else {
                  return null;
               }
            }
         });
      } catch (PrivilegedActionException var11) {
         Exception exc = var11.getException();
         this.invokeAbort(modules, controlFlags);
         throw (AuthorizationException)exc;
      } finally {
         if (modules != null) {
            modules.clear();
         }

         if (controlFlags != null) {
            controlFlags.clear();
         }

      }

      return 1;
   }

   private void initializeModules(Resource resource, RoleGroup role, List<AuthorizationModule> modules, List<ControlFlag> controlFlags) throws PrivilegedActionException {
      AuthorizationInfo authzInfo = this.getAuthorizationInfo(this.securityDomainName, resource);
      if (authzInfo == null) {
         throw PicketBoxMessages.MESSAGES.failedToObtainAuthorizationInfo(this.securityDomainName);
      } else {
         ClassLoader moduleCL = null;
         String jbossModuleName = authzInfo.getJBossModuleName();
         if (jbossModuleName != null) {
            ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
            if (cll != null) {
               moduleCL = cll.get(jbossModuleName);
            }
         }

         AuthorizationModuleEntry[] entries = authzInfo.getAuthorizationModuleEntry();
         int len = entries != null ? entries.length : 0;

         for(int i = 0; i < len; ++i) {
            AuthorizationModuleEntry entry = entries[i];
            ControlFlag flag = entry.getControlFlag();
            if (flag == null) {
               flag = ControlFlag.REQUIRED;
            }

            controlFlags.add(flag);
            AuthorizationModule module = this.instantiateModule(moduleCL, entry.getPolicyModuleName(), entry.getOptions(), role);
            modules.add(module);
         }

      }
   }

   private int invokeAuthorize(Resource resource, List<AuthorizationModule> modules, List<ControlFlag> controlFlags) throws AuthorizationException {
      boolean encounteredRequiredError = false;
      boolean encounteredOptionalError = false;
      AuthorizationException moduleException = null;
      int overallDecision = -1;
      int length = modules.size();

      for(int i = 0; i < length; ++i) {
         AuthorizationModule module = (AuthorizationModule)modules.get(i);
         ControlFlag flag = (ControlFlag)controlFlags.get(i);
         boolean var12 = true;

         int decision;
         try {
            decision = module.authorize(resource);
         } catch (Exception var14) {
            decision = -1;
            if (moduleException == null) {
               moduleException = new AuthorizationException(var14.getMessage());
            }
         }

         if (decision == 1) {
            overallDecision = 1;
            if (flag == ControlFlag.SUFFICIENT && !encounteredRequiredError) {
               return 1;
            }
         } else {
            if (flag == ControlFlag.REQUISITE) {
               PicketBoxLogger.LOGGER.debugRequisiteModuleFailure(module.getClass().getName());
               if (moduleException != null) {
                  throw moduleException;
               }

               moduleException = new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
            }

            if (flag == ControlFlag.REQUIRED) {
               PicketBoxLogger.LOGGER.debugRequiredModuleFailure(module.getClass().getName());
               if (!encounteredRequiredError) {
                  encounteredRequiredError = true;
               }
            }

            if (flag == ControlFlag.OPTIONAL) {
               encounteredOptionalError = true;
            }
         }
      }

      String msg = this.getAdditionalErrorMessage(moduleException);
      if (encounteredRequiredError) {
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage() + msg);
      } else if (overallDecision == -1 && encounteredOptionalError) {
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage() + msg);
      } else if (overallDecision == -1) {
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
      } else {
         return 1;
      }
   }

   private void invokeCommit(List<AuthorizationModule> modules, List<ControlFlag> controlFlags) throws AuthorizationException {
      int length = modules.size();

      for(int i = 0; i < length; ++i) {
         AuthorizationModule module = (AuthorizationModule)modules.get(i);
         boolean bool = module.commit();
         if (!bool) {
            throw new AuthorizationException(PicketBoxMessages.MESSAGES.moduleCommitFailedMessage());
         }
      }

   }

   private void invokeAbort(List<AuthorizationModule> modules, List<ControlFlag> controlFlags) throws AuthorizationException {
      int length = modules.size();

      for(int i = 0; i < length; ++i) {
         AuthorizationModule module = (AuthorizationModule)modules.get(i);
         boolean bool = module.abort();
         if (!bool) {
            throw new AuthorizationException(PicketBoxMessages.MESSAGES.moduleAbortFailedMessage());
         }
      }

   }

   private AuthorizationModule instantiateModule(ClassLoader cl, String name, Map<String, Object> map, RoleGroup subjectRoles) throws PrivilegedActionException {
      AuthorizationModule am = null;

      try {
         Class clazz;
         try {
            if (cl == null) {
               cl = this.getClass().getClassLoader();
            }

            clazz = cl.loadClass(name);
         } catch (Exception var9) {
            ClassLoader tcl = SecurityActions.getContextClassLoader();
            clazz = tcl.loadClass(name);
         }

         am = (AuthorizationModule)clazz.newInstance();
      } catch (Exception var10) {
         PicketBoxLogger.LOGGER.debugFailureToInstantiateClass(name, var10);
      }

      if (am == null) {
         throw new IllegalStateException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(AuthorizationModule.class));
      } else {
         am.initialize(this.authenticatedSubject, this.callbackHandler, this.sharedState, map, subjectRoles);
         return am;
      }
   }

   private AuthorizationInfo getAuthorizationInfo(String domainName, Resource resource) {
      ResourceType layer = resource.getLayer();
      if (this.applicationPolicy != null) {
         return this.applicationPolicy.getAuthorizationInfo();
      } else {
         ApplicationPolicy aPolicy = SecurityConfiguration.getApplicationPolicy(domainName);
         if (aPolicy == null) {
            if (layer == ResourceType.EJB) {
               aPolicy = SecurityConfiguration.getApplicationPolicy("jboss-ejb-policy");
            } else if (layer == ResourceType.WEB) {
               aPolicy = SecurityConfiguration.getApplicationPolicy("jboss-web-policy");
            }
         }

         if (aPolicy == null) {
            throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(domainName);
         } else {
            AuthorizationInfo ai = aPolicy.getAuthorizationInfo();
            return ai == null ? this.getAuthorizationInfo(layer) : aPolicy.getAuthorizationInfo();
         }
      }
   }

   private AuthorizationInfo getAuthorizationInfo(ResourceType layer) {
      AuthorizationInfo ai = null;
      if (layer == ResourceType.EJB) {
         ai = SecurityConfiguration.getApplicationPolicy("jboss-ejb-policy").getAuthorizationInfo();
      } else if (layer == ResourceType.WEB) {
         ai = SecurityConfiguration.getApplicationPolicy("jboss-web-policy").getAuthorizationInfo();
      } else {
         ai = new AuthorizationInfo("other");
         ai.add(new AuthorizationModuleEntry(DelegatingAuthorizationModule.class.getName()));
      }

      return ai;
   }

   private String getAdditionalErrorMessage(Exception e) {
      StringBuilder msg = new StringBuilder(" ");
      if (e != null) {
         msg.append(e.getLocalizedMessage());
      }

      return msg.toString();
   }
}
