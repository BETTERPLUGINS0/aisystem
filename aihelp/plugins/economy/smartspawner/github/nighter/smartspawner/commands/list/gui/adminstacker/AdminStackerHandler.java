package github.nighter.smartspawner.commands.list.gui.adminstacker;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.list.gui.management.SpawnerManagementGUI;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.data.database.SpawnerDatabaseHandler;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AdminStackerHandler implements Listener {
   private static final int[] DECREASE_SLOTS = new int[]{9, 10, 11};
   private static final int[] INCREASE_SLOTS = new int[]{17, 16, 15};
   private static final int SPAWNER_INFO_SLOT = 13;
   private static final int BACK_SLOT = 22;
   private static final int[] STACK_AMOUNTS = new int[]{64, 10, 1};
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;
   private final SpawnerManagementGUI managementGUI;

   public AdminStackerHandler(SmartSpawner plugin, SpawnerManagementGUI managementGUI) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
      this.managementGUI = managementGUI;
   }

   @EventHandler
   public void onAdminStackerClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof AdminStackerHolder) {
         AdminStackerHolder holder = (AdminStackerHolder)var3;
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
               SpawnerData spawner = holder.getSpawnerData();
               String worldName = holder.getWorldName();
               int listPage = holder.getListPage();
               if (spawner == null) {
                  this.messageService.sendMessage(player, "spawner_not_found");
               } else {
                  int slot = event.getSlot();
                  this.handleClick(player, spawner, worldName, listPage, slot);
               }
            }
         }
      }
   }

   private void handleClick(Player player, SpawnerData spawner, String worldName, int listPage, int slot) {
      if (slot == 22) {
         this.managementGUI.openManagementMenu(player, spawner.getSpawnerId(), worldName, listPage);
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      } else if (slot != 13) {
         int i;
         for(i = 0; i < DECREASE_SLOTS.length; ++i) {
            if (slot == DECREASE_SLOTS[i]) {
               this.handleStackChange(player, spawner, worldName, listPage, -STACK_AMOUNTS[i]);
               return;
            }
         }

         for(i = 0; i < INCREASE_SLOTS.length; ++i) {
            if (slot == INCREASE_SLOTS[i]) {
               this.handleStackChange(player, spawner, worldName, listPage, STACK_AMOUNTS[i]);
               return;
            }
         }

      }
   }

   private void handleStackChange(Player player, SpawnerData spawner, String worldName, int listPage, int change) {
      if (!player.hasPermission("smartspawner.stack")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         int newStackSize = spawner.getStackSize() + change;
         if (newStackSize < 1) {
            newStackSize = 1;
         } else if (newStackSize > spawner.getMaxStackSize()) {
            newStackSize = spawner.getMaxStackSize();
            Map<String, String> placeholders = new HashMap(2);
            placeholders.put("max", String.valueOf(newStackSize));
            this.messageService.sendMessage((Player)player, "spawner_stack_full", placeholders);
         }

         spawner.setStackSize(newStackSize);
         this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
         spawner.updateLastInteractedPlayer(player.getName());
         player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
         AdminStackerUI adminStackerUI = new AdminStackerUI(this.plugin);
         adminStackerUI.openAdminStackerGui(player, spawner, worldName, listPage);
      }
   }

   @EventHandler
   public void onRemoteAdminStackerClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof RemoteAdminStackerHolder) {
         RemoteAdminStackerHolder holder = (RemoteAdminStackerHolder)var3;
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
               int slot = event.getSlot();
               this.handleRemoteClick(player, holder, event.getInventory(), slot);
            }
         }
      }
   }

   private void handleRemoteClick(Player player, RemoteAdminStackerHolder holder, Inventory inventory, int slot) {
      if (slot == 22) {
         this.saveRemoteStackChanges(player, holder);
      } else if (slot != 13) {
         int i;
         for(i = 0; i < DECREASE_SLOTS.length; ++i) {
            if (slot == DECREASE_SLOTS[i]) {
               this.handleRemoteStackChange(player, holder, inventory, -STACK_AMOUNTS[i]);
               return;
            }
         }

         for(i = 0; i < INCREASE_SLOTS.length; ++i) {
            if (slot == INCREASE_SLOTS[i]) {
               this.handleRemoteStackChange(player, holder, inventory, STACK_AMOUNTS[i]);
               return;
            }
         }

      }
   }

   private void handleRemoteStackChange(Player player, RemoteAdminStackerHolder holder, Inventory inventory, int change) {
      if (!player.hasPermission("smartspawner.stack")) {
         this.messageService.sendMessage(player, "no_permission");
      } else {
         holder.adjustStackSize(change);
         player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
         AdminStackerUI adminStackerUI = new AdminStackerUI(this.plugin);
         adminStackerUI.refreshRemoteStackerGui(inventory, holder);
      }
   }

   private void saveRemoteStackChanges(Player player, RemoteAdminStackerHolder holder) {
      SpawnerStorage storage = this.plugin.getSpawnerStorage();
      if (storage instanceof SpawnerDatabaseHandler) {
         SpawnerDatabaseHandler dbHandler = (SpawnerDatabaseHandler)storage;
         String targetServer = holder.getTargetServer();
         String spawnerId = holder.getSpawnerId();
         int newStackSize = holder.getCurrentStackSize();
         int originalSize = holder.getSpawnerData().getStackSize();
         if (newStackSize != originalSize) {
            player.sendMessage("§eSaving stack size changes...");
            dbHandler.updateRemoteSpawnerStackSizeAsync(targetServer, spawnerId, newStackSize, (success) -> {
               if (success) {
                  player.sendMessage("§aStack size updated from " + originalSize + " to " + newStackSize);
                  player.sendMessage("§e[Note] Changes will sync to " + targetServer + " on next refresh.");
                  player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
               } else {
                  player.sendMessage("§cFailed to update stack size. Spawner may have been removed.");
                  player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
               }

               this.managementGUI.openManagementMenu(player, spawnerId, holder.getWorldName(), holder.getListPage(), targetServer);
            });
         } else {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
            this.managementGUI.openManagementMenu(player, spawnerId, holder.getWorldName(), holder.getListPage(), targetServer);
         }

      } else {
         this.messageService.sendMessage(player, "action_failed");
      }
   }
}
