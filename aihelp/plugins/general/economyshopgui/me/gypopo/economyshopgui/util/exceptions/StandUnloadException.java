package me.gypopo.economyshopgui.util.exceptions;

import me.gypopo.economyshopgui.files.Translatable;
import org.bukkit.ChatColor;

public class StandUnloadException extends Exception {
   public StandUnloadException(String reason) {
      super(reason);
   }

   public StandUnloadException(Translatable reason) {
      super(ChatColor.stripColor(reason.getLegacy()));
   }
}
