package github.nighter.smartspawner.spawner.interactions.click;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.bedrock.FloodgateHook;
import github.nighter.smartspawner.hooks.protections.CheckOpenMenu;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuFormUI;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuUI;
import github.nighter.smartspawner.spawner.interactions.stack.SpawnerStackHandler;
import github.nighter.smartspawner.spawner.interactions.type.SpawnEggHandler;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SpawnerClickManager implements Listener {
   private static final long COOLDOWN_MS = 250L;
   private static final long CLEANUP_INTERVAL_TICKS = 6000L;
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;
   private final SpawnEggHandler spawnEggHandler;
   private final SpawnerStackHandler spawnerStackHandler;
   private final SpawnerMenuUI spawnerMenuUI;
   private final SpawnerMenuFormUI spawnerMenuFormUI;
   private final Map<UUID, Long> playerCooldowns = new ConcurrentHashMap();
   private boolean skipMainGui = false;
   private FloodgateHook floodgateHook = null;

   public SpawnerClickManager(SmartSpawner plugin) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
      this.spawnEggHandler = plugin.getSpawnEggHandler();
      this.spawnerStackHandler = plugin.getSpawnerStackHandler();
      this.spawnerMenuUI = plugin.getSpawnerMenuUI();
      this.spawnerMenuFormUI = plugin.getSpawnerMenuFormUI();
      this.loadConfig();
      this.initCleanupTask();
   }

   public void loadConfig() {
      this.skipMainGui = this.plugin.getGuiLayoutConfig().isSkipMainGui();
      if (this.plugin.getIntegrationManager() != null) {
         this.floodgateHook = this.plugin.getIntegrationManager().getFloodgateHook();
      }

   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onSpawnerClick(PlayerInteractEvent event) {
      if (this.isValidSpawnerInteraction(event)) {
         Player player = event.getPlayer();
         Block block = event.getClickedBlock();
         SpawnerData spawner = this.spawnerManager.getSpawnerByLocation(block.getLocation());
         if (spawner != null) {
            if (!this.isInteractionAllowed(player)) {
               event.setCancelled(true);
            } else {
               ItemStack heldItem = player.getInventory().getItemInMainHand();
               ItemStack offhandItem = player.getInventory().getItemInOffHand();
               Material itemType = heldItem.getType();
               Material offhandType = offhandItem.getType();
               if (!this.shouldAllowNormalBlockPlacement(player, itemType)) {
                  if (!this.isBedrockPlayerUsingTool(player, itemType)) {
                     if (!this.isArmor(itemType) && !this.isArmor(offhandType)) {
                        event.setCancelled(true);
                        this.handleSpawnerInteraction(player, block, heldItem, itemType, spawner);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isValidSpawnerInteraction(PlayerInteractEvent event) {
      return event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SPAWNER;
   }

   private boolean isInteractionAllowed(Player player) {
      long currentTime = System.currentTimeMillis();
      Long lastInteraction = (Long)this.playerCooldowns.get(player.getUniqueId());
      if (lastInteraction != null && currentTime - lastInteraction < 250L) {
         return false;
      } else {
         this.playerCooldowns.put(player.getUniqueId(), currentTime);
         return true;
      }
   }

   private boolean shouldAllowNormalBlockPlacement(Player player, Material itemType) {
      return player.isSneaking() && itemType.isBlock() && itemType != Material.SPAWNER;
   }

   private boolean isBedrockPlayerUsingTool(Player player, Material itemType) {
      if (!this.isBedrockPlayer(player)) {
         return false;
      } else {
         String itemName = itemType.name();
         boolean isTool = itemName.endsWith("_PICKAXE") || itemName.endsWith("_SHOVEL") || itemName.endsWith("_HOE") || itemName.endsWith("_AXE");
         return isTool;
      }
   }

   private boolean isArmor(Material material) {
      String name = material.name();
      return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS") || material == Material.ELYTRA || material == Material.CARVED_PUMPKIN || material == Material.SKELETON_SKULL || material == Material.WITHER_SKELETON_SKULL || material == Material.ZOMBIE_HEAD || material == Material.PLAYER_HEAD || material == Material.DRAGON_HEAD || material == Material.PIGLIN_HEAD || material == Material.CREEPER_HEAD || material == Material.TURTLE_HELMET;
   }

   private void handleSpawnerInteraction(Player player, Block block, ItemStack heldItem, Material itemType, SpawnerData spawner) {
      if (!CheckOpenMenu.CanPlayerOpenMenu(player, block.getLocation())) {
         this.messageService.sendMessage(player, "spawner_protected");
      } else if (this.isSpawnEgg(itemType)) {
         this.spawnEggHandler.handleSpawnEggUse(player, (CreatureSpawner)block.getState(false), spawner, heldItem);
      } else if (itemType == Material.SPAWNER) {
         this.spawnerStackHandler.handleSpawnerStacking(player, block, spawner, heldItem);
      } else {
         this.openSpawnerMenu(player, spawner);
      }
   }

   private void openSpawnerMenu(Player player, SpawnerData spawner) {
      if (this.skipMainGui) {
         this.openStorageGui(player, spawner);
      } else {
         if (this.floodgateHook != null && this.floodgateHook.isBedrockPlayer(player)) {
            if (this.spawnerMenuFormUI != null) {
               this.spawnerMenuFormUI.openSpawnerForm(player, spawner);
            } else {
               this.spawnerMenuUI.openSpawnerMenu(player, spawner, false);
            }
         } else {
            this.spawnerMenuUI.openSpawnerMenu(player, spawner, false);
         }

      }
   }

   private void openStorageGui(Player player, SpawnerData spawner) {
      Inventory storageInventory = this.plugin.getSpawnerStorageUI().createStorageInventory(spawner, 1, -1);
      player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
      player.openInventory(storageInventory);
   }

   private boolean isSpawnEgg(Material material) {
      return material.name().endsWith("_SPAWN_EGG");
   }

   private boolean isBedrockPlayer(Player player) {
      return this.floodgateHook != null && this.floodgateHook.isBedrockPlayer(player);
   }

   private void initCleanupTask() {
      Scheduler.runTaskTimer(this::cleanupCooldowns, 6000L, 6000L);
   }

   public void cleanupCooldowns() {
      long expirationThreshold = System.currentTimeMillis() - 2500L;
      this.playerCooldowns.entrySet().removeIf((entry) -> {
         return (Long)entry.getValue() < expirationThreshold;
      });
   }

   public void cleanup() {
      this.playerCooldowns.clear();
   }
}
