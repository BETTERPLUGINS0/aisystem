package fr.xephi.authme.libs.org.jboss.security.config;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityConfiguration {
   private static final Map<String, ApplicationPolicy> appPolicies = new ConcurrentHashMap();
   private static String cipherAlgorithm;
   private static int iterationCount;
   private static String salt;
   private static String keyStoreType;
   private static String keyStoreURL;
   private static String keyStorePass;
   private static String trustStoreType;
   private static String trustStorePass;
   private static String trustStoreURL;
   private static Key cipherKey;
   private static AlgorithmParameterSpec cipherSpec;
   private static boolean deepCopySubjectMode;

   public static void addApplicationPolicy(ApplicationPolicy applicationPolicy) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".addApplicationPolicy"));
      }

      if (applicationPolicy == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("applicationPolicy");
      } else {
         appPolicies.put(applicationPolicy.getName(), applicationPolicy);
      }
   }

   public static void removeApplicationPolicy(String name) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".removeApplicationPolicy"));
      }

      appPolicies.remove(name);
   }

   public static ApplicationPolicy getApplicationPolicy(String policyName) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getApplicationPolicy"));
      }

      return (ApplicationPolicy)appPolicies.get(policyName);
   }

   public static String getCipherAlgorithm() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getCipherAlgorithm"));
      }

      return cipherAlgorithm;
   }

   public static void setCipherAlgorithm(String ca) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setCipherAlgorithm"));
      }

      cipherAlgorithm = ca;
   }

   public static Key getCipherKey() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getCipherKey"));
      }

      return cipherKey;
   }

   public static void setCipherKey(Key ca) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setCipherKey"));
      }

      cipherKey = ca;
   }

   public static AlgorithmParameterSpec getCipherSpec() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getCipherSpec"));
      }

      return cipherSpec;
   }

   public static void setCipherSpec(AlgorithmParameterSpec aps) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setCipherSpec"));
      }

      cipherSpec = aps;
   }

   public static int getIterationCount() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getIterationCount"));
      }

      return iterationCount;
   }

   public static void setIterationCount(int count) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setIterationCount"));
      }

      iterationCount = count;
   }

   public static String getSalt() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getSalt"));
      }

      return salt;
   }

   public static void setSalt(String s) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setSalt"));
      }

      salt = s;
   }

   public static String getKeyStoreType() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getKeyStoreType"));
      }

      return keyStoreType;
   }

   public static void setKeyStoreType(String type) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setKeyStoreType"));
      }

      keyStoreType = type;
   }

   public static String getKeyStoreURL() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getKeyStoreURL"));
      }

      return keyStoreURL;
   }

   public static void setKeyStoreURL(String storeURL) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setKeyStoreURL"));
      }

      keyStoreURL = storeURL;
   }

   public static String getKeyStorePass() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getKeyStorePass"));
      }

      return keyStorePass;
   }

   public static void setKeyStorePass(String password) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setKeyStorePass"));
      }

      keyStorePass = password;
   }

   public static String getTrustStoreType() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getTrustStoreType"));
      }

      return trustStoreType;
   }

   public static void setTrustStoreType(String type) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setTrustStoreType"));
      }

      trustStoreType = type;
   }

   public static String getTrustStorePass() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getTrustStorePass"));
      }

      return trustStorePass;
   }

   public static void setTrustStorePass(String password) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setTrustStorePass"));
      }

      trustStorePass = password;
   }

   public static String getTrustStoreURL() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".getTrustStoreURL"));
      }

      return trustStoreURL;
   }

   public static void setTrustStoreURL(String storeURL) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setTrustStoreURL"));
      }

      trustStoreURL = storeURL;
   }

   public static boolean isDeepCopySubjectMode() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".isDeepCopySubjectMode"));
      }

      return deepCopySubjectMode;
   }

   public static void setDeepCopySubjectMode(boolean dcsm) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityConfiguration.class.getName() + ".setDeepCopySubjectMode"));
      }

      deepCopySubjectMode = dcsm;
   }
}
