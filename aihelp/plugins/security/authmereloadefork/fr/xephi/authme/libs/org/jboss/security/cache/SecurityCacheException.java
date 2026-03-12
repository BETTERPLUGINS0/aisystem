package fr.xephi.authme.libs.org.jboss.security.cache;

public class SecurityCacheException extends Exception {
   private static final long serialVersionUID = 1L;

   public SecurityCacheException() {
   }

   public SecurityCacheException(String message, Throwable t) {
      super(message, t);
   }

   public SecurityCacheException(String message) {
      super(message);
   }

   public SecurityCacheException(Throwable t) {
      super(t);
   }
}
