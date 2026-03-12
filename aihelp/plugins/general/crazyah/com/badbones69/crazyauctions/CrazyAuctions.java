package com.badbones69.crazyauctions;

import com.badbones69.crazyauctions.api.CrazyManager;
import com.badbones69.crazyauctions.api.enums.Files;
import com.badbones69.crazyauctions.api.enums.Messages;
import com.badbones69.crazyauctions.api.support.MetricsWrapper;
import com.badbones69.crazyauctions.commands.AuctionCommand;
import com.badbones69.crazyauctions.commands.AuctionTab;
import com.badbones69.crazyauctions.controllers.GuiListener;
import com.badbones69.crazyauctions.controllers.MarcoListener;
import com.badbones69.crazyauctions.currency.VaultSupport;
import java.util.Base64;
import java.util.Iterator;
import libs.com.ryderbelserion.vital.paper.Vital;
import libs.com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CrazyAuctions extends Vital {
   private CrazyManager crazyManager;
   private VaultSupport support;

   @NotNull
   public static CrazyAuctions get() {
      return (CrazyAuctions)JavaPlugin.getPlugin(CrazyAuctions.class);
   }

   public void onEnable() {
      if (!this.getServer().getPluginManager().isPluginEnabled("Vault")) {
         this.getLogger().severe("Vault was not found so the plugin will now disable.");
         this.getServer().getPluginManager().disablePlugin(this);
      } else {
         this.getFileManager().addFile("config.yml").addFile("data.yml").addFile("messages.yml").init();
         this.crazyManager = new CrazyManager();
         FileConfiguration configuration = Files.data.getConfiguration();
         Iterator var2;
         String key;
         ItemStack itemStack;
         String uuid;
         OfflinePlayer player;
         if (configuration.contains("OutOfTime/Cancelled")) {
            var2 = configuration.getConfigurationSection("OutOfTime/Cancelled").getKeys(false).iterator();

            while(var2.hasNext()) {
               key = (String)var2.next();
               itemStack = configuration.getItemStack("OutOfTime/Cancelled." + key + ".Item");
               if (itemStack != null) {
                  configuration.set("OutOfTime/Cancelled." + key + ".Item", Base64.getEncoder().encodeToString(itemStack.serializeAsBytes()));
                  Files.data.save();
               }

               uuid = configuration.getString("OutOfTime/Cancelled." + key + ".Seller");
               if (uuid != null) {
                  player = Methods.getOfflinePlayer(uuid);
                  configuration.set("OutOfTime/Cancelled." + key + ".Seller", player.getUniqueId().toString());
                  Files.data.save();
               }
            }
         }

         if (configuration.contains("Items")) {
            var2 = configuration.getConfigurationSection("Items").getKeys(false).iterator();

            while(var2.hasNext()) {
               key = (String)var2.next();
               itemStack = configuration.getItemStack("Items." + key + ".Item");
               if (itemStack != null) {
                  configuration.set("Items." + key + ".Item", Base64.getEncoder().encodeToString(itemStack.serializeAsBytes()));
                  Files.data.save();
               }

               uuid = configuration.getString("Items." + key + ".Seller");
               if (uuid != null) {
                  player = Methods.getOfflinePlayer(uuid);
                  if (!uuid.equals(player.getUniqueId().toString())) {
                     configuration.set("Items." + key + ".Seller", player.getUniqueId().toString());
                     Files.data.save();
                  }
               }

               String bidder = configuration.getString("Items." + key + ".TopBidder");
               if (bidder != null && !bidder.equals("None")) {
                  OfflinePlayer player = Methods.getOfflinePlayer(bidder);
                  if (!bidder.equals(player.getUniqueId().toString())) {
                     configuration.set("Items." + key + ".TopBidder", player.getUniqueId().toString());
                     Files.data.save();
                  }
               }
            }
         }

         this.crazyManager.load();
         this.getServer().getPluginManager().registerEvents(new GuiListener(), this);
         this.getServer().getPluginManager().registerEvents(new MarcoListener(), this);
         this.registerCommand(this.getCommand("crazyauctions"), new AuctionTab(), new AuctionCommand());
         this.support = new VaultSupport();
         this.support.setupEconomy();
         (new FoliaRunnable(this, this.getServer().getGlobalRegionScheduler()) {
            public void run() {
               Methods.updateAuction();
            }
         }).runAtFixedRate(this, 0L, 5000L);
         Messages.addMissingMessages();
         new MetricsWrapper(this, 4624);
      }
   }

   private void registerCommand(PluginCommand pluginCommand, TabCompleter tabCompleter, CommandExecutor commandExecutor) {
      if (pluginCommand != null) {
         pluginCommand.setExecutor(commandExecutor);
         if (tabCompleter != null) {
            pluginCommand.setTabCompleter(tabCompleter);
         }
      }

   }

   public void onDisable() {
      if (this.crazyManager != null) {
         this.crazyManager.unload();
      }

   }

   public final VaultSupport getSupport() {
      return this.support;
   }

   public final CrazyManager getCrazyManager() {
      return this.crazyManager;
   }
}
