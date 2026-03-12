package com.lenis0012.bukkit.loginsecurity.modules.storage;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import org.bukkit.command.CommandSender;

public interface StorageImport extends Runnable {
   boolean isPossible();

   static StorageImport fromSourceName(String sourceName, CommandSender sender) {
      String var2 = sourceName.toLowerCase();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -43323991:
         if (var2.equals("loginsecurity")) {
            var3 = 0;
         }
      default:
         switch(var3) {
         case 0:
            return new LoginSecurityImport((LoginSecurity)LoginSecurity.getInstance(), sender);
         default:
            return null;
         }
      }
   }
}
