package com.nisovin.shopkeepers.container.protection;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.MutableBlockLocation;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Chest.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ProtectedContainers {
   private static final MutableBlockLocation sharedBlockLocation = new MutableBlockLocation();
   private final SKShopkeepersPlugin plugin;
   private final ContainerProtectionListener containerProtectionListener = new ContainerProtectionListener((ProtectedContainers)Unsafe.initialized(this));
   private final InventoryMoveItemListener inventoryMoveItemListener = new InventoryMoveItemListener((ProtectedContainers)Unsafe.initialized(this));
   private final Map<BlockLocation, List<AbstractPlayerShopkeeper>> protectedContainers = new HashMap();
   private final List<AbstractPlayerShopkeeper> tempResultsList = new ArrayList();

   public ProtectedContainers(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
   }

   public void enable() {
      if (Settings.protectContainers) {
         Bukkit.getPluginManager().registerEvents(this.containerProtectionListener, this.plugin);
         if (Settings.preventItemMovement) {
            Bukkit.getPluginManager().registerEvents(this.inventoryMoveItemListener, this.plugin);
         }
      }

   }

   public void disable() {
      HandlerList.unregisterAll(this.containerProtectionListener);
      HandlerList.unregisterAll(this.inventoryMoveItemListener);
      this.protectedContainers.clear();
   }

   private BlockLocation getSharedKey(String worldName, int x, int y, int z) {
      sharedBlockLocation.set(worldName, x, y, z);
      return sharedBlockLocation;
   }

   public void addContainer(BlockLocation location, AbstractPlayerShopkeeper shopkeeper) {
      Validate.notNull(location, (String)"location is null");
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      List<AbstractPlayerShopkeeper> shopkeepers = (List)this.protectedContainers.computeIfAbsent(location.immutable(), (key) -> {
         return new ArrayList(1);
      });

      assert shopkeepers != null;

      shopkeepers.add(shopkeeper);
   }

   public void removeContainer(BlockLocation location, AbstractPlayerShopkeeper shopkeeper) {
      Validate.notNull(location, (String)"location is null");
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.protectedContainers.computeIfPresent(location, (key, shopkeepers) -> {
         shopkeepers.remove(shopkeeper);
         return shopkeepers.isEmpty() ? (List)Unsafe.uncheckedNull() : shopkeepers;
      });
   }

   @Nullable
   private List<? extends AbstractPlayerShopkeeper> _getShopkeepers(String worldName, int x, int y, int z) {
      BlockLocation key = this.getSharedKey(worldName, x, y, z);
      return (List)this.protectedContainers.get(key);
   }

   @Nullable
   private List<? extends AbstractPlayerShopkeeper> _getShopkeepers(Block block) {
      return this._getShopkeepers(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public List<? extends PlayerShopkeeper> getShopkeepers(String worldName, int x, int y, int z) {
      List<? extends PlayerShopkeeper> shopkeepers = this._getShopkeepers(worldName, x, y, z);
      return shopkeepers != null ? Collections.unmodifiableList(shopkeepers) : Collections.emptyList();
   }

   public List<? extends PlayerShopkeeper> getShopkeepers(Block block) {
      return this.getShopkeepers(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public boolean isContainerDirectlyProtected(String worldName, int x, int y, int z, @Nullable Player player) {
      List<? extends PlayerShopkeeper> shopkeepers = this._getShopkeepers(worldName, x, y, z);
      if (shopkeepers == null) {
         return false;
      } else {
         assert !shopkeepers.isEmpty();

         if (player != null) {
            Iterator var7 = shopkeepers.iterator();

            while(var7.hasNext()) {
               PlayerShopkeeper shopkeeper = (PlayerShopkeeper)var7.next();
               if (shopkeeper.isOwner(player)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean isContainerDirectlyProtected(Block block, Player player) {
      return this.isContainerDirectlyProtected(block.getWorld().getName(), block.getX(), block.getY(), block.getZ(), player);
   }

   public boolean isContainerProtected(Block containerBlock, @Nullable Player player) {
      Validate.notNull(containerBlock, (String)"containerBlock is null!");
      this.getShopkeepersUsingContainer(containerBlock, this.tempResultsList);
      if (this.tempResultsList.isEmpty()) {
         return false;
      } else {
         boolean result = true;
         if (player != null) {
            Iterator var4 = this.tempResultsList.iterator();

            while(var4.hasNext()) {
               AbstractPlayerShopkeeper shopkeeper = (AbstractPlayerShopkeeper)var4.next();
               if (shopkeeper.canEdit(player, true)) {
                  result = false;
                  break;
               }
            }
         }

         this.tempResultsList.clear();
         return result;
      }
   }

   public boolean isProtectedContainer(Block block) {
      return this.isProtectedContainer(block, (Player)null);
   }

   public boolean isProtectedContainer(Block block, @Nullable Player player) {
      Validate.notNull(block, (String)"block is null");
      return !ShopContainers.isSupportedContainer(block.getType()) ? false : this.isContainerProtected(block, player);
   }

   public List<? extends PlayerShopkeeper> getShopkeepersUsingContainer(Block containerBlock) {
      return this.getShopkeepersUsingContainer(containerBlock, new ArrayList());
   }

   private List<? extends AbstractPlayerShopkeeper> getShopkeepersUsingContainer(Block containerBlock, List<AbstractPlayerShopkeeper> results) {
      Validate.notNull(containerBlock, (String)"containerBlock is null!");
      Validate.notNull(results, (String)"results is null!");
      List<? extends AbstractPlayerShopkeeper> shopkeepers = this._getShopkeepers(containerBlock);
      if (shopkeepers != null) {
         assert !shopkeepers.isEmpty();

         results.addAll(shopkeepers);
      }

      Material chestType = containerBlock.getType();
      if (ItemUtils.isChest(chestType)) {
         Chest chestData = (Chest)containerBlock.getBlockData();
         BlockFace chestFacing = chestData.getFacing();
         BlockFace connectedFace = getConnectedBlockFace(chestFacing, chestData.getType());
         if (connectedFace != null) {
            Block connectedChest = containerBlock.getRelative(connectedFace);
            shopkeepers = this._getShopkeepers(connectedChest);
            if (shopkeepers != null) {
               results.addAll(shopkeepers);
            }
         }
      }

      return results;
   }

   @Nullable
   private static BlockFace getConnectedBlockFace(BlockFace chestFacing, Type chestType) {
      switch(chestFacing) {
      case NORTH:
         switch(chestType) {
         case RIGHT:
            return BlockFace.WEST;
         case LEFT:
            return BlockFace.EAST;
         default:
            return null;
         }
      case EAST:
         switch(chestType) {
         case RIGHT:
            return BlockFace.NORTH;
         case LEFT:
            return BlockFace.SOUTH;
         default:
            return null;
         }
      case SOUTH:
         switch(chestType) {
         case RIGHT:
            return BlockFace.EAST;
         case LEFT:
            return BlockFace.WEST;
         default:
            return null;
         }
      case WEST:
         switch(chestType) {
         case RIGHT:
            return BlockFace.SOUTH;
         case LEFT:
            return BlockFace.NORTH;
         default:
            return null;
         }
      default:
         return null;
      }
   }
}
