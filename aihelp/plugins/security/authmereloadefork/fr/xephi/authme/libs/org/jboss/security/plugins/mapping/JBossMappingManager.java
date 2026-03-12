package fr.xephi.authme.libs.org.jboss.security.plugins.mapping;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityUtil;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.MappingInfo;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingContext;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.config.MappingModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocator;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocatorFactory;
import java.util.ArrayList;

public class JBossMappingManager implements MappingManager {
   private String securityDomain;

   public JBossMappingManager(String domain) {
      this.securityDomain = SecurityUtil.unprefixSecurityDomain(domain);
   }

   public <T> MappingContext<T> getMappingContext(String mappingType) {
      ApplicationPolicy aPolicy = SecurityConfiguration.getApplicationPolicy(this.securityDomain);
      if (aPolicy == null) {
         String defaultDomain = "other";
         aPolicy = SecurityConfiguration.getApplicationPolicy(defaultDomain);
      }

      if (aPolicy == null) {
         throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(this.securityDomain);
      } else {
         MappingContext<T> mc = null;
         MappingInfo rmi = aPolicy.getMappingInfo(mappingType);
         if (rmi != null) {
            mc = this.generateMappingContext(mc, rmi);
         }

         return mc;
      }
   }

   public <T> MappingContext<T> getMappingContext(Class<T> mappingType) {
      ApplicationPolicy aPolicy = SecurityConfiguration.getApplicationPolicy(this.securityDomain);
      if (aPolicy == null) {
         String defaultDomain = "other";
         aPolicy = SecurityConfiguration.getApplicationPolicy(defaultDomain);
      }

      if (aPolicy == null) {
         throw PicketBoxMessages.MESSAGES.failedToObtainApplicationPolicy(this.securityDomain);
      } else {
         MappingContext<T> mc = null;
         MappingInfo rmi = aPolicy.getMappingInfo(mappingType);
         if (rmi != null) {
            mc = this.generateMappingContext(mc, rmi);
         }

         return mc;
      }
   }

   private <T> MappingContext<T> generateMappingContext(MappingContext<T> mc, MappingInfo rmi) {
      ClassLoader moduleCL = null;
      String jbossModuleName = rmi.getJBossModuleName();
      if (jbossModuleName != null) {
         ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
         if (cll != null) {
            moduleCL = cll.get(jbossModuleName);
         }
      }

      MappingModuleEntry[] mpe = rmi.getMappingModuleEntry();
      ArrayList<MappingProvider<T>> al = new ArrayList();

      for(int i = 0; i < mpe.length; ++i) {
         MappingProvider<T> mp = this.getMappingProvider(moduleCL, mpe[i]);
         if (mp != null) {
            al.add(mp);
         }
      }

      return new MappingContext(al);
   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }

   private <T> MappingProvider<T> getMappingProvider(ClassLoader cl, MappingModuleEntry mme) {
      MappingProvider mp = null;

      try {
         String fqn = mme.getMappingModuleName();
         Class<?> clazz = SecurityActions.loadClass(cl, fqn);
         mp = (MappingProvider)clazz.newInstance();
         mp.init(mme.getOptions());
      } catch (Exception var6) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var6);
      }

      return mp;
   }
}
