package com.bergerkiller.bukkit.tc.controller;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.collections.ClassMap;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.controller.DefaultEntityController;
import com.bergerkiller.bukkit.common.controller.EntityNetworkController;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.CommonEntityType;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartChest;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartCommandBlock;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartHopper;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartMobSpawner;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartRideable;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartTNT;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.HumanHand;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberCommandBlock;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberHopper;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberMobSpawner;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberTNT;
import com.bergerkiller.bukkit.tc.events.MemberSpawnEvent;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.utils.PaperRedstonePhysicsChecker;
import com.bergerkiller.mountiplex.conversion.annotations.ConverterMethod;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MinecartMemberStore {
   private static ClassMap<Function<TrainCarts, ? extends MinecartMember<?>>> controllers = new ClassMap();

   public static void convertAllAutomatically(TrainCarts plugin) {
      List<Minecart> minecarts = new ArrayList();
      Iterator var2 = WorldUtil.getWorlds().iterator();

      while(true) {
         World world;
         do {
            if (!var2.hasNext()) {
               var2 = minecarts.iterator();

               while(var2.hasNext()) {
                  Minecart minecart = (Minecart)var2.next();
                  convert(plugin, minecart);
               }

               minecarts.clear();
               return;
            }

            world = (World)var2.next();
         } while(TrainCarts.isWorldDisabled(world));

         Iterator var4 = WorldUtil.getEntities(world).iterator();

         while(var4.hasNext()) {
            Entity entity = (Entity)var4.next();
            if (canConvertAutomatically(entity)) {
               minecarts.add((Minecart)entity);
            }
         }
      }
   }

   public static boolean canConvertAutomatically(Entity minecart) {
      if (!canConvert(minecart)) {
         return false;
      } else {
         TrainCarts traincarts = TrainCarts.plugin;
         return traincarts == null || TCConfig.allMinecartsAreTrainCarts || traincarts.getOfflineGroups().containsMinecart(minecart.getUniqueId());
      }
   }

   public static boolean canConvert(Entity minecart) {
      if (!(minecart instanceof Minecart)) {
         return false;
      } else if (TrainCarts.isWorldDisabled(minecart.getWorld())) {
         return false;
      } else {
         TrainCarts traincarts = TrainCarts.plugin;
         if (traincarts != null && traincarts.getOfflineGroups().isDestroyingGroupOf((Minecart)minecart)) {
            return false;
         } else {
            CommonEntity<Entity> common = CommonEntity.get(minecart);
            return common.hasControllerSupport() && common.getController() instanceof DefaultEntityController;
         }
      }
   }

   public static MinecartMember<?> convert(TrainCarts plugin, Minecart source) {
      if (plugin == null) {
         throw new IllegalArgumentException("TrainCarts plugin instance cannot be null");
      } else if (source.isDead()) {
         return null;
      } else {
         CommonEntity<?> entity = CommonEntity.get(source);
         MinecartMember newController;
         if (entity.getController() instanceof MinecartMember) {
            newController = (MinecartMember)entity.getController();
            newController.updateUnloaded();
            return newController;
         } else if (!canConvert(source)) {
            return null;
         } else {
            newController = createController(plugin, entity);
            if (newController == null) {
               return null;
            } else {
               entity.setController(newController);
               entity.setNetworkController(createNetworkController());
               newController.updateUnloaded();
               if (!newController.isUnloaded() && !plugin.getOfflineGroups().containsMinecart(entity.getUniqueId())) {
                  newController.getGroup().getProperties().setDefault();
               }

               PaperRedstonePhysicsChecker.check(source.getWorld());
               return newController;
            }
         }
      }
   }

   public static EntityNetworkController<?> createNetworkController() {
      return new MinecartMemberNetwork(TrainCarts.plugin);
   }

   public static MinecartMember<?> createController(TrainCarts plugin, EntityType entityType) {
      if (plugin == null) {
         throw new IllegalArgumentException("TrainCarts plugin cannot be null");
      } else {
         try {
            Class<?> commonType = CommonEntityType.byEntityType(entityType).commonType.getType();
            Function<TrainCarts, ? extends MinecartMember<?>> controllerConstr = (Function)controllers.get(commonType);
            return controllerConstr != null ? (MinecartMember)controllerConstr.apply(plugin) : null;
         } catch (Throwable var4) {
            plugin.handle(var4);
            return null;
         }
      }
   }

   public static MinecartMember<?> createController(TrainCarts plugin, CommonEntity<?> entity) {
      if (plugin == null) {
         throw new IllegalArgumentException("TrainCarts plugin cannot be null");
      } else {
         Function<TrainCarts, ? extends MinecartMember<?>> controllerConstr = (Function)controllers.get(entity);
         if (controllerConstr == null) {
            return null;
         } else {
            try {
               return (MinecartMember)controllerConstr.apply(plugin);
            } catch (Throwable var4) {
               plugin.handle(var4);
               return null;
            }
         }
      }
   }

   public static MinecartMember<?> spawnBy(TrainCarts plugin, Location at, Player player) {
      ItemStack item = HumanHand.getItemInMainHand(player);
      if (LogicUtil.nullOrEmpty(item)) {
         return null;
      } else {
         EntityType type = (EntityType)Conversion.toMinecartType.convert(item.getType());
         if (type == null) {
            return null;
         } else {
            if (player.getGameMode() != GameMode.CREATIVE) {
               ItemUtil.subtractAmount(item, 1);
               if (LogicUtil.nullOrEmpty(item)) {
                  HumanHand.setItemInMainHand(player, (ItemStack)null);
               } else {
                  HumanHand.setItemInMainHand(player, item);
               }
            }

            MinecartMember<?> spawned = spawn(plugin, at, type);
            if (spawned != null && !((CommonMinecart)spawned.getEntity()).isRemoved()) {
               spawned.getGroup().getProperties().setDefault(player);
               if (TCConfig.setOwnerOnPlacement) {
                  spawned.getProperties().setOwner(player);
               }

               plugin.getPlayer(player).editMember(spawned);
            }

            return spawned;
         }
      }
   }

   public static MinecartMember<?> spawn(TrainCarts plugin, Location at, EntityType type) {
      return spawn(plugin, at, type, (ConfigurationNode)null);
   }

   public static MinecartMember<?> spawn(TrainCarts plugin, Location at, EntityType type, ConfigurationNode config) {
      return spawn(plugin, at, false, type, config);
   }

   public static MinecartMember<?> spawn(TrainCarts plugin, Location at, boolean flipped, EntityType type, ConfigurationNode config) {
      MinecartMember<?> controller = createController(plugin, type);
      if (controller == null) {
         throw new IllegalArgumentException("No suitable MinecartMember type for " + type);
      } else {
         boolean disableDefaultModel = config != null && config.isNode("model");
         if (disableDefaultModel) {
            controller.getAttachments().setHidden(true);
         }

         if (flipped) {
            at = Util.invertRotation(at.clone());
         }

         EntityNetworkController<?> networkController = createNetworkController();
         if (networkController instanceof MinecartMemberNetwork) {
            ((MinecartMemberNetwork)networkController).setInProcessOfSpawning(true);
         }

         CommonEntity.spawn(type, at, controller, networkController);
         if (networkController instanceof MinecartMemberNetwork) {
            ((MinecartMemberNetwork)networkController).setInProcessOfSpawning(false);
         }

         controller.setDirectionForward(flipped);
         controller.updateDirection();
         MinecartMember<?> result = MemberSpawnEvent.call(controller).getMember();
         if (config != null) {
            controller.setUnloaded(true);
            controller.getProperties().load(config);
            ConfigurationNode dataNode = config.getNodeIfExists("data");
            if (dataNode != null) {
               controller.onTrainSpawned(dataNode);
            }
         }

         if (disableDefaultModel) {
            controller.getAttachments().setHidden(false);
         }

         controller.setUnloaded(false);
         PaperRedstonePhysicsChecker.check(at.getWorld());
         return result;
      }
   }

   @ConverterMethod
   public static MinecartMember<?> getFromUID(UUID uuid) {
      Iterator var1 = WorldUtil.getWorlds().iterator();

      while(var1.hasNext()) {
         World world = (World)var1.next();
         if (!TrainCarts.isWorldDisabled(world)) {
            MinecartMember<?> member = getFromEntity(EntityUtil.getEntity(world, uuid));
            if (member != null && member.getEntity() != null) {
               return member;
            }
         }
      }

      return null;
   }

   @ConverterMethod
   public static MinecartMember<?> getFromEntity(Entity entity) {
      if (entity instanceof Minecart) {
         CommonEntity<?> commonEntity = CommonEntity.get((Minecart)entity);
         MinecartMember<?> result = (MinecartMember)commonEntity.getController(MinecartMember.class);
         if (result != null && !result.isUnloaded()) {
            return result;
         }
      }

      return null;
   }

   /** @deprecated */
   @Deprecated
   public static MinecartMember<?> getAt(Block block) {
      List<MinecartMember<?>> members = RailLookup.findMembersOnRail(OfflineBlock.of(block));
      return members.isEmpty() ? null : (MinecartMember)members.get(0);
   }

   /** @deprecated */
   @Deprecated
   public static MinecartMember<?> getAt(World world, IntVector3 coord) {
      return getAt(BlockUtil.getBlock(world, coord));
   }

   /** @deprecated */
   public static MinecartMember<?> getAt(Location at) {
      RailPiece piece = RailType.findRailPiece(at);
      if (piece == null) {
         return null;
      } else {
         List<MinecartMember<?>> members = piece.members();
         return members.isEmpty() ? null : (MinecartMember)members.get(0);
      }
   }

   public static MinecartMember<?> getAt(Location at, MinecartGroup in) {
      return getAt(at, in, 0.999D);
   }

   public static MinecartMember<?> getAt(Location at, MinecartGroup in, double searchRadius) {
      if (at != null && !TrainCarts.isWorldDisabled(at.getWorld())) {
         MinecartMember<?> result = null;
         double distSquared = searchRadius * searchRadius;
         Iterator var7 = WorldUtil.getNearbyEntities(at, searchRadius, searchRadius, searchRadius).iterator();

         while(true) {
            MinecartMember mm;
            do {
               do {
                  if (!var7.hasNext()) {
                     return result;
                  }

                  Entity e = (Entity)var7.next();
                  mm = getFromEntity(e);
               } while(mm == null);
            } while(in != null && mm.getGroup() != in);

            if (!(((CommonMinecart)mm.getEntity()).loc.distanceSquared(at) > distSquared)) {
               result = mm;
               if (mm.isHeadingTo(at)) {
                  return mm;
               }
            }
         }
      } else {
         return null;
      }
   }

   public static MinecartMember<?> getFromHitTest(Location eyeLocation) {
      MinecartMember<?> best = null;
      double best_dist = 4.5D;
      Iterator var4 = MinecartGroupStore.getGroups().cloneAsIterable().iterator();

      while(true) {
         MinecartGroup group;
         do {
            if (!var4.hasNext()) {
               return best;
            }

            group = (MinecartGroup)var4.next();
         } while(group.getWorld() != eyeLocation.getWorld());

         for(int i = 0; i < group.size(); ++i) {
            MinecartMember member;
            try {
               member = (MinecartMember)group.get(i);
            } catch (IndexOutOfBoundsException var14) {
               break;
            }

            double max_rad = 2.0D * (double)((CommonMinecart)member.getEntity()).getWidth();
            double dist_sq = ((CommonMinecart)member.getEntity()).loc.distanceSquared(eyeLocation);
            if (!(dist_sq > max_rad * max_rad)) {
               double dist_hit = member.getHitBox().hitTest(eyeLocation);
               if (!(dist_hit >= best_dist)) {
                  best_dist = dist_hit;
                  best = member;
               }
            }
         }
      }
   }

   static {
      controllers.put(CommonMinecartRideable.class, MinecartMemberRideable::new);
      controllers.put(CommonMinecartFurnace.class, MinecartMemberFurnace::new);
      controllers.put(CommonMinecartChest.class, MinecartMemberChest::new);
      controllers.put(CommonMinecartHopper.class, MinecartMemberHopper::new);
      controllers.put(CommonMinecartTNT.class, MinecartMemberTNT::new);
      controllers.put(CommonMinecartMobSpawner.class, MinecartMemberMobSpawner::new);
      controllers.put(CommonMinecartCommandBlock.class, MinecartMemberCommandBlock::new);
   }
}
