package fr.xephi.authme.libs.de.rtner.security.auth.spi;

public interface PRF {
   void init(byte[] var1);

   byte[] doFinal(byte[] var1);

   int getHLen();
}
