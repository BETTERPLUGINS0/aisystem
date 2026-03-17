package com.bergerkiller.bukkit.tc.chest;

import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Greedy;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Quoted;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.Commands;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresMultiplePermissions;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.exception.command.NoTrainStorageChestItemException;
import com.bergerkiller.bukkit.tc.utils.FormattedSpeed;
import java.util.function.Consumer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TrainChestCommands {
   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest give <target_player>")
   @CommandDescription("Gives a pre-configured train-storing chest item to another player")
   private void commandGiveChestItemToPlayer(TrainCarts plugin, CommandSender sender, @Argument(value = "target_player",description = "Who to give it to",suggestions = "targetplayer") String targetPlayerName, @Flag(value = "train",description = "Initial train spawn configuration",suggestions = "trainspawnpattern") String spawnConfig, @Flag(value = "name",description = "Display name of the train in the chest item") String name, @Flag(value = "locked",description = "Whether the train in the item can be changed") boolean locked, @Flag(value = "finite",description = "Whether to make the item empty when used") boolean finite, @Quoted @Flag(value = "spawnmessage",description = "Sets a custom successful spawn message") String spawnMessage) {
      Player targetPlayer = Util.findPlayer(sender, targetPlayerName);
      if (targetPlayer != null) {
         ItemStack item = TrainChestItemUtil.createItem();
         if (spawnConfig != null && !spawnConfig.isEmpty()) {
            if (sender instanceof Player && !SpawnableGroup.parse(plugin, spawnConfig).checkSpawnPermissions((Player)sender)) {
               Localization.SPAWN_FORBIDDEN_CONTENTS.message(sender, new String[0]);
               return;
            }

            TrainChestItemUtil.store(item, spawnConfig);
         }

         if (name != null) {
            TrainChestItemUtil.setName(item, name);
         }

         if (locked) {
            TrainChestItemUtil.setLocked(item, locked);
         }

         if (finite) {
            TrainChestItemUtil.setFiniteSpawns(item, finite);
         }

         if (spawnMessage != null) {
            TrainChestItemUtil.setSpawnMessage(item, StringUtil.ampToColor(spawnMessage));
         }

         targetPlayer.getInventory().addItem(new ItemStack[]{item});
         if (targetPlayer == sender) {
            Localization.CHEST_GIVE.message(sender, new String[0]);
         } else {
            Localization.CHEST_GIVE_TO.message(sender, new String[]{targetPlayer.getName()});
            Localization.CHEST_GIVE.message(targetPlayer, new String[0]);
         }

      }
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest [spawnconfig]")
   @CommandDescription("Gives a new train-storing chest item to the sender, train information to store can be specified")
   private void commandGiveChestItem(TrainCarts plugin, Player sender, @Argument("spawnconfig") @Greedy String spawnConfig) {
      ItemStack item = TrainChestItemUtil.createItem();
      if (spawnConfig != null && !spawnConfig.isEmpty()) {
         if (!SpawnableGroup.parse(plugin, spawnConfig).checkSpawnPermissions(sender)) {
            Localization.SPAWN_FORBIDDEN_CONTENTS.message(sender, new String[0]);
            return;
         }

         TrainChestItemUtil.store(item, spawnConfig);
      }

      sender.getInventory().addItem(new ItemStack[]{item});
      Localization.CHEST_GIVE.message(sender, new String[0]);
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest set [spawnconfig]")
   @CommandDescription("Clears the train-storing chest item the player is currently holding")
   private void commandSetChestItem(TrainCarts plugin, Player player, @Argument(value = "spawnconfig",suggestions = "trainspawnpattern") @Greedy String spawnConfig) {
      if (spawnConfig != null && !spawnConfig.isEmpty() && !SpawnableGroup.parse(plugin, spawnConfig).checkSpawnPermissions(player)) {
         Localization.SPAWN_FORBIDDEN_CONTENTS.message(player, new String[0]);
      } else {
         this.updateChestItemInInventory(player, (item) -> {
            TrainChestItemUtil.store(item, spawnConfig == null ? "" : spawnConfig);
         });
      }
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest clear")
   @CommandDescription("Clears the train-storing chest item the player is currently holding")
   private void commandClearChestItem(Player player) {
      this.updateChestItemInInventory(player, TrainChestItemUtil::clear);
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest lock")
   @CommandDescription("Locks the train-storing chest item so it can not pick up trains by right-clicking")
   private void commandLockChestItem(Player player) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setLocked(item, true);
      });
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest unlock")
   @CommandDescription("Unlocks the train-storing chest item so it can pick up trains by right-clicking again")
   private void commandUnlockChestItem(Player player) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setLocked(item, false);
      });
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest finite <finite_spawns>")
   @CommandDescription("Sets whether the train-storing chest item has only finite spawns, and becomes empty after spawning")
   private void commandChestItemSetFiniteSpawns(Player player, @Argument("finite_spawns") boolean finite) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setFiniteSpawns(item, finite);
      });
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest speed <speed>")
   @CommandDescription("Sets the initial speed of the train when spawning")
   private void commandChestItemSetSpeed(Player player, @Argument("speed") FormattedSpeed speed) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setSpeed(item, speed.getValue());
      });
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest name <name>")
   @CommandDescription("Sets a descriptive name for the train-storing chest item")
   private void commandNameChestItem(Player player, @Argument("name") String name) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setName(item, name);
      });
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest spawnmessage <message>")
   @CommandDescription("Sets the message displayed when successfully spawning using the chest item")
   private void commandSetShowMessage(Player player, @Greedy @Argument("message") String message) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setSpawnMessage(item, StringUtil.ampToColor(message));
      });
   }

   @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)
   @Command("train chest spawnmessage DEFAULT")
   @CommandDescription("Resets the message displayed when successfully spawning using the chest item")
   private void commandSetDefaultShowMessage(Player player) {
      this.updateChestItemInInventory(player, (item) -> {
         TrainChestItemUtil.setSpawnMessage((ItemStack)item, (String)null);
      });
   }

   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_SAVEDTRAIN_IMPORT), @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)})
   @Command("train chest import <url>")
   @CommandDescription("Imports a saved train into the chest item from an online hastebin server by url")
   private void commandImportChestItem(Player player, TrainCarts plugin, @Greedy @FlagYielding @Argument("url") String url, @Flag("force") boolean force, @Flag("import-models") boolean importModels) {
      ItemStack item_when_started = HumanHand.getItemInMainHand(player);
      Commands.importTrain(plugin, player, url, (config) -> {
         Commands.importTrainUsedModels(plugin, player, config, importModels, force);
         if (TrainChestItemUtil.isItem(item_when_started)) {
            TrainChestItemUtil.store(item_when_started, config);
         } else {
            ItemStack newItem = TrainChestItemUtil.createItem();
            TrainChestItemUtil.store(newItem, config);
            player.getInventory().addItem(new ItemStack[]{newItem});
            Localization.CHEST_GIVE.message(player, new String[0]);
         }

         Localization.CHEST_IMPORTED.message(player, new String[0]);
      });
   }

   @CommandRequiresMultiplePermissions({@CommandRequiresPermission(Permission.COMMAND_SAVEDTRAIN_EXPORT), @CommandRequiresPermission(Permission.COMMAND_STORAGE_CHEST_CREATE)})
   @Command("train chest export")
   @CommandDescription("Exports the train configuration in the chest item to a hastebin server")
   private void commandExportChestItem(TrainCarts plugin, Player player) {
      ItemStack item = HumanHand.getItemInMainHand(player);
      if (!TrainChestItemUtil.isItem(item)) {
         throw new NoTrainStorageChestItemException();
      } else {
         SpawnableGroup spawnable = TrainChestItemUtil.getSpawnableGroup(plugin, item);
         if (spawnable == null) {
            Localization.CHEST_SPAWN_EMPTY.message(player, new String[0]);
         } else {
            Commands.exportTrain(player, spawnable.getSavedName(), spawnable.getFullConfig());
         }
      }
   }

   private void updateChestItemInInventory(Player player, Consumer<ItemStack> consumer) {
      ItemStack item = HumanHand.getItemInMainHand(player);
      if (!TrainChestItemUtil.isItem(item)) {
         throw new NoTrainStorageChestItemException();
      } else {
         item = ItemUtil.cloneItem(item);
         consumer.accept(item);
         HumanHand.setItemInMainHand(player, item);
         Localization.CHEST_UPDATE.message(player, new String[0]);
      }
   }
}
