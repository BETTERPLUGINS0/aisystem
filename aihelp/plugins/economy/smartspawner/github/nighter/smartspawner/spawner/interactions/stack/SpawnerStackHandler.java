package github.nighter.smartspawner.spawner.interactions.stack;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerStackEvent;
import github.nighter.smartspawner.hooks.protections.CheckStackBlock;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.utils.SpawnerTypeChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerStackHandler {
   private static final long STACK_COOLDOWN = 250L;
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerManager spawnerManager;
   private final Map<UUID, Long> lastStackTime;
   private final Map<Location, UUID> stackLocks;

   public SpawnerStackHandler(SmartSpawner plugin) {
      this.plugin = plugin;
      this.messageService = plugin.getMessageService();
      this.spawnerManager = plugin.getSpawnerManager();
      this.lastStackTime = new ConcurrentHashMap();
      this.stackLocks = new ConcurrentHashMap();
      this.startCleanupTask();
   }

   private void startCleanupTask() {
      Scheduler.runTaskTimer(() -> {
         long now = System.currentTimeMillis();
         this.lastStackTime.entrySet().removeIf((entry) -> {
            return now - (Long)entry.getValue() > 10000L;
         });
         this.stackLocks.entrySet().removeIf((entry) -> {
            return this.plugin.getServer().getPlayer((UUID)entry.getValue()) == null;
         });
      }, 200L, 200L);
   }

   public void handleSpawnerStacking(Player player, Block block, SpawnerData spawnerData, ItemStack itemInHand) {
      if (!this.isOnCooldown(player)) {
         if (this.acquireStackLock(player, block.getLocation())) {
            try {
               this.handleSpawnerStack(player, spawnerData, itemInHand, player.isSneaking());
            } finally {
               this.releaseStackLock(block.getLocation());
               this.updateLastStackTime(player);
            }

         }
      }
   }

   private boolean isOnCooldown(Player player) {
      long lastTime = (Long)this.lastStackTime.getOrDefault(player.getUniqueId(), 0L);
      return System.currentTimeMillis() - lastTime < 250L;
   }

   private void updateLastStackTime(Player player) {
      this.lastStackTime.put(player.getUniqueId(), System.currentTimeMillis());
   }

   private boolean acquireStackLock(Player player, Location location) {
      return this.stackLocks.putIfAbsent(location, player.getUniqueId()) == null;
   }

   private void releaseStackLock(Location location) {
      this.stackLocks.remove(location);
   }

   public boolean handleSpawnerStack(Player player, SpawnerData targetSpawner, ItemStack itemInHand, boolean stackAll) {
      if (itemInHand.getType() != Material.SPAWNER) {
         return false;
      } else {
         Location location = targetSpawner.getSpawnerLocation();
         if (!this.hasStackPermissions(player, location)) {
            return false;
         } else if (SpawnerTypeChecker.isVanillaSpawner(itemInHand)) {
            this.messageService.sendMessage(player, "spawner_invalid");
            return false;
         } else {
            boolean isItemSpawnerItem = SpawnerTypeChecker.isItemSpawner(itemInHand);
            boolean isTargetItemSpawner = targetSpawner.isItemSpawner();
            if (isItemSpawnerItem != isTargetItemSpawner) {
               this.messageService.sendMessage(player, "spawner_different");
               return false;
            } else {
               if (isItemSpawnerItem && isTargetItemSpawner) {
                  Material handItemMaterial = SpawnerTypeChecker.getItemSpawnerMaterial(itemInHand);
                  Material targetItemMaterial = targetSpawner.getSpawnedItemMaterial();
                  if (handItemMaterial == null || targetItemMaterial == null || handItemMaterial != targetItemMaterial) {
                     this.messageService.sendMessage(player, "spawner_different");
                     return false;
                  }
               } else {
                  Optional<EntityType> handEntityTypeOpt = this.getEntityTypeFromItem(itemInHand);
                  if (!handEntityTypeOpt.isPresent()) {
                     this.messageService.sendMessage(player, "spawner_invalid");
                     return false;
                  }

                  EntityType handEntityType = (EntityType)handEntityTypeOpt.get();
                  EntityType targetEntityType = targetSpawner.getEntityType();
                  if (handEntityType != targetEntityType) {
                     this.messageService.sendMessage(player, "spawner_different");
                     return false;
                  }
               }

               int maxStackSize = targetSpawner.getMaxStackSize();
               int currentStack = targetSpawner.getStackSize();
               Map<String, String> placeholders = new HashMap();
               placeholders.put("max", String.valueOf(maxStackSize));
               if (currentStack >= maxStackSize) {
                  this.messageService.sendMessage((Player)player, "spawner_stack_full", placeholders);
                  return false;
               } else {
                  return this.processStackAddition(player, targetSpawner, itemInHand, stackAll, currentStack, maxStackSize);
               }
            }
         }
      }
   }

   private boolean hasStackPermissions(Player player, Location location) {
      if (!CheckStackBlock.CanPlayerPlaceBlock(player, location)) {
         this.messageService.sendMessage(player, "spawner_protected");
         return false;
      } else if (!player.hasPermission("smartspawner.stack")) {
         this.messageService.sendMessage(player, "no_permission");
         return false;
      } else {
         return true;
      }
   }

   public Optional<EntityType> getEntityTypeFromItem(ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return Optional.empty();
      } else {
         if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockMeta = (BlockStateMeta)meta;
            if (blockMeta.hasBlockState()) {
               BlockState var5 = blockMeta.getBlockState();
               if (var5 instanceof CreatureSpawner) {
                  CreatureSpawner handSpawner = (CreatureSpawner)var5;
                  EntityType entityType = handSpawner.getSpawnedType();
                  if (entityType != null) {
                     return Optional.of(entityType);
                  }
               }
            }
         }

         return Optional.empty();
      }
   }

   private boolean processStackAddition(Player player, SpawnerData targetSpawner, ItemStack itemInHand, boolean stackAll, int currentStack, int maxStackSize) {
      int itemAmount = itemInHand.getAmount();
      int spaceLeft = maxStackSize - currentStack;
      int amountToStack = stackAll ? Math.min(spaceLeft, itemAmount) : 1;
      int newStack = currentStack + amountToStack;
      if (SpawnerStackEvent.getHandlerList().getRegisteredListeners().length != 0) {
         SpawnerStackEvent e = new SpawnerStackEvent(player, targetSpawner.getSpawnerLocation(), currentStack, newStack);
         Bukkit.getPluginManager().callEvent(e);
         if (e.isCancelled()) {
            return false;
         }
      }

      targetSpawner.setStackSize(newStack);
      this.spawnerManager.markSpawnerModified(targetSpawner.getSpawnerId());
      if (targetSpawner.getIsAtCapacity()) {
         targetSpawner.setIsAtCapacity(false);
      }

      targetSpawner.updateLastInteractedPlayer(player.getName());
      this.updatePlayerInventory(player, itemInHand, amountToStack);
      this.showStackAnimation(targetSpawner, newStack, player);
      return true;
   }

   private void updatePlayerInventory(Player player, ItemStack itemInHand, int amountUsed) {
      if (player.getGameMode() != GameMode.CREATIVE) {
         int remainingAmount = itemInHand.getAmount() - amountUsed;
         if (remainingAmount <= 0) {
            player.getInventory().setItemInMainHand((ItemStack)null);
         } else {
            itemInHand.setAmount(remainingAmount);
         }
      }

   }

   private void showStackAnimation(SpawnerData spawner, int newStack, Player player) {
      if (this.plugin.getConfig().getBoolean("particle.spawner_stack", true)) {
         Location loc = spawner.getSpawnerLocation();
         World world = loc.getWorld();
         if (world != null) {
            Scheduler.runLocationTask(loc, () -> {
               world.spawnParticle(Particle.HAPPY_VILLAGER, loc.clone().add(0.5D, 0.5D, 0.5D), 10, 0.3D, 0.3D, 0.3D, 0.0D);
            });
         }
      }

      Map<String, String> placeholders = new HashMap();
      placeholders.put("amount", String.valueOf(newStack));
      this.messageService.sendMessage((Player)player, "spawner_stack_success", placeholders);
   }
}
