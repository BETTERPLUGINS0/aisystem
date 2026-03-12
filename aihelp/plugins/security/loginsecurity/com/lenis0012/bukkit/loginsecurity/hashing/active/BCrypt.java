package com.lenis0012.bukkit.loginsecurity.hashing.active;

import com.lenis0012.bukkit.loginsecurity.hashing.BasicAlgorithm;
import com.lenis0012.bukkit.loginsecurity.hashing.lib.BCryptLib;

public class BCrypt extends BasicAlgorithm {
   public String hash(String pw) {
      String salt = BCryptLib.gensalt();
      return BCryptLib.hashpw(pw, salt);
   }

   public boolean check(String pw, String hashed) {
      return BCryptLib.checkpw(pw, hashed);
   }
}
