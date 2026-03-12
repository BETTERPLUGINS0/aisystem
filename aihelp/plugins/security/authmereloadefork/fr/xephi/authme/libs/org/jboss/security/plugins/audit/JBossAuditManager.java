package fr.xephi.authme.libs.org.jboss.security.plugins.audit;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.SecurityUtil;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditContext;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditEvent;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditProvider;
import fr.xephi.authme.libs.org.jboss.security.audit.config.AuditProviderEntry;
import fr.xephi.authme.libs.org.jboss.security.audit.providers.LogAuditProvider;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.AuditInfo;
import fr.xephi.authme.libs.org.jboss.security.config.SecurityConfiguration;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocator;
import fr.xephi.authme.libs.org.jboss.security.plugins.ClassLoaderLocatorFactory;
import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JBossAuditManager implements AuditManager {
   private static ConcurrentHashMap<String, AuditContext> contexts = new ConcurrentHashMap();
   private static AuditContext defaultContext = null;
   private String securityDomain;

   public JBossAuditManager(String secDomain) {
      this.securityDomain = SecurityUtil.unprefixSecurityDomain(secDomain);
   }

   public AuditContext getAuditContext() throws PrivilegedActionException {
      ClassLoader moduleCL = null;
      AuditContext ac = (AuditContext)contexts.get(this.securityDomain);
      if (ac == null) {
         ac = new JBossAuditContext(this.securityDomain);
         ApplicationPolicy ap = SecurityConfiguration.getApplicationPolicy(this.securityDomain);
         if (ap != null) {
            AuditInfo ai = ap.getAuditInfo();
            if (ai != null) {
               String jbossModuleName = ai.getJBossModuleName();
               if (jbossModuleName != null) {
                  ClassLoaderLocator cll = ClassLoaderLocatorFactory.get();
                  if (cll != null) {
                     moduleCL = cll.get(jbossModuleName);
                  }
               }

               ac = this.instantiate(moduleCL, ai);
            }
         }
      }

      if (ac == null) {
         PicketBoxLogger.LOGGER.traceNoAuditContextFoundForDomain(this.securityDomain);
         ac = defaultContext;
      }

      return (AuditContext)ac;
   }

   public static AuditContext getAuditContext(String securityDomain) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(JBossAuditManager.class.getName() + ".getAuditContext"));
      }

      AuditContext ac = (AuditContext)contexts.get(securityDomain);
      if (ac == null) {
         ac = defaultContext;
      }

      return ac;
   }

   public static void addAuditContext(String securityDomain, AuditContext ac) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(JBossAuditManager.class.getName() + ".addAuditContext"));
      }

      contexts.put(securityDomain, ac);
   }

   public void audit(AuditEvent ae) {
      AuditContext ac = null;

      try {
         ac = this.getAuditContext();
      } catch (PrivilegedActionException var4) {
         throw new RuntimeException(var4);
      }

      ac.audit(ae);
      if (ac != defaultContext) {
         defaultContext.audit(ae);
      }

   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }

   private AuditContext instantiate(ClassLoader cl, AuditInfo ai) {
      AuditContext ac = new JBossAuditContext(this.securityDomain);
      AuditProviderEntry[] apeArr = ai.getAuditProviderEntry();
      List<AuditProviderEntry> list = Arrays.asList(apeArr);
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         AuditProviderEntry ape = (AuditProviderEntry)i$.next();
         String pname = ape.getName();

         try {
            Class<?> clazz = SecurityActions.loadClass(cl, pname);
            ac.addProvider((AuditProvider)clazz.newInstance());
         } catch (Exception var10) {
            throw new RuntimeException(var10);
         }
      }

      return ac;
   }

   static {
      defaultContext = new JBossAuditContext("Default_Context");
      defaultContext.addProvider(new LogAuditProvider());
   }
}
