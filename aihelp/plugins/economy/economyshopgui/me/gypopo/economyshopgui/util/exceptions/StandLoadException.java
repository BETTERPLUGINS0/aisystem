package me.gypopo.economyshopgui.util.exceptions;

import me.gypopo.economyshopgui.files.Translatable;
import org.bukkit.ChatColor;

public class StandLoadException extends Exception {
   public StandLoadException(String reason) {
      super(reason);
   }

   public StandLoadException(Translatable reason) {
      super(ChatColor.stripColor(reason.getLegacy()));
   }
}
