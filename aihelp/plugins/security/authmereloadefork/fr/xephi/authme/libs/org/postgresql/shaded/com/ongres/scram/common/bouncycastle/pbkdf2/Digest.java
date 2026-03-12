package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2;

public interface Digest {
   String getAlgorithmName();

   int getDigestSize();

   void update(byte var1);

   void update(byte[] var1, int var2, int var3);

   int doFinal(byte[] var1, int var2);

   void reset();
}
