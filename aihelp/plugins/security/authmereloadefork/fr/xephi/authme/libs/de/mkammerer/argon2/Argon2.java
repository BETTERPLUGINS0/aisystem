package fr.xephi.authme.libs.de.mkammerer.argon2;

import java.nio.charset.Charset;

public interface Argon2 {
   /** @deprecated */
   @Deprecated
   String hash(int var1, int var2, int var3, String var4);

   /** @deprecated */
   @Deprecated
   String hash(int var1, int var2, int var3, String var4, Charset var5);

   String hash(int var1, int var2, int var3, char[] var4);

   String hash(int var1, int var2, int var3, char[] var4, Charset var5);

   String hash(int var1, int var2, int var3, byte[] var4);

   /** @deprecated */
   @Deprecated
   boolean verify(String var1, String var2);

   /** @deprecated */
   @Deprecated
   boolean verify(String var1, String var2, Charset var3);

   boolean verify(String var1, char[] var2);

   boolean verify(String var1, char[] var2, Charset var3);

   boolean verify(String var1, byte[] var2);

   void wipeArray(char[] var1);

   void wipeArray(byte[] var1);

   boolean needsRehash(String var1, int var2, int var3, int var4);
}
