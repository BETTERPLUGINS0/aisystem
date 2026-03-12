package fr.xephi.authme.libs.de.mkammerer.argon2;

import java.nio.charset.Charset;

public interface Argon2Advanced extends Argon2 {
   /** @deprecated */
   @Deprecated
   byte[] rawHash(int var1, int var2, int var3, String var4, byte[] var5);

   /** @deprecated */
   @Deprecated
   byte[] rawHash(int var1, int var2, int var3, String var4, Charset var5, byte[] var6);

   byte[] rawHash(int var1, int var2, int var3, char[] var4, byte[] var5);

   byte[] rawHash(int var1, int var2, int var3, byte[] var4, byte[] var5);

   byte[] rawHash(int var1, int var2, int var3, char[] var4, Charset var5, byte[] var6);

   byte[] pbkdf(int var1, int var2, int var3, char[] var4, Charset var5, byte[] var6, int var7);

   byte[] pbkdf(int var1, int var2, int var3, byte[] var4, byte[] var5, int var6);

   String hash(int var1, int var2, int var3, char[] var4, Charset var5, byte[] var6);

   HashResult hashAdvanced(int var1, int var2, int var3, byte[] var4, byte[] var5, int var6, Argon2Version var7);

   byte[] rawHashAdvanced(int var1, int var2, int var3, char[] var4, Charset var5, byte[] var6, byte[] var7, byte[] var8);

   byte[] rawHashAdvanced(int var1, int var2, int var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, int var8, Argon2Version var9);

   boolean verifyAdvanced(int var1, int var2, int var3, char[] var4, Charset var5, byte[] var6, byte[] var7, byte[] var8, byte[] var9);

   boolean verifyAdvanced(int var1, int var2, int var3, byte[] var4, byte[] var5, byte[] var6, byte[] var7, int var8, Argon2Version var9, byte[] var10);

   byte[] generateSalt();

   byte[] generateSalt(int var1);
}
