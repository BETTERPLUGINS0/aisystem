package fr.xephi.authme.libs.org.jboss.security.identitytrust;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.IdentityTrustInfo;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.config.IdentityTrustModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocator;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocatorFactory;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import javax.security.auth.login.LoginException;

public class JBossIdentityTrustContext extends IdentityTrustContext {
   public JBossIdentityTrustContext(String secDomain, SecurityContext sc) {
      this.securityDomain = secDomain;
      this.securityContext = sc;
   }

   public IdentityTrustManager.TrustDecision isTrusted() throws IdentityTrustException {
      IdentityTrustManager.TrustDecision decision = this.NOTAPPLICABLE;

      try {
         this.initializeModules();
      } catch (Exception var5) {
         throw new IdentityTrustException(var5);
      }

      try {
         decision = (IdentityTrustManager.TrustDecision)AccessController.doPrivileged(new PrivilegedExceptionAction<IdentityTrustManager.TrustDecision>() {
            public IdentityTrustManager.TrustDecision run() throws IdentityTrustException {
               IdentityTrustManager.TrustDecision result = JBossIdentityTrustContext.this.invokeTrusted();
               if (result == JBossIdentityTrustContext.this.PERMIT) {
                  JBossIdentityTrustContext.this.invokeCommit();
               }

               if (result == JBossIdentityTrustContext.this.DENY || result == JBossIdentityTrustContext.this.NOTAPPLICABLE) {
                  JBossIdentityTrustContext.this.invokeAbort();
               }

               return result;
            }
         });
         return decision;
      } catch (PrivilegedActionException var4) {
         Exception exc = var4.getException();
         this.invokeAbort();
         throw (IdentityTrustException)exc;
      }
   }

   private void initializeModules() throws Exception {
      ClassLoader moduleCL = null;
      this.modules.clear();
      ApplicationPolicy aPolicy = SecurityConfiguration.getApplicationPolicy(this.securityDomain);
      if (aPolicy == null) {
         throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(this.securityDomain);
      } else {
         IdentityTrustInfo iti = aPolicy.getIdentityTrustInfo();
         if (iti != null) {
            String jbossModuleName = iti.getJBossModuleName();
            if (jbossModuleName != null) {
               ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
               if (cll != null) {
                  moduleCL = cll.get(jbossModuleName);
               }
            }

            IdentityTrustModuleEntry[] itmearr = iti.getIdentityTrustModuleEntry();
            IdentityTrustModuleEntry[] arr$ = itmearr;
            int len$ = itmearr.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               IdentityTrustModuleEntry itme = arr$[i$];
               ControlFlag cf = itme.getControlFlag();
               if (cf == null) {
                  cf = ControlFlag.REQUIRED;
               }

               this.controlFlags.add(cf);
               IdentityTrustModule module = this.instantiateModule(moduleCL, itme.getName(), itme.getOptions());
               this.modules.add(module);
            }

         }
      }
   }

   private IdentityTrustModule instantiateModule(ClassLoader cl, String name, Map map) throws Exception {
      IdentityTrustModule im = null;

      try {
         Class clazz = SecurityActions.loadClass(cl, name);
         im = (IdentityTrustModule)clazz.newInstance();
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      if (im == null) {
         throw new LoginException(PicketBoxMessages.MESSAGES.failedToInstantiateClassMessage(IdentityTrustModule.class));
      } else {
         im.initialize(this.securityContext, this.callbackHandler, this.sharedState, map);
         return im;
      }
   }

   private IdentityTrustManager.TrustDecision invokeTrusted() throws IdentityTrustException {
      boolean encounteredRequiredDeny = false;
      boolean encounteredRequiredNotApplicable = false;
      boolean encounteredOptionalError = false;
      IdentityTrustException moduleException = null;
      IdentityTrustManager.TrustDecision overallDecision = IdentityTrustManager.TrustDecision.NotApplicable;
      boolean encounteredRequiredPermit = false;
      IdentityTrustManager.TrustDecision decision = this.NOTAPPLICABLE;
      int length = this.modules.size();
      if (length == 0) {
         return decision;
      } else {
         for(int i = 0; i < length; ++i) {
            IdentityTrustModule module = (IdentityTrustModule)this.modules.get(i);
            ControlFlag flag = (ControlFlag)this.controlFlags.get(i);

            try {
               decision = module.isTrusted();
            } catch (Exception var13) {
               decision = this.NOTAPPLICABLE;
               if (moduleException == null) {
                  moduleException = new IdentityTrustException(var13);
               }
            }

            if (decision == this.PERMIT) {
               overallDecision = this.PERMIT;
               if (flag == ControlFlag.REQUIRED) {
                  encounteredRequiredPermit = true;
               }

               if (flag == ControlFlag.SUFFICIENT && !encounteredRequiredDeny) {
                  return this.PERMIT;
               }
            } else if (decision == this.NOTAPPLICABLE && flag == ControlFlag.REQUIRED) {
               encounteredRequiredNotApplicable = true;
            } else {
               if (flag == ControlFlag.REQUISITE) {
                  PicketBoxLogger.LOGGER.debugRequisiteModuleFailure(module.getClass().getName());
                  if (moduleException != null) {
                     throw moduleException;
                  }

                  moduleException = new IdentityTrustException(PicketBoxMessages.MESSAGES.identityTrustValidationFailedMessage());
               }

               if (flag == ControlFlag.REQUIRED) {
                  PicketBoxLogger.LOGGER.debugRequiredModuleFailure(module.getClass().getName());
                  encounteredRequiredDeny = true;
               }

               if (flag == ControlFlag.OPTIONAL) {
                  encounteredOptionalError = true;
               }
            }
         }

         if (encounteredRequiredDeny) {
            return this.DENY;
         } else if (overallDecision == this.DENY && encounteredOptionalError) {
            return this.DENY;
         } else if (overallDecision == this.DENY) {
            return this.DENY;
         } else {
            return encounteredRequiredNotApplicable && !encounteredRequiredPermit ? this.NOTAPPLICABLE : this.PERMIT;
         }
      }
   }

   private void invokeCommit() throws IdentityTrustException {
      int length = this.modules.size();

      for(int i = 0; i < length; ++i) {
         IdentityTrustModule module = (IdentityTrustModule)this.modules.get(i);
         boolean bool = module.commit();
         if (!bool) {
            throw new IdentityTrustException(PicketBoxMessages.MESSAGES.moduleCommitFailedMessage());
         }
      }

   }

   private void invokeAbort() throws IdentityTrustException {
      int length = this.modules.size();

      for(int i = 0; i < length; ++i) {
         IdentityTrustModule module = (IdentityTrustModule)this.modules.get(i);
         boolean bool = module.abort();
         if (!bool) {
            throw new IdentityTrustException(PicketBoxMessages.MESSAGES.moduleAbortFailedMessage());
         }
      }

   }
}
