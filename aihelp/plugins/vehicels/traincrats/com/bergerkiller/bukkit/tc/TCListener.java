package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.collections.EntityMap;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.events.ChunkLoadEntitiesEvent;
import com.bergerkiller.bukkit.common.events.EntityAddEvent;
import com.bergerkiller.bukkit.common.events.EntityRemoveFromServerEvent;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.control.light.LightAPIController;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.debug.DebugTool;
import com.bergerkiller.bukkit.tc.editor.TCMapControl;
import com.bergerkiller.bukkit.tc.events.signactions.SignActionRegisterEvent;
import com.bergerkiller.bukkit.tc.events.signactions.SignActionUnregisterEvent;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroup;
import com.bergerkiller.bukkit.tc.pathfinding.PathNode;
import com.bergerkiller.bukkit.tc.portals.PortalDestination;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Rails;

public class TCListener implements Listener {
   private static final boolean DEBUG_DO_TRACKTEST = false;
   private static final boolean DEBUG_DO_INVISIBLE_TRACK = false;
   private static final boolean MUST_CHECK_PLAYER_TAKE = !Common.hasCapability("Common:EntityController:isPlayerTakeable");
   private static final long SIGN_CLICK_INTERVAL = 500L;
   private static final long MAX_INTERACT_INTERVAL = 300L;
   public static boolean cancelNextDrops = false;
   public static MinecartMember<?> killedByMember = null;
   private final TrainCarts plugin;
   private EntityMap<Player, Long> lastHitTimes = new EntityMap();
   private EntityMap<Player, BlockFace> lastClickedDirection = new EntityMap();

   public TCListener(TrainCarts plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerQuit(PlayerQuitEvent event) {
      if (MUST_CHECK_PLAYER_TAKE) {
         MinecartMember<?> vehicle = MinecartMemberStore.getFromEntity(event.getPlayer().getVehicle());
         if (vehicle != null && !vehicle.isPlayerTakeable()) {
            ((CommonMinecart)vehicle.getEntity()).removePassenger(event.getPlayer());
         }
      }

      FakePlayerSpawner.onViewerQuit(event.getPlayer());
      this.plugin.getTeamProvider().reset(event.getPlayer());
      this.plugin.getAttachmentViewers().remove(event.getPlayer());
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onItemSpawn(ItemSpawnEvent event) {
      if (cancelNextDrops) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onChunkUnload(ChunkUnloadEvent event) {
      this.plugin.getOfflineGroups().unloadChunk(event.getChunk());
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onChunkLoadEntities(ChunkLoadEntitiesEvent event) {
      this.plugin.getOfflineGroups().loadChunk(event.getChunk());
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onWorldLoad(WorldLoadEvent event) {
      this.plugin.getOfflineGroups().refresh(event.getWorld());
      Map<OfflineGroup, List<ForcedChunk>> chunks = this.plugin.getOfflineGroups().getForceLoadedChunks(event.getWorld());
      if (!chunks.isEmpty()) {
         this.plugin.log(Level.INFO, "Restoring trains and loading nearby chunks on world " + event.getWorld().getName() + "...");
         this.plugin.preloadChunks(chunks);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onWorldUnload(WorldUnloadEvent event) {
      this.plugin.getOfflineGroups().unloadWorld(event.getWorld());
      if (Bukkit.getPluginManager().isPluginEnabled("LightAPI")) {
         disableLightAPIWorld(event.getWorld());
      }

      TCConfig.enabledWorlds.onWorldUnloaded(event.getWorld());
      TCConfig.disabledWorlds.onWorldUnloaded(event.getWorld());
   }

   private static void disableLightAPIWorld(World world) {
      LightAPIController.disableWorld(world);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
      this.plugin.getTeamProvider().reset(event.getPlayer());
   }

   @EventHandler(
      priority = EventPriority.NORMAL
   )
   public void onPlayerDeath(PlayerDeathEvent event) {
      if (killedByMember != null) {
         String deathMessage = killedByMember.getGroup().getProperties().getKillMessage();
         if (!deathMessage.isEmpty()) {
            deathMessage = deathMessage.replaceAll("%0%", event.getEntity().getDisplayName());
            deathMessage = deathMessage.replaceAll("%1%", killedByMember.getGroup().getProperties().getDisplayName());
            event.setDeathMessage(deathMessage);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onEntityAdd(EntityAddEvent event) {
      if (MinecartMemberStore.canConvertAutomatically(event.getEntity())) {
         MinecartMemberStore.convert(this.plugin, (Minecart)event.getEntity());
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onEntityRemoveFromServer(EntityRemoveFromServerEvent event) {
      if (event.getEntity() instanceof Minecart) {
         UUID entityUUID = event.getEntity().getUniqueId();
         if (EntityUtil.getEntity(event.getEntity().getWorld(), entityUUID) != null) {
            return;
         }

         if (EntityHandle.fromBukkit(event.getEntity()).isDestroyed()) {
            this.plugin.getOfflineGroups().removeMember(entityUUID);
         } else {
            MinecartMember<?> member = MinecartMemberStore.getFromEntity(event.getEntity());
            if (member == null) {
               return;
            }

            MinecartGroup group = member.getGroup();
            if (group == null) {
               return;
            }

            if (group.canUnload()) {
               this.plugin.log(Level.WARNING, "Train '" + group.getProperties().getTrainName() + "' forcibly unloaded!");
            } else {
               this.plugin.log(Level.WARNING, "Train '" + group.getProperties().getTrainName() + "' had to be restored after unexpected unload");
            }

            group.unload();
            CommonUtil.nextTick(new Runnable() {
               public void run() {
                  TCListener.this.plugin.getOfflineGroups().refresh();
               }
            });
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onVehicleDamageByEntity(EntityDamageByEntityEvent event) {
      if (this.isCartDamageCancelled(event.getEntity(), event.getDamager())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onVehicleDamage(VehicleDamageEvent event) {
      if (this.isCartDamageCancelled(event.getVehicle(), event.getAttacker())) {
         event.setCancelled(true);
      }

   }

   private boolean isCartDamageCancelled(Entity vehicle, Entity attacker) {
      MinecartMember<?> mm = MinecartMemberStore.getFromEntity(vehicle);
      if (mm == null) {
         return false;
      } else {
         if (attacker instanceof Projectile) {
            attacker = (Entity)((Projectile)attacker).getShooter();
         }

         boolean breakAny = attacker instanceof Player && Permission.BREAK_MINECART_ANY.has((Player)attacker);
         if (mm.getProperties().isInvincible() && !breakAny) {
            return true;
         } else {
            if (attacker instanceof Player) {
               Player p = (Player)attacker;
               if (!breakAny && (!mm.getProperties().hasOwnership(p) || !Permission.BREAK_MINECART_SELF.has(p))) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
      if (!TrainCarts.isWorldDisabled(event.getVehicle().getWorld())) {
         try {
            MinecartMember<?> member = MinecartMemberStore.getFromEntity(event.getVehicle());
            if (member != null) {
               event.setCancelled(!member.onEntityCollision(event.getEntity()));
            }
         } catch (Throwable var3) {
            this.plugin.handle(var3);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = false
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (!TrainCarts.isWorldDisabled(event.getPlayer().getWorld())) {
         if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && DebugTool.onDebugInteract(this.plugin, event.getPlayer(), event.getClickedBlock(), event.getItem(), false)) {
            event.setUseInteractedBlock(Result.DENY);
         } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack heldItem = HumanHand.getItemInMainHand(event.getPlayer());
            if (TCMapControl.isTCMapItem(event.getItem())) {
               if (event.getClickedBlock() != null) {
                  CommonItemStack.of(event.getItem()).updateCustomData((tag) -> {
                     tag.putBlockLocation("selected", new BlockLocation(event.getClickedBlock()));
                  });
               }

               TCMapControl.updateMapItem(event.getPlayer(), true);
               event.setUseInteractedBlock(Result.DENY);
               event.setUseItemInHand(Result.DENY);
            } else {
               try {
                  Block clickedBlock = event.getClickedBlock();
                  if (clickedBlock == null) {
                     clickedBlock = CommonEntity.get(event.getPlayer()).getTargetBlock();
                  }

                  if (DebugTool.onDebugInteract(this.plugin, event.getPlayer(), clickedBlock, event.getItem(), true)) {
                     event.setUseInteractedBlock(Result.DENY);
                     return;
                  }

                  if (clickedBlock == null) {
                     return;
                  }

                  if (event.getItem() == null) {
                     Material var10000 = Material.AIR;
                  } else {
                     event.getItem().getType();
                  }

                  long lastHitTime = (Long)this.lastHitTimes.getOrDefault(event.getPlayer(), Long.MIN_VALUE);
                  long time = System.currentTimeMillis();
                  long clickInterval = time - lastHitTime;
                  this.lastHitTimes.put(event.getPlayer(), time);
                  this.handleRailPlacement(event, heldItem);
                  if (!event.isCancelled() && !this.onRightClick(clickedBlock, event.getPlayer(), heldItem, clickInterval)) {
                     event.setUseItemInHand(Result.DENY);
                     event.setUseInteractedBlock(Result.DENY);
                     event.setCancelled(true);
                  }
               } catch (Throwable var11) {
                  this.plugin.handle(var11);
               }

            }
         }
      }
   }

   private void handleRailPlacement(PlayerInteractEvent event, ItemStack heldItem) {
      if (event.getClickedBlock() != null && heldItem != null) {
         if (!(Boolean)MaterialUtil.ISINTERACTABLE.get(event.getClickedBlock()) || event.getPlayer().isSneaking()) {
            Block placedBlock = event.getClickedBlock().getRelative(event.getBlockFace());
            if ((Boolean)MaterialUtil.ISAIR.get(placedBlock)) {
               Material railType = heldItem.getType();
               if (MaterialUtil.ISRAILS.get(railType) && TCConfig.allowUpsideDownRails) {
                  Block below = placedBlock.getRelative(BlockFace.DOWN);
                  Block above = placedBlock.getRelative(BlockFace.UP);
                  if (((Boolean)MaterialUtil.ISAIR.get(below) || (Boolean)Util.ISVERTRAIL.get(below)) && Util.isUpsideDownRailSupport(above)) {
                     BlockPlaceEvent placeEvent = new BlockPlaceEvent(placedBlock, placedBlock.getState(), event.getClickedBlock(), heldItem.clone(), event.getPlayer(), true);
                     BlockData railData = BlockData.fromMaterial(railType);
                     WorldUtil.setBlockDataFast(placedBlock, railData);
                     WorldUtil.queueBlockSend(placedBlock);
                     CommonUtil.callEvent(placeEvent);
                     if (!placeEvent.isCancelled() && placeEvent.canBuild()) {
                        this.plugin.applyBlockPhysics(placedBlock, railData);
                        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                           ItemStack oldItem = HumanHand.getItemInMainHand(event.getPlayer());
                           if (oldItem != null && oldItem.getAmount() > 1) {
                              oldItem = oldItem.clone();
                              oldItem.setAmount(oldItem.getAmount() - 1);
                           } else {
                              oldItem = null;
                           }

                           HumanHand.setItemInMainHand(event.getPlayer(), oldItem);
                        }

                        WorldUtil.playSound(event.getClickedBlock().getLocation(), railData.getPlaceSound(), 1.0F, 1.0F);
                        event.setUseItemInHand(Result.DENY);
                        event.setUseInteractedBlock(Result.DENY);
                        event.setCancelled(true);
                     } else {
                        WorldUtil.setBlockDataFast(placedBlock, BlockData.AIR);
                     }
                  }
               }

            }
         }
      }
   }

   public boolean onRightClick(Block clickedBlock, Player player, ItemStack heldItem, long clickInterval) {
      if (clickedBlock != null && ((Boolean)MaterialUtil.ISMINECART.get(heldItem) || (Boolean)Util.ISTCRAIL.get(heldItem))) {
         BlockData type = WorldUtil.getBlockData(clickedBlock);
         RailType railType = RailType.getType(clickedBlock, type);
         if (railType != RailType.NONE) {
            if ((Boolean)MaterialUtil.ISMINECART.get(heldItem)) {
               return this.handleMinecartPlacement(player, clickedBlock);
            }

            if (type.isType(heldItem.getType()) && MaterialUtil.ISRAILS.get(type) && TCConfig.allowRailEditing && clickInterval >= 300L && BlockUtil.canBuildBlock(clickedBlock, type)) {
               BlockFace direction = FaceUtil.getDirection(player.getLocation().getDirection(), false);
               BlockFace lastDirection = (BlockFace)this.lastClickedDirection.getOrDefault(player, direction);
               Rails rails = BlockUtil.getRails(clickedBlock);
               if (BlockUtil.isSolid(clickedBlock.getRelative(direction))) {
                  if (rails.isOnSlope()) {
                     if (rails.getDirection() == direction) {
                        rails.setDirection(direction, false);
                     } else {
                        rails.setDirection(direction, true);
                     }
                  } else {
                     rails.setDirection(direction, true);
                  }
               } else if (RailType.REGULAR.isRail(type)) {
                  BlockFace[] faces = FaceUtil.getFaces(rails.getDirection());
                  if (!LogicUtil.contains(direction.getOppositeFace(), faces)) {
                     BlockFace otherFace = faces[0] == lastDirection.getOppositeFace() ? faces[0] : faces[1];
                     rails.setDirection(FaceUtil.combine(otherFace, direction.getOppositeFace()), false);
                  }
               } else {
                  rails.setDirection(direction, false);
               }

               TrainCarts.plugin.setBlockDataWithoutBreaking(clickedBlock, BlockData.fromMaterialData(rails));
               this.lastClickedDirection.put(player, direction);
            }
         }
      }

      return !(Boolean)MaterialUtil.ISSIGN.get(clickedBlock) || clickInterval < 500L || !SignAction.handleClick(clickedBlock, player);
   }

   private boolean handleMinecartPlacement(Player player, Block clickedBlock) {
      if (!Permission.GENERAL_PLACE_MINECART.has(player)) {
         return false;
      } else {
         RailType clickedRailType = RailType.getType(clickedBlock);
         if (clickedRailType == RailType.NONE) {
            return true;
         } else if (!TCConfig.allMinecartsAreTrainCarts && !Permission.GENERAL_PLACE_TRAINCART.has(player)) {
            return true;
         } else {
            BlockFace orientation = FaceUtil.vectorToBlockFace(player.getEyeLocation().getDirection().setY(0.0D), false);
            Location at = clickedRailType.getSpawnLocation(clickedBlock, orientation);
            if (MinecartMemberStore.getAt(at, (MinecartGroup)null, 0.5D) != null) {
               return false;
            } else if (MinecartGroupStore.isPerWorldSpawnLimitReached((Location)at, 1)) {
               Localization.SPAWN_MAX_PER_WORLD.message(player, new String[0]);
               return false;
            } else {
               MinecartMemberStore.spawnBy(this.plugin, at, player);
               return false;
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      if (event.getRightClicked() instanceof Minecart) {
         Long lastHitTime = (Long)this.lastHitTimes.get(event.getPlayer());
         if (lastHitTime != null) {
            long time = System.currentTimeMillis();
            long clickInterval = time - lastHitTime;
            if (clickInterval < 300L) {
               event.setCancelled(true);
               return;
            }
         }

         if (event.getRightClicked() instanceof RideableMinecart) {
            event.setCancelled(!this.plugin.handlePlayerVehicleChange(event.getPlayer(), event.getRightClicked()));
            MinecartMember<?> newMinecart = MinecartMemberStore.getFromEntity(event.getRightClicked());
            if (!event.isCancelled() && newMinecart != null) {
               newMinecart.getAttachments().storeSeatHint(event.getPlayer());
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onBlockBreak(BlockBreakEvent event) {
      if ((Boolean)MaterialUtil.ISRAILS.get(event.getBlock())) {
         this.onRailsBreak(event.getBlock());
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onBlockPlace(BlockPlaceEvent event) {
      RailType railType = RailType.getType(event.getBlockPlaced());
      if (railType != RailType.NONE) {
         final Block placed = event.getBlockPlaced();
         CommonUtil.nextTick(new Runnable() {
            public void run() {
               BlockData blockData = WorldUtil.getBlockData(placed);
               RailType railType = RailType.getType(placed, blockData);
               if (railType != RailType.NONE) {
                  railType.onBlockPlaced(placed);
                  TCListener.this.plugin.applyBlockPhysics(placed, blockData);
               }

            }
         });
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onBlockPhysics(BlockPhysicsEvent event) {
      MinecartGroupStore.notifyPhysicsChange();
      Block block = event.getBlock();
      BlockData blockData = Util.getBlockDataOfPhysicsEvent(event);
      Iterator var4 = RailType.values().iterator();

      while(var4.hasNext()) {
         RailType type = (RailType)var4.next();
         if (type.isHandlingPhysics() && RailType.checkRailTypeIsAt(type, block, blockData)) {
            if (!type.isRailsSupported(block)) {
               this.onRailsBreak(block);
            }

            type.onBlockPhysics(event);
            RailLookup.CachedRailPiece cachedRailPiece = RailLookup.lookupCachedRailPieceIfCached(OfflineBlock.of(block), type);
            if (!cachedRailPiece.isNone()) {
               cachedRailPiece.forceCacheVerification();
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onEntityDamage(EntityDamageEvent event) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(event.getEntity().getVehicle());
      if (member != null && !member.canTakeDamage(event.getEntity(), event.getCause())) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onPlayerCreativeSetSlot(InventoryCreativeEvent event) {
      if (event.getSlotType() == SlotType.ARMOR) {
         CartAttachmentSeat seat = this.plugin.getSeatAttachmentMap().get(event.getWhoClicked().getEntityId());
         if (seat != null && seat.firstPerson.getLiveMode().isRealPlayerInvisible()) {
            event.setResult(Result.DENY);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onEntityPortal(EntityPortalEvent event) {
      MinecartMember<?> member = MinecartMemberStore.getFromEntity(event.getEntity());
      if (member != null) {
         event.setCancelled(true);
         if (TCConfig.allowNetherTeleport) {
            Location loc = event.getTo();
            if (loc != null) {
               Direction direction = Direction.fromFace(member.getDirectionFrom());
               final PortalDestination dest = PortalDestination.findDestinationAtNetherPortal(loc.getBlock(), direction);
               if (dest != null && dest.getRailsBlock() != null && dest.hasDirections()) {
                  final MinecartGroup group = member.getGroup();
                  CommonUtil.nextTick(new Runnable() {
                     public void run() {
                        group.teleport(dest.getRailsBlock(), dest.getDirections()[0]);
                     }
                  });
               }

            }
         }
      }
   }

   public void onRailsBreak(Block railsBlock) {
      MinecartMember<?> mm = MinecartMemberStore.getAt(railsBlock);
      if (mm != null) {
         mm.getGroup().getSignTracker().updatePosition();
      }

      PathNode.remove(railsBlock);
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onLateSignRegistered(SignActionRegisterEvent event) {
      this.plugin.redetectSignActions();
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onLateSignUnregistered(SignActionUnregisterEvent event) {
      this.plugin.redetectSignActions();
   }
}
