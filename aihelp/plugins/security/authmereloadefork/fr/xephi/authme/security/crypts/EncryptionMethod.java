package fr.xephi.authme.security.crypts;

public interface EncryptionMethod {
   HashedPassword computeHash(String var1, String var2);

   String computeHash(String var1, String var2, String var3);

   boolean comparePassword(String var1, HashedPassword var2, String var3);

   String generateSalt();

   boolean hasSeparateSalt();
}
