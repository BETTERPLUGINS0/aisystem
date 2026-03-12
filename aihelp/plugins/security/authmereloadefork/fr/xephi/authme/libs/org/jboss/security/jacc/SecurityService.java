package fr.xephi.authme.libs.org.jboss.security.jacc;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.Policy;
import java.security.PrivilegedAction;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.security.jacc.PolicyContext;

public class SecurityService {
   private static final String JACC_POLICY_PROVIDER = "javax.security.jacc.policy.provider";
   private Policy oldPolicy;
   private Policy jaccPolicy;
   private ObjectName policyName;
   private String policyAttributeName = "Policy";
   private MBeanServer server;

   public ObjectName getPolicyName() {
      return this.policyName;
   }

   public void setPolicyName(ObjectName policyName) {
      this.policyName = policyName;
   }

   public String getPolicyAttributeName() {
      return this.policyAttributeName;
   }

   public void setPolicyAttributeName(String policyAttributeName) {
      this.policyAttributeName = policyAttributeName;
   }

   public MBeanServer getMBeanServer() {
      return this.server;
   }

   public void setMBeanServer(MBeanServer server) {
      this.server = server;
   }

   public Policy getPolicy() {
      return this.jaccPolicy;
   }

   public void setPolicy(Policy jaccPolicy) {
      this.jaccPolicy = jaccPolicy;
   }

   public void start() throws Exception {
      this.oldPolicy = Policy.getPolicy();
      if (this.server != null && this.policyName != null && this.server.isRegistered(this.policyName)) {
         try {
            this.jaccPolicy = (Policy)this.server.getAttribute(this.policyName, this.policyAttributeName);
         } catch (Exception var8) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var8);
         }
      }

      if (this.jaccPolicy == null) {
         String provider = getProperty("javax.security.jacc.policy.provider", "fr.xephi.authme.libs.org.jboss.security.jacc.DelegatingPolicy");
         ClassLoader loader = SecurityActions.getContextClassLoader();
         Class providerClass = loader.loadClass(provider);

         try {
            Class<?>[] ctorSig = new Class[]{Policy.class};
            Constructor<?> ctor = providerClass.getConstructor(ctorSig);
            Object[] ctorArgs = new Object[]{this.oldPolicy};
            this.jaccPolicy = (Policy)ctor.newInstance(ctorArgs);
         } catch (NoSuchMethodException var7) {
            this.jaccPolicy = (Policy)providerClass.newInstance();
         }
      }

      Policy.setPolicy(this.jaccPolicy);
      this.jaccPolicy.refresh();
      SubjectPolicyContextHandler handler = new SubjectPolicyContextHandler();
      PolicyContext.registerHandler("javax.security.auth.Subject.container", handler, true);
   }

   public void stop() throws Exception {
      if (this.jaccPolicy != null) {
         Policy.setPolicy(this.oldPolicy);
      }

   }

   static String getProperty(String name) {
      return getProperty(name, (String)null);
   }

   static String getProperty(String name, String defaultValue) {
      PrivilegedAction<?> action = new SecurityService.PropertyAccessAction(name, defaultValue);
      String property = (String)AccessController.doPrivileged(action);
      return property;
   }

   static class PropertyAccessAction implements PrivilegedAction<String> {
      private String name;
      private String defaultValue;

      PropertyAccessAction(String name, String defaultValue) {
         this.name = name;
         this.defaultValue = defaultValue;
      }

      public String run() {
         return System.getProperty(this.name, this.defaultValue);
      }
   }
}
