package github.nighter.smartspawner.commands.reload;

import com.mojang.brigadier.context.CommandContext;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.BaseSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ReloadSubCommand extends BaseSubCommand {
   public ReloadSubCommand(SmartSpawner plugin) {
      super(plugin);
   }

   public String getName() {
      return "reload";
   }

   public String getPermission() {
      return "smartspawner.command.reload";
   }

   public String getDescription() {
      return "Reload the plugin configuration and data";
   }

   public int execute(CommandContext<CommandSourceStack> context) {
      CommandSender sender = ((CommandSourceStack)context.getSource()).getSender();
      this.reloadAll(sender);
      return 1;
   }

   private void reloadAll(CommandSender sender) {
      try {
         this.plugin.getMessageService().sendMessage(sender, "reload_command_start");
         if (this.plugin.getConfig().getBoolean("debug", false)) {
            this.logCacheStats();
         }

         this.plugin.getSpawnerItemFactory().clearAllCaches();
         this.plugin.getMessageService().clearKeyExistsCache();
         this.plugin.reloadConfig();
         this.plugin.setUpHopperHandler();
         this.plugin.getItemPriceManager().reload();
         this.plugin.getSpawnerSettingsConfig().reload();
         this.plugin.getSpawnerManager().reloadSpawnerDropsAndConfigs();
         this.plugin.getLanguageManager().reloadLanguages();
         this.plugin.getGuiLayoutConfig().loadLayout();
         this.plugin.getSpawnerMenuUI().loadConfig();
         if (this.plugin.getSpawnerClickManager() != null) {
            this.plugin.getSpawnerClickManager().loadConfig();
         }

         this.plugin.getSpawnerGuiViewManager().recheckTimerPlaceholders();
         this.plugin.getSpawnerItemFactory().reload();
         this.plugin.getSpawnerManager().reloadAllHolograms();
         this.plugin.reload();
         if (this.plugin.getConfig().getBoolean("debug", false)) {
            this.logCacheStats();
         }

         this.plugin.getMessageService().sendMessage(sender, "reload_command_success");
      } catch (Exception var3) {
         this.plugin.getLogger().severe("Error during reload: " + var3.getMessage());
         var3.printStackTrace();
         this.plugin.getMessageService().sendMessage(sender, "reload_command_error");
      }

   }

   private void logCacheStats() {
      Map<String, Object> stats = this.plugin.getLanguageManager().getCacheStats();
      this.plugin.getLogger().info("Language cache statistics:");
      Iterator var2 = stats.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, Object> entry = (Entry)var2.next();
         Logger var10000 = this.plugin.getLogger();
         String var10001 = (String)entry.getKey();
         var10000.info("  " + var10001 + ": " + String.valueOf(entry.getValue()));
      }

   }
}
