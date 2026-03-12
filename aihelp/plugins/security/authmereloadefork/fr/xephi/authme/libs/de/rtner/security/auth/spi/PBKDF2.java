package fr.xephi.authme.libs.de.rtner.security.auth.spi;

public interface PBKDF2 {
   byte[] deriveKey(String var1);

   byte[] deriveKey(String var1, int var2);

   boolean verifyKey(String var1);

   PBKDF2Parameters getParameters();

   void setParameters(PBKDF2Parameters var1);

   PRF getPseudoRandomFunction();

   void setPseudoRandomFunction(PRF var1);
}
