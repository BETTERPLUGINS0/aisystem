package fr.xephi.authme.libs.org.jboss.security.cache;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.Subject;

public class JBossAuthenticationCache implements SecurityCache<Principal> {
   private int initialCapacity = 16;
   private float loadFactor = 0.75F;
   private int concurrencyLevel = 16;
   private ConcurrentHashMap<Principal, JBossAuthenticationCache.AuthCacheObject> cacheMap = null;

   public JBossAuthenticationCache() {
      this.constructCache();
   }

   public JBossAuthenticationCache(int initCapacity, float loadFactor, int level) {
      this.concurrencyLevel = level;
      this.loadFactor = loadFactor;
      this.initialCapacity = initCapacity;
      this.constructCache();
   }

   public void addCacheEntry(Principal principal, Map<String, Object> map) throws SecurityCacheException {
      try {
         JBossAuthenticationCache.AuthCacheObject ao = new JBossAuthenticationCache.AuthCacheObject(map.get("Credential"), (Subject)map.get("Subject"));
         this.cacheMap.put(principal, ao);
      } catch (Exception var4) {
         throw new SecurityCacheException(var4);
      }
   }

   public boolean cacheHit(Principal principal) {
      return this.cacheMap.containsKey(principal);
   }

   public void cacheOperation(Principal principal, Map<String, Object> map) throws SecurityCacheException {
      boolean isValid = false;
      if (!this.cacheHit(principal)) {
         throw new SecurityCacheException(PicketBoxMessages.MESSAGES.cacheMissMessage());
      } else {
         Object cred = map.get("Credential");
         JBossAuthenticationCache.AuthCacheObject ao = (JBossAuthenticationCache.AuthCacheObject)this.cacheMap.get(principal);
         Object cacheCred = ao.credential;
         if (cred != null && cacheCred != null) {
            char[] a2;
            char[] a1;
            if (cacheCred.getClass().isAssignableFrom(cred.getClass())) {
               if (cacheCred instanceof Comparable) {
                  Comparable c = (Comparable)cacheCred;
                  isValid = c.compareTo(cred) == 0;
               } else if (cacheCred instanceof char[]) {
                  a1 = (char[])((char[])cacheCred);
                  a2 = (char[])((char[])cred);
                  isValid = Arrays.equals(a1, a2);
               } else if (cacheCred instanceof byte[]) {
                  byte[] a1 = (byte[])((byte[])cacheCred);
                  byte[] a2 = (byte[])((byte[])cred);
                  isValid = Arrays.equals(a1, a2);
               } else if (cacheCred.getClass().isArray()) {
                  Object[] a1 = (Object[])((Object[])cacheCred);
                  Object[] a2 = (Object[])((Object[])cred);
                  isValid = Arrays.equals(a1, a2);
               } else {
                  isValid = cacheCred.equals(cred);
               }
            } else if (cacheCred instanceof char[] && cred instanceof String) {
               a1 = (char[])((char[])cacheCred);
               a2 = ((String)cred).toCharArray();
               isValid = Arrays.equals(a1, a2);
            } else if (cacheCred instanceof String && cred instanceof char[]) {
               a1 = ((String)cacheCred).toCharArray();
               a2 = (char[])((char[])cred);
               isValid = Arrays.equals(a1, a2);
            }
         } else if (cred == null && cacheCred == null) {
            isValid = true;
         }

         if (!isValid) {
            throw new SecurityCacheException(PicketBoxMessages.MESSAGES.cacheValidationFailedMessage());
         }
      }
   }

   public <Y> Y get(Principal key) throws SecurityCacheException {
      Subject subj = null;
      if (this.cacheHit(key)) {
         JBossAuthenticationCache.AuthCacheObject aco = (JBossAuthenticationCache.AuthCacheObject)this.cacheMap.get(key);
         subj = aco.subject;
      }

      return subj;
   }

   private void constructCache() {
      this.cacheMap = new ConcurrentHashMap(this.initialCapacity, this.loadFactor, this.concurrencyLevel);
   }

   private class AuthCacheObject {
      private Object credential;
      private Subject subject;

      public AuthCacheObject(Object credential, Subject subject) {
         this.credential = credential;
         this.subject = subject;
      }
   }
}
