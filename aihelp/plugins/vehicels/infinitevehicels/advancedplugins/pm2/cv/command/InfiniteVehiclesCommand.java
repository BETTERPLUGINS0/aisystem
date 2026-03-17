package advancedplugins.pm2.cv.command;

import advancedplugins.pm2.cv.InfiniteVehiclesPlugin;
import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.AdminLogs;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.configuration.LeaderboardGuiConfiguration;
import advancedplugins.pm2.cv.api.item.ClickableItems;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.upgrade.Upgrade;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.upgrade.UpgradeTier;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleController;
import advancedplugins.pm2.cv.menu.VehiclesMenu;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.ModelGenerator;
import advancedplugins.pm2.cv.models.core.ModelAPIImpl;
import advancedplugins.pm2.cv.util.ItemUtil;
import advancedplugins.pm2.cv.util.math.ClampUtil;
import advancedplugins.pm2.cv.vehicle.GuiSubHandler;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import me.PM2.infinitevehicles.commands.BaseCommand;
import me.PM2.infinitevehicles.commands.annotation.CommandAlias;
import me.PM2.infinitevehicles.commands.annotation.CommandCompletion;
import me.PM2.infinitevehicles.commands.annotation.CommandPermission;
import me.PM2.infinitevehicles.commands.annotation.HelpCommand;
import me.PM2.infinitevehicles.commands.annotation.Optional;
import me.PM2.infinitevehicles.commands.annotation.Subcommand;
import me.PM2.infinitevehicles.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@CommandAlias("infinitevehicles|iv|infv|infinitev|ivehicles")
public class InfiniteVehiclesCommand extends BaseCommand {
   private static boolean reloading = false;

   @Subcommand("reload")
   @CommandPermission("infinitevehicles.command.reload")
   public void reload(CommandSender sender) {
      String var10001;
      if (reloading) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.PLUGIN_RELOADING.value());
      } else {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.RELOADING_PLUGIN.value());
         long var2 = System.currentTimeMillis();
         InfiniteVehiclesPlugin var4 = InfiniteVehiclesPlugin.getInstance();
         reloading = true;
         Configuration.load(var4);
         LangConfiguration.load(var4);

         try {
            GuiConfiguration.load(var4);
         } catch (InvalidConfigurationException var10) {
            var10.printStackTrace();
         }

         try {
            LeaderboardGuiConfiguration.load(var4);
         } catch (InvalidConfigurationException var9) {
            var9.printStackTrace();
         }

         try {
            AdminLogs.load(var4);
         } catch (InvalidConfigurationException var8) {
            var8.printStackTrace();
         }

         var4.extractExamples();
         Constants.Files.mkdirs();
         Registries.reload();
         var4.loadRecipes();
         InfiniteVehicles.getVehicleHandler().getRegisteredVehicles().forEach((var0) -> {
            var0.getVehicleControllers().forEach(VehicleController::loadProperties);
         });
         ModelGenerator var5 = ModelAPIImpl.getModelGenerator();
         if (var5 != null) {
            var5.importModels(false);
            var5.queueTask(ModelGenerator.Phase.POST_IMPORT, () -> {
               long var3 = System.currentTimeMillis() - var2;
               String var10001 = LangConfiguration.PREFIX.value();
               var1.sendMessage(var10001 + LangConfiguration.RELOADED_PLUGIN.value().replace("%s", var3.makeConcatWithConstants<invokedynamic>(var3)));
               reloading = false;
            });
         } else {
            long var6 = System.currentTimeMillis() - var2;
            var10001 = LangConfiguration.PREFIX.value();
            var1.sendMessage(var10001 + LangConfiguration.RELOADED_PLUGIN.value().replace("%s", var6.makeConcatWithConstants<invokedynamic>(var6)));
            reloading = false;
         }
      }
   }

   @Subcommand("getupgrade")
   @CommandPermission("infinitevehicles.command.getupgrade")
   public void getUpgrade(Player player, String upgradeId, String upgradeType, String tier, @Optional Integer amount) {
      if (var5 == null) {
         var5 = 1;
      }

      if (var5 > 64) {
         var5 = 64;
      }

      UpgradeConfiguration var6 = (UpgradeConfiguration)Registries.getRegistry(UpgradeConfiguration.class).get(var2);
      String var10001;
      if (var6 == null) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.COMMAND_UNKNOWN_UPGRADE.value());
      } else {
         Upgrade var7 = (Upgrade)var6.getUpgrades().stream().filter((var1x) -> {
            return var1x.getId().equals(var3);
         }).findFirst().orElse((Object)null);
         if (var7 == null) {
            var10001 = LangConfiguration.PREFIX.value();
            var1.sendMessage(var10001 + LangConfiguration.UNKNOWN_UPGRADE_TYPE.value());
         } else {
            UpgradeTier var8 = (UpgradeTier)var7.getUpgradeTiers().get(var4);
            if (var8 == null) {
               var10001 = LangConfiguration.PREFIX.value();
               var1.sendMessage(var10001 + LangConfiguration.UNKNOWN_UPGRADE_TIER.value());
            } else {
               ItemStack var9 = GuiSubHandler.buildIcon(var8.getPhysicalItem(), var8).clone();
               ItemMeta var10 = var9.getItemMeta();
               if (var10 == null) {
                  var10 = Bukkit.getItemFactory().getItemMeta(var9.getType());
               }

               if (var10 == null) {
                  var10001 = LangConfiguration.PREFIX.value();
                  var1.sendMessage(var10001 + LangConfiguration.NO_ITEM_META.value());
               } else {
                  var10001 = var6.getId();
                  ClickableItems.setClickableItem(var9, "upgrade_" + var10001 + "_" + var7.getId() + "_" + var4);

                  for(int var11 = 0; var11 < var5; ++var11) {
                     var1.getInventory().addItem(new ItemStack[]{var9.clone()});
                  }

               }
            }
         }
      }
   }

   @Subcommand("leaderboard|lb")
   @CommandPermission("infinitevehicles.command.leaderboard")
   public void leaderboard(Player player, String vehicleID) {
      (new LeaderboardCommand()).leaderboard(var1, var2);
   }

   @Subcommand("menu")
   @CommandPermission("infinitevehicles.command.menu")
   public void vehiclesMenu(CommandSender sender, @Optional OnlinePlayer targetPlayer) {
      Player var3;
      if (var2 == null && var1 instanceof Player) {
         var3 = (Player)var1;
      } else {
         var3 = var2 == null ? null : var2.getPlayer();
      }

      if (var3 == null) {
         String var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.UNKNOWN_PLAYER.value());
      } else {
         (new VehiclesMenu()).open(var3);
      }
   }

   @Subcommand("give|get")
   @CommandCompletion("@items|all")
   @CommandPermission("infinitevehicles.command.get")
   public void give(CommandSender sender, String itemType, @Optional OnlinePlayer targetPlayer, @Optional Integer amount) {
      Player var5;
      if (var3 == null && var1 instanceof Player) {
         var5 = (Player)var1;
      } else {
         var5 = var3 == null ? null : var3.getPlayer();
      }

      String var10001;
      if (var5 == null) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.UNKNOWN_PLAYER.value());
      } else {
         if (var4 == null) {
            var4 = 1;
         }

         if (var4 > 64) {
            var4 = 64;
         }

         ItemConfiguration var6 = (ItemConfiguration)Registries.getRegistry(ItemConfiguration.class).get(var2);
         if (var6 == null) {
            var6 = (ItemConfiguration)Registries.getRegistry(VehicleConfiguration.class).getEntries().stream().filter((var1x) -> {
               return var1x.getId().equalsIgnoreCase(var2) && var1x.getPickupItem() != null;
            }).map(VehicleConfiguration::getPickupItem).findFirst().orElse((Object)null);
         }

         if (!var2.equalsIgnoreCase("everything") && !var2.equalsIgnoreCase("all")) {
            if (var6 == null) {
               var10001 = LangConfiguration.PREFIX.value();
               var1.sendMessage(var10001 + LangConfiguration.COMMAND_UNKNOWN_ITEM.value());
            } else {
               ItemStack var8 = var6.getItemStack();
               var8 = var8.clone();
               var4 = ClampUtil.clamp(var4, 1, var8.getMaxStackSize());
               var8.setAmount(var4);
               ItemUtil.setPDCInt(var8, Constants.NamespacedKeys.LEGAL_ITEM_AMOUNT, var4);
               var5.getInventory().addItem(new ItemStack[]{var8});
               var10001 = LangConfiguration.PREFIX.value();
               var1.sendMessage(var10001 + String.format(LangConfiguration.ITEM_SUCCESS_GIVE.value(), var4, var2, var5.getName()));
            }
         } else {
            int var7 = var4;
            Registries.getRegistry(ItemConfiguration.class).getEntries().forEach((var2x) -> {
               ItemStack var3 = var2x.getItemStack();
               var3 = var3.clone();
               int var4 = ClampUtil.clamp(var7, 1, var3.getMaxStackSize());
               var3.setAmount(var7);
               ItemUtil.setPDCInt(var3, Constants.NamespacedKeys.LEGAL_ITEM_AMOUNT, var4);
               var5.getInventory().addItem(new ItemStack[]{var3});
            });
            var10001 = LangConfiguration.PREFIX.value();
            var1.sendMessage(var10001 + String.format(LangConfiguration.ITEM_SUCCESS_GIVE.value(), var4, "everything", var5.getName()));
         }
      }
   }

   @Subcommand("spawn")
   @CommandPermission("infinitevehicles.command.spawn")
   @CommandCompletion("@vehicles")
   public void spawn(CommandSender sender, String vehicleID, @Optional Location location) {
      var3 = var3 == null && var1 instanceof Player ? ((Player)var1).getLocation() : var3;
      String var10001;
      if (var3 == null) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.COMMAND_UNKNOWN_LOCATION.value());
      } else if (this.spawnVehicle(var1, var2, var3)) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + String.format(LangConfiguration.VEHICLE_SUCCESS_SPAWN.value(), var2));
      }
   }

   @Subcommand("remove")
   @CommandPermission("infinitevehicles.command.remove")
   @CommandCompletion("1-100 @vehicles")
   public void remove(Player player, double radius, String vehicleID) {
      String var10001;
      if (!(var2 > 100.0D) && !(var2 < 1.0D)) {
         int var5 = this.removeVehiclesNearby(var1, var2, var4);
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + String.format(LangConfiguration.VEHICLE_SUCCESS_REMOVE.value(), var5));
      } else {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + String.format(LangConfiguration.COMMAND_INVALID_RADIUS.value(), "1", "100"));
      }
   }

   @HelpCommand
   @Subcommand("help")
   public void sendHelpMessage(CommandSender player) {
      var1.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', "&f.&bo&fO&b-&fO&bo&f. &f&lInfinite&b&lVehicles &f&lHelp &f.&bo&fO&b-&fO&bo&f.")));
      if (var1.hasPermission("infinitevehicles.command.get")) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &b/infinitevehicles give <name> [<player>] [<amount>]"));
      }

      if (var1.hasPermission("infinitevehicles.command.spawn")) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &b/infinitevehicles spawn <type> [<location>]"));
      }

      if (var1.hasPermission("infinitevehicles.command.leaderboard")) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &b/infinitevehicles leaderboard <type>"));
      }

      if (var1.hasPermission("infinitevehicles.command.remove")) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &b/infinitevehicles remove <radius> [<type>]"));
      }

      if (var1.hasPermission("infinitevehicles.command.reload")) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &b/infinitevehicles reload"));
      }

      if (var1.hasPermission("infinitevehicles.command.getupgrade")) {
         var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &b/infinitevehicles getupgrade <upgrade_id> <upgrade_type> <tier> [<amount>]"));
      }

   }

   private boolean spawnVehicle(@NotNull CommandSender by, @NotNull String name, @NotNull Location location) {
      VehicleConfiguration var4 = (VehicleConfiguration)Registries.getRegistry(VehicleConfiguration.class).get(var2);
      if (var4 == null) {
         String var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.COMMAND_UNKNOWN_VEHICLE.value());
         return false;
      } else {
         Vehicle var5 = InfiniteVehicles.getVehicleHandler().spawnVehicle((VehicleConfiguration)Objects.requireNonNull(var4), (World)Objects.requireNonNull(var3.getWorld()), var3.getX(), var3.getY(), var3.getZ(), (UUID)null, var1 instanceof Player ? ((Player)var1).getUniqueId() : null);
         return true;
      }
   }

   public int removeVehiclesNearby(Player player, double radius, String type) {
      Location var5 = var1.getLocation();
      VehicleConfiguration var6 = var4 != null ? (VehicleConfiguration)Registries.getRegistry(VehicleConfiguration.class).get(var4) : null;
      int var7 = 0;
      Iterator var8 = InfiniteVehicles.getVehicleHandler().getRegisteredVehicles().iterator();

      while(true) {
         Vehicle var9;
         do {
            do {
               if (!var8.hasNext()) {
                  return var7;
               }

               var9 = (Vehicle)var8.next();
            } while(!Objects.equals(var9.getWorld(), var1.getWorld()));
         } while(var6 != null && !Objects.equals(var6.getId(), var9.getConfiguration().getId()));

         if (var9.getLocation().distanceSquared(var5) <= var2 * var2) {
            InfiniteVehicles.getVehicleHandler().destroyVehicle(var9);
            ++var7;
         }
      }
   }
}
