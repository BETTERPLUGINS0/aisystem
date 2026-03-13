package me.gypopo.economyshopgui.util.exceptions;

import me.gypopo.economyshopgui.files.Translatable;
import org.bukkit.ChatColor;

public class ItemLoadException extends Exception {
   public ItemLoadException(String reason) {
      super(reason);
   }

   public ItemLoadException(Translatable reason) {
      super(ChatColor.stripColor(reason.getLegacy()));
   }
}
