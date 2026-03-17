package me.PM2.infinitevehicles.commands;

import org.bukkit.plugin.Plugin;

public class PaperCommandManager extends BukkitCommandManager {
   private boolean brigadierAvailable;

   public PaperCommandManager(Plugin plugin) {
      super(var1);

      try {
         Class.forName("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
         var1.getServer().getPluginManager().registerEvents(new PaperAsyncTabCompleteHandler(this), var1);
      } catch (ClassNotFoundException var4) {
      }

      try {
         Class.forName("com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent");
         this.brigadierAvailable = true;
      } catch (ClassNotFoundException var3) {
      }

   }

   public void enableUnstableAPI(String api) {
      super.enableUnstableAPI(var1);
      if ("brigadier".equals(var1) && this.brigadierAvailable) {
         new PaperBrigadierManager(this.plugin, this);
      }

   }

   public synchronized CommandContexts<BukkitCommandExecutionContext> getCommandContexts() {
      if (this.contexts == null) {
         this.contexts = new PaperCommandContexts(this);
      }

      return this.contexts;
   }

   public synchronized CommandCompletions<BukkitCommandCompletionContext> getCommandCompletions() {
      if (this.completions == null) {
         this.completions = new PaperCommandCompletions(this);
      }

      return this.completions;
   }
}
