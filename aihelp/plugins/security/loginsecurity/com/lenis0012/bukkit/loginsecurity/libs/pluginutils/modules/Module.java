package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

public abstract class Module<T extends ModularPlugin> {
   protected T plugin;
   protected boolean enabled;
   boolean local;

   public Module(T plugin) {
      this.plugin = plugin;
   }

   public abstract void enable();

   public abstract void disable();

   public void reload() {
   }

   public List<Class<? extends Module>> getRequiredModules() {
      return Lists.newArrayList();
   }

   protected void register(Listener listener) {
      Bukkit.getPluginManager().registerEvents(listener, this.plugin);
   }

   protected void register(CommandExecutor command, String... cmds) {
      String[] var3 = cmds;
      int var4 = cmds.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String cmd = var3[var5];
         this.plugin.getCommand(cmd).setExecutor(command);
      }

   }

   public Logger logger() {
      return this.plugin.getLogger();
   }
}
