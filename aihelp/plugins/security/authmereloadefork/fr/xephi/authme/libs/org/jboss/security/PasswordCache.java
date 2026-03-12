package fr.xephi.authme.libs.org.jboss.security;

public interface PasswordCache {
   boolean contains(String var1, long var2);

   char[] getPassword(String var1);

   void storePassword(String var1, char[] var2);

   void reset();
}
