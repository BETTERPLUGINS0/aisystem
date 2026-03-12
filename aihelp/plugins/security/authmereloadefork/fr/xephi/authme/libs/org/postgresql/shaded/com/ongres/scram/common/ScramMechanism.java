package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparation;

public interface ScramMechanism {
   String getName();

   byte[] digest(byte[] var1) throws RuntimeException;

   byte[] hmac(byte[] var1, byte[] var2) throws RuntimeException;

   int algorithmKeyLength();

   boolean supportsChannelBinding();

   byte[] saltedPassword(StringPreparation var1, String var2, byte[] var3, int var4);
}
