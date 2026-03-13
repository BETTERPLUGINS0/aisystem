package github.nighter.smartspawner.commands.list.gui.management;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.list.ListSubCommand;
import github.nighter.smartspawner.commands.list.gui.adminstacker.AdminStackerUI;
import github.nighter.smartspawner.commands.list.gui.list.enums.FilterOption;
import github.nighter.smartspawner.commands.list.gui.list.enums.SortOption;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.data.database.SpawnerDatabaseHandler;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuUI;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpawnerManagementHandler implements Listener {
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;
   private final SpawnerStorage spawnerStorage;
   private final ListSubCommand listSubCommand;
   private final SpawnerMenuUI spawnerMenuUI;
   private final AdminStackerUI adminStackerUI;

   public SpawnerManagementHandler(SmartSpawner plugin, ListSubCommand listSubCommand) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
      this.spawnerStorage = plugin.getSpawnerStorage();
      this.listSubCommand = listSubCommand;
      this.spawnerMenuUI = plugin.getSpawnerMenuUI();
      this.adminStackerUI = new AdminStackerUI(plugin);
   }

   @EventHandler
   public void onSpawnerManagementClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof SpawnerManagementHolder) {
         SpawnerManagementHolder holder = (SpawnerManagementHolder)var3;
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
               String spawnerId = holder.getSpawnerId();
               String worldName = holder.getWorldName();
               int listPage = holder.getListPage();
               String targetServer = holder.getTargetServer();
               boolean isRemote = holder.isRemoteServer();
               int slot = event.getSlot();
               if (slot == 26) {
                  this.handleBack(player, worldName, listPage, targetServer);
               } else if (isRemote) {
                  switch(slot) {
                  case 10:
                     player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                  case 11:
                  case 13:
                  case 15:
                  default:
                     break;
                  case 12:
                     this.handleRemoteOpenSpawnerInfo(player, spawnerId, targetServer, worldName, listPage);
                     break;
                  case 14:
                     this.handleRemoteStackManagement(player, spawnerId, targetServer, worldName, listPage);
                     break;
                  case 16:
                     this.handleRemoteRemoveSpawner(player, spawnerId, targetServer, worldName, listPage);
                  }

               } else {
                  SpawnerData spawner = this.spawnerManager.getSpawnerById(spawnerId);
                  if (spawner == null) {
                     this.messageService.sendMessage(player, "spawner_not_found");
                  } else {
                     switch(slot) {
                     case 10:
                        this.handleTeleport(player, spawner);
                     case 11:
                     case 13:
                     case 15:
                     default:
                        break;
                     case 12:
                        this.handleOpenSpawner(player, spawner);
                        break;
                     case 14:
                        this.handleStackManagement(player, spawner, worldName, listPage);
                        break;
                     case 16:
                        this.handleRemoveSpawner(player, spawner, worldName, listPage);
                     }

                  }
               }
            }
         }
      }
   }

   private void handleTeleport(Player player, SpawnerData spawner) {
      Location loc = spawner.getSpawnerLocation().clone().add(0.5D, 1.0D, 0.5D);
      player.teleportAsync(loc);
      this.messageService.sendMessage(player, "teleported_to_spawner");
      player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
      player.closeInventory();
   }

   private void handleOpenSpawner(Player player, SpawnerData spawner) {
      if (this.plugin.getGuiLayoutConfig().isSkipMainGui()) {
         Inventory storageInventory = this.plugin.getSpawnerStorageUI().createStorageInventory(spawner, 1, -1);
         player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
         player.openInventory(storageInventory);
      } else {
         if (this.isBedrockPlayer(player)) {
            if (this.plugin.getSpawnerMenuFormUI() != null) {
               this.plugin.getSpawnerMenuFormUI().openSpawnerForm(player, spawner);
            } else {
               this.spawnerMenuUI.openSpawnerMenu(player, spawner, false);
            }
         } else {
            this.spawnerMenuUI.openSpawnerMenu(player, spawner, false);
         }

         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      }
   }

   private void handleStackManagement(Player player, SpawnerData spawner, String worldName, int listPage) {
      if (!player.hasPermission("smartspawner.stack")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         this.adminStackerUI.openAdminStackerGui(player, spawner, worldName, listPage);
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      }
   }

   private void handleRemoveSpawner(Player player, SpawnerData spawner, String worldName, int listPage) {
      Location loc = spawner.getSpawnerLocation();
      this.plugin.getSpawnerGuiViewManager().closeAllViewersInventory(spawner);
      String spawnerId = spawner.getSpawnerId();
      spawner.getSpawnerStop().set(true);
      if (loc.getBlock().getType() == Material.SPAWNER) {
         loc.getBlock().setType(Material.AIR);
      }

      this.spawnerManager.removeSpawner(spawnerId);
      this.spawnerStorage.markSpawnerDeleted(spawnerId);
      Map<String, String> placeholders = new HashMap();
      placeholders.put("id", spawner.getSpawnerId());
      this.messageService.sendMessage((Player)player, "spawner_management.removed", placeholders);
      player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
      this.handleBack(player, worldName, listPage, (String)null);
   }

   private void handleBack(Player player, String worldName, int listPage, String targetServer) {
      FilterOption filter = FilterOption.ALL;
      SortOption sort = SortOption.DEFAULT;

      try {
         filter = this.listSubCommand.getUserFilter(player, worldName);
         sort = this.listSubCommand.getUserSort(player, worldName);
      } catch (Exception var8) {
      }

      if (targetServer != null && !targetServer.equals(this.listSubCommand.getCurrentServerName())) {
         this.listSubCommand.openSpawnerListGUIForServer(player, targetServer, worldName, listPage);
      } else {
         this.listSubCommand.openSpawnerListGUI(player, worldName, listPage, filter, sort);
      }

      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
   }

   private boolean isBedrockPlayer(Player player) {
      return this.plugin.getIntegrationManager() != null && this.plugin.getIntegrationManager().getFloodgateHook() != null ? this.plugin.getIntegrationManager().getFloodgateHook().isBedrockPlayer(player) : false;
   }

   private void handleRemoteOpenSpawnerInfo(Player player, String spawnerId, String targetServer, String worldName, int listPage) {
      SpawnerDatabaseHandler dbHandler = this.getDbHandler();
      if (dbHandler == null) {
         this.messageService.sendMessage(player, "action_failed");
      } else {
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
         dbHandler.getRemoteSpawnerByIdAsync(targetServer, spawnerId, (spawnerData) -> {
            if (spawnerData == null) {
               this.messageService.sendMessage(player, "spawner_not_found");
            } else {
               player.sendMessage("");
               player.sendMessage("§6§l=== Remote Spawner Info ===");
               String var10001 = spawnerData.getServerName();
               player.sendMessage("§7Server: §f" + var10001);
               var10001 = spawnerData.getSpawnerId();
               player.sendMessage("§7ID: §f#" + var10001);
               var10001 = this.formatEntityName(spawnerData.getEntityType().name());
               player.sendMessage("§7Type: §f" + var10001);
               var10001 = spawnerData.getWorldName();
               player.sendMessage("§7Location: §f" + var10001 + " (" + spawnerData.getLocX() + ", " + spawnerData.getLocY() + ", " + spawnerData.getLocZ() + ")");
               int var3 = spawnerData.getStackSize();
               player.sendMessage("§7Stack Size: §f" + var3);
               player.sendMessage("§7Status: " + (spawnerData.isActive() ? "§aActive" : "§cInactive"));
               player.sendMessage("§7Stored XP: §f" + spawnerData.getStoredExp());
               player.sendMessage("§7Total Items: §f" + spawnerData.getTotalItems());
               player.sendMessage("§6§l==========================");
               player.sendMessage("");
            }
         });
      }
   }

   private void handleRemoteStackManagement(Player player, String spawnerId, String targetServer, String worldName, int listPage) {
      if (!player.hasPermission("smartspawner.stack")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         SpawnerDatabaseHandler dbHandler = this.getDbHandler();
         if (dbHandler == null) {
            this.messageService.sendMessage(player, "action_failed");
         } else {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
            dbHandler.getRemoteSpawnerByIdAsync(targetServer, spawnerId, (spawnerData) -> {
               if (spawnerData == null) {
                  this.messageService.sendMessage(player, "spawner_not_found");
               } else {
                  this.adminStackerUI.openRemoteAdminStackerGui(player, spawnerData, targetServer, worldName, listPage);
               }
            });
         }
      }
   }

   private void handleRemoteRemoveSpawner(Player player, String spawnerId, String targetServer, String worldName, int listPage) {
      SpawnerDatabaseHandler dbHandler = this.getDbHandler();
      if (dbHandler == null) {
         this.messageService.sendMessage(player, "action_failed");
      } else {
         dbHandler.deleteRemoteSpawnerAsync(targetServer, spawnerId, (success) -> {
            if (success) {
               Map<String, String> placeholders = new HashMap();
               placeholders.put("id", spawnerId);
               this.messageService.sendMessage((Player)player, "spawner_management.removed", placeholders);
               player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
               player.sendMessage("§e[Note] The spawner block on " + targetServer + " will be removed when that server syncs.");
            } else {
               this.messageService.sendMessage(player, "spawner_not_found");
               player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            }

            this.handleBack(player, worldName, listPage, targetServer);
         });
      }
   }

   private SpawnerDatabaseHandler getDbHandler() {
      return this.spawnerStorage instanceof SpawnerDatabaseHandler ? (SpawnerDatabaseHandler)this.spawnerStorage : null;
   }

   private String formatEntityName(String name) {
      return (String)Arrays.stream(name.toLowerCase().split("_")).map((word) -> {
         String var10000 = word.substring(0, 1).toUpperCase();
         return var10000 + word.substring(1);
      }).reduce((a, b) -> {
         return a + " " + b;
      }).orElse(name);
   }
}
