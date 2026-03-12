package com.nisovin.shopkeepers.shopobjects.block.base;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.bukkit.MutableBlockLocation;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.interaction.InteractionUtils;
import com.nisovin.shopkeepers.util.interaction.TestPlayerInteractEvent;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

class BaseBlockShopListener implements Listener {
   private static final BlockFace[] BLOCK_SIDES = (BlockFace[])BlockFaceUtils.getBlockSides().toArray(new BlockFace[0]);
   private static final BlockFace[] PHYSICS_BLOCK_FACES;
   private static final MutableBlockLocation SHARED_BLOCK_LOCATION;
   private final SKShopkeepersPlugin plugin;
   private final BaseBlockShops baseBlockShops;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final Map<BlockLocation, Integer> cancelledBlockPhysics = new HashMap();

   BaseBlockShopListener(SKShopkeepersPlugin plugin, BaseBlockShops blockShops) {
      this.plugin = plugin;
      this.baseBlockShops = blockShops;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
   }

   void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      EventUtils.enforceExecuteFirst(PlayerInteractEvent.class, EventPriority.LOWEST, (Plugin)this.plugin);
   }

   void onDisable() {
      HandlerList.unregisterAll(this);
   }

   void addBlockPhysicsCancellation(Block block) {
      MutableBlockLocation blockLocation = SHARED_BLOCK_LOCATION;
      blockLocation.set(block);
      this.addBlockPhysicsCancellation((BlockLocation)blockLocation);
   }

   void removeBlockPhysicsCancellation(Block block) {
      MutableBlockLocation blockLocation = SHARED_BLOCK_LOCATION;
      blockLocation.set(block);
      this.removeBlockPhysicsCancellation((BlockLocation)blockLocation);
   }

   void addBlockPhysicsCancellation(BlockLocation blockLocation) {
      BlockFace[] var2 = PHYSICS_BLOCK_FACES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace blockFace = var2[var4];
         int adjacentX = blockLocation.getX() + blockFace.getModX();
         int adjacentY = blockLocation.getY() + blockFace.getModY();
         int adjacentZ = blockLocation.getZ() + blockFace.getModZ();
         BlockLocation adjacentBlockLocation = new BlockLocation((String)Unsafe.assertNonNull(blockLocation.getWorldName()), adjacentX, adjacentY, adjacentZ);
         this.addSpecificBlockPhysicsCancellation(adjacentBlockLocation);
      }

   }

   void removeBlockPhysicsCancellation(BlockLocation blockLocation) {
      BlockFace[] var2 = PHYSICS_BLOCK_FACES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace blockFace = var2[var4];
         int adjacentX = blockLocation.getX() + blockFace.getModX();
         int adjacentY = blockLocation.getY() + blockFace.getModY();
         int adjacentZ = blockLocation.getZ() + blockFace.getModZ();
         BlockLocation adjacentBlockLocation = new BlockLocation((String)Unsafe.assertNonNull(blockLocation.getWorldName()), adjacentX, adjacentY, adjacentZ);
         this.removeSpecificBlockPhysicsCancellation(adjacentBlockLocation);
      }

   }

   private void addSpecificBlockPhysicsCancellation(BlockLocation blockLocation) {
      this.cancelledBlockPhysics.compute(blockLocation.immutable(), (k, v) -> {
         return v == null ? 1 : v + 1;
      });
   }

   private void removeSpecificBlockPhysicsCancellation(BlockLocation blockLocation) {
      this.cancelledBlockPhysics.compute(blockLocation.immutable(), (k, v) -> {
         return v != null && v > 1 ? v - 1 : null;
      });
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onPlayerInteract(PlayerInteractEvent event) {
      if (!(event instanceof TestPlayerInteractEvent)) {
         if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = (Block)Unsafe.assertNonNull(event.getClickedBlock());
            Log.debug(() -> {
               String var10000 = player.getName();
               return "Player " + var10000 + " is interacting (" + String.valueOf(event.getHand()) + ") with block at " + TextUtils.getLocationString(block);
            });
            AbstractShopkeeper shopkeeper = this.shopkeeperRegistry.getShopkeeperByBlock(block);
            if (shopkeeper == null) {
               Log.debug("  Non-shopkeeper");
            } else if (!this.baseBlockShops.isBaseBlockShop((Shopkeeper)shopkeeper)) {
               Log.debug("  Not using default block shop behaviors");
            } else {
               boolean useInteractedBlock = event.useInteractedBlock() != Result.DENY;
               event.setCancelled(true);
               player.updateInventory();
               if (!useInteractedBlock) {
                  Log.debug("  Ignoring already cancelled block interaction");
               } else if (event.getHand() != EquipmentSlot.HAND) {
                  Log.debug("  Ignoring off-hand interaction");
               } else if (Settings.checkShopInteractionResult && !InteractionUtils.checkBlockInteract(player, block, false)) {
                  Log.debug("  Cancelled by another plugin");
               } else {
                  shopkeeper.onPlayerInteraction(player);
               }
            }
         }
      }
   }

   private boolean isProtectedBlock(Block block) {
      if (this.baseBlockShops.isBaseBlockShop(block)) {
         return true;
      } else {
         String worldName = block.getWorld().getName();
         int blockX = block.getX();
         int blockY = block.getY();
         int blockZ = block.getZ();
         BlockFace[] var6 = BLOCK_SIDES;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            BlockFace blockFace = var6[var8];
            int adjacentX = blockX + blockFace.getModX();
            int adjacentY = blockY + blockFace.getModY();
            int adjacentZ = blockZ + blockFace.getModZ();
            Shopkeeper shopkeeper = this.shopkeeperRegistry.getShopkeeperByBlock(worldName, adjacentX, adjacentY, adjacentZ);
            if (shopkeeper != null && this.baseBlockShops.isBaseBlockShop((Shopkeeper)shopkeeper)) {
               BaseBlockShopObject blockShop = (BaseBlockShopObject)shopkeeper.getShopObject();
               BlockFace attachedFace = blockShop.getAttachedBlockFace();
               if (blockFace == attachedFace) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockBreak(BlockBreakEvent event) {
      Block block = event.getBlock();
      if (this.isProtectedBlock(block)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockPlace(BlockPlaceEvent event) {
      Block block = event.getBlock();
      if (this.baseBlockShops.isBaseBlockShop(block)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockPhysics(BlockPhysicsEvent event) {
      Block block = event.getBlock();
      World world = block.getWorld();
      String worldName = world.getName();
      int blockX = block.getX();
      int blockY = block.getY();
      int blockZ = block.getZ();
      MutableBlockLocation blockLocation = SHARED_BLOCK_LOCATION;
      SHARED_BLOCK_LOCATION.set(worldName, blockX, blockY, blockZ);
      if (this.cancelledBlockPhysics.containsKey(blockLocation)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onEntityExplosion(EntityExplodeEvent event) {
      List<Block> blockList = event.blockList();
      this.removeProtectedBlocks(blockList);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockExplosion(BlockExplodeEvent event) {
      List<Block> blockList = event.blockList();
      this.removeProtectedBlocks(blockList);
   }

   private void removeProtectedBlocks(List<? extends Block> blockList) {
      assert blockList != null;

      blockList.removeIf(this::isProtectedBlock);
   }

   static {
      PHYSICS_BLOCK_FACES = (BlockFace[])Stream.concat(Stream.of(BlockFace.SELF), BlockFaceUtils.getBlockSides().stream()).toArray((length) -> {
         return new BlockFace[length];
      });
      SHARED_BLOCK_LOCATION = new MutableBlockLocation();
   }
}
