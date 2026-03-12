package fr.xephi.authme.libs.org.jboss.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExternalPasswordCache implements PasswordCache {
   private static final ExternalPasswordCache PASSWORD_CACHE = new ExternalPasswordCache();
   private Map<String, PasswordRecord> cache = Collections.synchronizedMap(new HashMap());
   private MessageDigest md5Digest = null;

   private ExternalPasswordCache() {
      try {
         this.md5Digest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException var2) {
         PicketBoxLogger.LOGGER.errorCannotGetMD5AlgorithmInstance();
      }

   }

   public static ExternalPasswordCache getExternalPasswordCacheInstance() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(ExternalPasswordCache.class.getName() + ".getExternalPasswordCacheInstance"));
      }

      return PASSWORD_CACHE;
   }

   public boolean contains(String key, long timeOut) {
      String transformedKey = this.transformKey(key);
      PasswordRecord pr = (PasswordRecord)this.cache.get(transformedKey);
      return pr != null && (timeOut == 0L || System.currentTimeMillis() - pr.timeOut < timeOut);
   }

   public char[] getPassword(String key) {
      String newKey = this.transformKey(key);
      PicketBoxLogger.LOGGER.traceRetrievingPasswordFromCache(newKey);
      PasswordRecord pr = (PasswordRecord)this.cache.get(newKey);
      return pr.password;
   }

   public void storePassword(String key, char[] password) {
      String newKey = this.transformKey(key);
      PicketBoxLogger.LOGGER.traceStoringPasswordToCache(newKey);
      PasswordRecord pr = new PasswordRecord();
      pr.timeOut = System.currentTimeMillis();
      pr.password = password;
      this.cache.put(newKey, pr);
   }

   private String transformKey(String key) {
      String newKey = key;
      if (this.md5Digest != null) {
         this.md5Digest.reset();
         byte[] bt = key.getBytes();
         byte[] md5 = this.md5Digest.digest(bt);
         newKey = new String(Base64Utils.tob64(md5));
      }

      return newKey;
   }

   public int getCachedPasswordsCount() {
      return this.cache.size();
   }

   public void reset() {
      PicketBoxLogger.LOGGER.traceResettingCache();
      this.cache.clear();
   }
}
