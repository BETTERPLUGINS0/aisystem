package fr.xephi.authme.security.crypts;

import fr.xephi.authme.security.HashUtils;

public class Md5vB extends HexSaltedMethod {
   public String computeHash(String password, String salt, String name) {
      return "$MD5vb$" + salt + "$" + HashUtils.md5(HashUtils.md5(password) + salt);
   }

   public boolean comparePassword(String password, HashedPassword hashedPassword, String name) {
      String hash = hashedPassword.getHash();
      String[] line = hash.split("\\$");
      return line.length == 4 && HashUtils.isEqual(hash, this.computeHash(password, line[2], name));
   }

   public int getSaltLength() {
      return 16;
   }
}
