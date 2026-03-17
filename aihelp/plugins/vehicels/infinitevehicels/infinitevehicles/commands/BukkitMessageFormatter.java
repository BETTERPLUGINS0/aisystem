package me.PM2.infinitevehicles.commands;

import org.bukkit.ChatColor;

public class BukkitMessageFormatter extends MessageFormatter<ChatColor> {
   public BukkitMessageFormatter(ChatColor... colors) {
      super(var1);
   }

   String format(ChatColor color, String message) {
      return var1 + var2;
   }
}
