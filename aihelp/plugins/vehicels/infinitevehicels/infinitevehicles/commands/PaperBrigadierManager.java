package me.PM2.infinitevehicles.commands;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/** @deprecated */
@Deprecated
@UnstableAPI
public class PaperBrigadierManager implements Listener {
   private final PaperCommandManager manager;
   private final ACFBrigadierManager<BukkitBrigadierCommandSource> brigadierManager;

   public PaperBrigadierManager(Plugin plugin, PaperCommandManager manager) {
      var2.verifyUnstableAPI("brigadier");
      var2.log(LogLevel.INFO, "Enabled Brigadier Support!");
      this.manager = var2;
      this.brigadierManager = new ACFBrigadierManager(var2);
      Bukkit.getPluginManager().registerEvents(this, var1);
   }

   @EventHandler
   public void onCommandRegister(CommandRegisteredEvent<BukkitBrigadierCommandSource> event) {
      RootCommand var2 = this.manager.getRootCommand(var1.getCommandLabel());
      if (var2 != null) {
         var1.setLiteral(this.brigadierManager.register(var2, var1.getLiteral(), var1.getBrigadierCommand(), var1.getBrigadierCommand(), this::checkPermRoot, this::checkPermSub));
      }

   }

   private boolean checkPermSub(RegisteredCommand registeredCommand, BukkitBrigadierCommandSource sender) {
      return var1.hasPermission(this.manager.getCommandIssuer(var2.getBukkitSender()));
   }

   private boolean checkPermRoot(RootCommand rootCommand, BukkitBrigadierCommandSource sender) {
      return var1.hasAnyPermission(this.manager.getCommandIssuer(var2.getBukkitSender()));
   }
}
