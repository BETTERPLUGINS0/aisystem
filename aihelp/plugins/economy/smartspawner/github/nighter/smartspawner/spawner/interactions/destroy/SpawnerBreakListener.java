package github.nighter.smartspawner.spawner.interactions.destroy;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerPlayerBreakEvent;
import github.nighter.smartspawner.extras.HopperService;
import github.nighter.smartspawner.hooks.protections.CheckBreakBlock;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.data.SpawnerFileHandler;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.item.SpawnerItemFactory;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.utils.SpawnerLocationLockManager;
import github.nighter.smartspawner.utils.BlockPos;
import java.util.Iterator;
import java.util.Map;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerBreakListener implements Listener {
   private static final int MAX_STACK_SIZE = 64;
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;
   private final HopperService hopperService;
   private final SpawnerItemFactory spawnerItemFactory;
   private final SpawnerFileHandler spawnerFileHandler;
   private final SpawnerLocationLockManager locationLockManager;

   public SpawnerBreakListener(SmartSpawner plugin) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
      this.hopperService = plugin.getHopperService();
      this.spawnerItemFactory = plugin.getSpawnerItemFactory();
      this.spawnerFileHandler = plugin.getSpawnerFileHandler();
      this.locationLockManager = plugin.getSpawnerLocationLockManager();
   }

   @EventHandler(
      priority = EventPriority.HIGH,
      ignoreCancelled = true
   )
   public void onSpawnerBreak(BlockBreakEvent event) {
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Location location = block.getLocation();
      if (block.getType() == Material.SPAWNER) {
         if (!CheckBreakBlock.CanPlayerBreakBlock(player, location)) {
            event.setCancelled(true);
         } else if (!this.plugin.getConfig().getBoolean("spawner_break.enabled", true)) {
            event.setCancelled(true);
         } else {
            SpawnerData spawner = this.spawnerManager.getSpawnerByLocation(location);
            if (!this.plugin.getConfig().getBoolean("natural_spawner.breakable", false) && spawner == null) {
               block.setType(Material.AIR);
               event.setCancelled(true);
               this.messageService.sendMessage(player, "natural_spawner_break_blocked");
            } else if (!player.hasPermission("smartspawner.break")) {
               event.setCancelled(true);
               this.messageService.sendMessage(player, "spawner_break_no_permission");
            } else {
               if (spawner != null) {
                  this.handleSmartSpawnerBreak(block, spawner, player);
               } else {
                  CreatureSpawner creatureSpawner = (CreatureSpawner)block.getState(false);
                  if (this.callAPIEvent(player, block.getLocation(), 1)) {
                     event.setCancelled(true);
                     return;
                  }

                  this.handleVanillaSpawnerBreak(block, creatureSpawner, player);
               }

               event.setCancelled(true);
               this.cleanupAssociatedHopper(block);
            }
         }
      }
   }

   private void handleSmartSpawnerBreak(Block block, SpawnerData spawner, Player player) {
      Location location = block.getLocation();
      ItemStack tool = player.getInventory().getItemInMainHand();
      if (this.validateBreakConditions(player, tool, spawner)) {
         if (!this.locationLockManager.tryLock(location)) {
            this.messageService.sendMessage(player, "spawner_break_in_progress");
         } else {
            try {
               SpawnerData currentSpawner = this.spawnerManager.getSpawnerByLocation(location);
               if (currentSpawner != null && currentSpawner.getSpawnerId().equals(spawner.getSpawnerId())) {
                  spawner.updateLastInteractedPlayer(player.getName());
                  this.plugin.getSpawnerGuiViewManager().closeAllViewersInventory(spawner);
                  SpawnerBreakListener.SpawnerBreakResult result = this.processDrops(player, location, spawner, player.isSneaking(), block);
                  if (result.isSuccess() && player.getGameMode() != GameMode.CREATIVE) {
                     this.reduceDurability(tool, player, result.getDurabilityLoss());
                  }

                  return;
               }
            } finally {
               this.locationLockManager.unlock(location);
            }

         }
      }
   }

   private void handleVanillaSpawnerBreak(Block block, CreatureSpawner creatureSpawner, Player player) {
      Location location = block.getLocation();
      ItemStack tool = player.getInventory().getItemInMainHand();
      if (this.validateBreakConditions(player, tool, (SpawnerData)null)) {
         if (!this.locationLockManager.tryLock(location)) {
            this.messageService.sendMessage(player, "action_in_progress");
         } else {
            try {
               if (block.getType() != Material.SPAWNER) {
                  return;
               }

               EntityType entityType = creatureSpawner.getSpawnedType();
               ItemStack spawnerItem;
               if (this.plugin.getConfig().getBoolean("natural_spawner.convert_to_smart_spawner", false)) {
                  spawnerItem = this.spawnerItemFactory.createSmartSpawnerItem(entityType);
               } else {
                  spawnerItem = this.spawnerItemFactory.createVanillaSpawnerItem(entityType);
               }

               boolean directToInventory = this.plugin.getConfig().getBoolean("spawner_break.direct_to_inventory", false);
               World world = location.getWorld();
               if (world != null) {
                  block.setType(Material.AIR);
                  if (directToInventory) {
                     this.giveSpawnersToPlayer(player, 1, spawnerItem);
                     player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.2F);
                  } else {
                     world.dropItemNaturally(location.toCenterLocation(), spawnerItem);
                  }

                  this.reduceDurability(tool, player, this.plugin.getConfig().getInt("spawner_break.durability_loss", 1));
               }
            } finally {
               this.locationLockManager.unlock(location);
            }

         }
      }
   }

   private boolean validateBreakConditions(Player player, ItemStack tool, SpawnerData spawner) {
      if (player.getGameMode() == GameMode.CREATIVE) {
         return true;
      } else if (!player.hasPermission("smartspawner.break")) {
         this.messageService.sendMessage(player, "spawner_break_no_permission");
         return false;
      } else if (!this.isValidTool(tool)) {
         this.messageService.sendMessage(player, "spawner_break_required_tools");
         return false;
      } else {
         if (this.plugin.getConfig().getBoolean("spawner_break.silk_touch.required", true)) {
            int requiredLevel = this.plugin.getConfig().getInt("spawner_break.silk_touch.level", 1);
            if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) < requiredLevel) {
               this.messageService.sendMessage(player, "spawner_break_silk_touch_required");
               return false;
            }
         }

         return true;
      }
   }

   private SpawnerBreakListener.SpawnerBreakResult processDrops(Player player, Location location, SpawnerData spawner, boolean isCrouching, Block spawnerBlock) {
      int currentStackSize = spawner.getStackSize();
      int durabilityLoss = this.plugin.getConfig().getInt("spawner_break.durability_loss", 1);
      World world = location.getWorld();
      if (world == null) {
         return new SpawnerBreakListener.SpawnerBreakResult(false, 0, durabilityLoss);
      } else {
         ItemStack template;
         if (spawner.isItemSpawner()) {
            template = this.spawnerItemFactory.createItemSpawnerItem(spawner.getSpawnedItemMaterial());
         } else {
            EntityType entityType = spawner.getEntityType();
            template = this.spawnerItemFactory.createSmartSpawnerItem(entityType);
         }

         boolean shouldDeleteSpawner = false;
         int dropAmount;
         if (isCrouching) {
            if (currentStackSize <= 64) {
               dropAmount = currentStackSize;
               if (this.callAPIEvent(player, location, currentStackSize)) {
                  return new SpawnerBreakListener.SpawnerBreakResult(false, currentStackSize, 0);
               }
            } else {
               dropAmount = 64;
               if (this.callAPIEvent(player, location, dropAmount)) {
                  return new SpawnerBreakListener.SpawnerBreakResult(false, dropAmount, 0);
               }

               spawner.setStackSize(currentStackSize - 64);
            }
         } else {
            dropAmount = 1;
            if (this.callAPIEvent(player, location, dropAmount)) {
               return new SpawnerBreakListener.SpawnerBreakResult(false, dropAmount, 0);
            }

            if (currentStackSize <= 1) {
               shouldDeleteSpawner = true;
            } else {
               spawner.setStackSize(currentStackSize - 1);
            }
         }

         if (dropAmount != currentStackSize && !shouldDeleteSpawner) {
            this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
         } else {
            this.cleanupSpawner(spawnerBlock, spawner);
         }

         boolean directToInventory = this.plugin.getConfig().getBoolean("spawner_break.direct_to_inventory", false);
         if (directToInventory) {
            this.giveSpawnersToPlayer(player, dropAmount, template);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.2F);
         } else {
            template.setAmount(dropAmount);
            world.dropItemNaturally(location.toCenterLocation(), template.clone());
         }

         return new SpawnerBreakListener.SpawnerBreakResult(true, dropAmount, durabilityLoss);
      }
   }

   private boolean callAPIEvent(Player player, Location location, int dropAmount) {
      if (SpawnerPlayerBreakEvent.getHandlerList().getRegisteredListeners().length != 0) {
         SpawnerPlayerBreakEvent e = new SpawnerPlayerBreakEvent(player, location, dropAmount);
         Bukkit.getPluginManager().callEvent(e);
         return e.isCancelled();
      } else {
         return false;
      }
   }

   private void reduceDurability(ItemStack tool, Player player, int durabilityLoss) {
      if (tool.getType().getMaxDurability() != 0) {
         ItemMeta meta = tool.getItemMeta();
         if (!meta.isUnbreakable()) {
            if (meta instanceof Damageable) {
               Damageable damageable = (Damageable)meta;
               int currentDurability = damageable.getDamage();
               int newDurability = currentDurability + durabilityLoss;
               if (newDurability >= tool.getType().getMaxDurability()) {
                  player.getInventory().setItemInMainHand((ItemStack)null);
                  player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
               } else {
                  damageable.setDamage(newDurability);
                  tool.setItemMeta(meta);
               }
            }

         }
      }
   }

   private void cleanupSpawner(Block block, SpawnerData spawner) {
      spawner.getSpawnerStop().set(true);
      block.setType(Material.AIR);
      String spawnerId = spawner.getSpawnerId();
      this.plugin.getRangeChecker().deactivateSpawner(spawner);
      this.spawnerManager.removeSpawner(spawnerId);
      this.spawnerManager.markSpawnerDeleted(spawnerId);
      Location location = block.getLocation();
      this.locationLockManager.removeLock(location);
   }

   private boolean isValidTool(ItemStack tool) {
      return tool == null ? false : this.plugin.getConfig().getStringList("spawner_break.required_tools").contains(tool.getType().name());
   }

   private void giveSpawnersToPlayer(Player player, int amount, ItemStack template) {
      int MAX_STACK_SIZE = true;
      ItemStack itemToGive = template.clone();
      itemToGive.setAmount(Math.min(amount, 64));
      Map<Integer, ItemStack> failedItems = player.getInventory().addItem(new ItemStack[]{itemToGive});
      if (!failedItems.isEmpty()) {
         Iterator var7 = failedItems.values().iterator();

         while(var7.hasNext()) {
            ItemStack failedItem = (ItemStack)var7.next();
            player.getWorld().dropItemNaturally(player.getLocation().toCenterLocation(), failedItem);
         }

         this.messageService.sendMessage(player, "inventory_full_items_dropped");
      }

      player.updateInventory();
   }

   @EventHandler
   public void onSpawnerDamage(BlockDamageEvent event) {
      Block block = event.getBlock();
      Player player = event.getPlayer();
      if (block.getType() == Material.SPAWNER) {
         if (player.getGameMode() != GameMode.CREATIVE) {
            ItemStack tool = player.getInventory().getItemInMainHand();
            if (tool.getType() != Material.AIR) {
               SpawnerData spawner = this.spawnerManager.getSpawnerByLocation(block.getLocation());
               if (spawner != null) {
                  this.messageService.sendMessage(player, "spawner_break_warning");
               }

               if (this.isValidTool(tool)) {
                  if (this.plugin.getConfig().getBoolean("spawner_break.silk_touch.required", true)) {
                     int requiredLevel = this.plugin.getConfig().getInt("spawner_break.silk_touch.level", 1);
                     if (tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) < requiredLevel) {
                        this.messageService.sendMessage(player, "spawner_break_silk_touch_required");
                        return;
                     }
                  }

                  if (!player.hasPermission("smartspawner.break")) {
                     this.messageService.sendMessage(player, "spawner_break_no_permission");
                  }
               } else {
                  this.messageService.sendMessage(player, "spawner_break_required_tools");
               }

            }
         }
      }
   }

   public void cleanupAssociatedHopper(Block block) {
      Block blockBelow = block.getRelative(BlockFace.DOWN);
      if (this.plugin.getHopperConfig().isHopperEnabled() && blockBelow.getType() == Material.HOPPER) {
         this.hopperService.getRegistry().remove(new BlockPos(blockBelow.getLocation()));
      }

   }

   private static class SpawnerBreakResult {
      private final boolean success;
      private final int droppedAmount;
      private final int baseDurabilityLoss;

      public SpawnerBreakResult(boolean success, int droppedAmount, int baseDurabilityLoss) {
         this.success = success;
         this.droppedAmount = droppedAmount;
         this.baseDurabilityLoss = baseDurabilityLoss;
      }

      public int getDurabilityLoss() {
         return this.droppedAmount * this.baseDurabilityLoss;
      }

      @Generated
      public boolean isSuccess() {
         return this.success;
      }
   }
}
