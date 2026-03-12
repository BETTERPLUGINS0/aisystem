package com.lenis0012.bukkit.loginsecurity.hashing;

public interface HashFunction {
   String hash(String var1);

   boolean check(String var1, String var2);
}
