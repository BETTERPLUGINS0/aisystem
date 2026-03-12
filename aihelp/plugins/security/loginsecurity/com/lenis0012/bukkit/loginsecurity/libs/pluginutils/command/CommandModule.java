package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.command;

import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.ModularPlugin;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;

public class CommandModule extends Module<ModularPlugin> {
   public CommandModule(ModularPlugin plugin) {
      super(plugin);
   }

   public void enable() {
   }

   public void disable() {
   }

   public void registerCommand(Command command, String... aliases) {
      String[] var3 = aliases;
      int var4 = aliases.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String alias = var3[var5];
         this.plugin.getCommand(alias).setExecutor(command);
      }

   }
}
