package com.lenis0012.bukkit.loginsecurity.hashing;

import com.lenis0012.bukkit.loginsecurity.hashing.active.BCrypt;
import com.lenis0012.bukkit.loginsecurity.hashing.authme.AuthmeSHA256;
import com.lenis0012.bukkit.loginsecurity.hashing.xauth.xAuthAlgorithm;

public enum Algorithm {
   BCRYPT(false, 7, new BCrypt()),
   xAuth_Authme_SHA256(true, 10, new AuthmeSHA256()),
   xAuth_DEFAULT(true, 11, new xAuthAlgorithm(true)),
   xAuth_WHIRLPOOL(true, 15, new xAuthAlgorithm(false)),
   AuthMe_SHA256(true, 30, new AuthmeSHA256());

   private final boolean deprecated;
   private final BasicAlgorithm algorithm;
   private final int id;

   private Algorithm(boolean deprecated, int id, BasicAlgorithm algorithm) {
      this.deprecated = deprecated;
      this.algorithm = algorithm;
      this.id = id;
   }

   public boolean isDeprecated() {
      return this.deprecated;
   }

   public int getId() {
      return this.id;
   }

   public String hash(String password) {
      return this.algorithm.hash(password);
   }

   public boolean check(String password, String hashed) {
      return this.algorithm.check(password, hashed);
   }

   public static Algorithm getById(int id) {
      Algorithm[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Algorithm algorithm = var1[var3];
         if (algorithm.id == id) {
            return algorithm;
         }
      }

      return null;
   }

   // $FF: synthetic method
   private static Algorithm[] $values() {
      return new Algorithm[]{BCRYPT, xAuth_Authme_SHA256, xAuth_DEFAULT, xAuth_WHIRLPOOL, AuthMe_SHA256};
   }
}
