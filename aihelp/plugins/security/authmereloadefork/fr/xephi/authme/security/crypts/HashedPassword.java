package fr.xephi.authme.security.crypts;

public class HashedPassword {
   private final String hash;
   private final String salt;

   public HashedPassword(String hash, String salt) {
      this.hash = hash;
      this.salt = salt;
   }

   public HashedPassword(String hash) {
      this(hash, (String)null);
   }

   public String getHash() {
      return this.hash;
   }

   public String getSalt() {
      return this.salt;
   }
}
