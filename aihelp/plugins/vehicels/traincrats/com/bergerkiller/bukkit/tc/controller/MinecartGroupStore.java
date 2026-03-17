package com.bergerkiller.bukkit.tc.controller;

import com.bergerkiller.bukkit.common.collections.ImplicitlySharedSet;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableMember;
import com.bergerkiller.bukkit.tc.events.GroupCreateEvent;
import com.bergerkiller.bukkit.tc.events.GroupLinkEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class MinecartGroupStore extends ArrayList<MinecartMember<?>> {
   private static final long serialVersionUID = 1L;
   protected static ImplicitlySharedSet<MinecartGroup> groups = new ImplicitlySharedSet();
   protected static boolean hasPhysicsChanges = false;
   private static long lastMaxPerWorldLogTimestamp = 0L;

   public static void doFixedTick(TrainCarts plugin) {
      ImplicitlySharedSet groups_copy = groups.clone();

      try {
         try {
            Iterator var2 = groups_copy.iterator();

            while(var2.hasNext()) {
               MinecartGroup group = (MinecartGroup)var2.next();
               group.doPhysics(plugin);
               Iterator var4 = group.iterator();

               while(var4.hasNext()) {
                  MinecartMember<?> member = (MinecartMember)var4.next();
                  if (!member.isUnloaded()) {
                     ((CommonMinecart)member.getEntity()).doPostTick();
                  }
               }
            }
         } catch (Throwable var7) {
            plugin.handle(var7);
         }
      } catch (Throwable var8) {
         if (groups_copy != null) {
            try {
               groups_copy.close();
            } catch (Throwable var6) {
               var8.addSuppressed(var6);
            }
         }

         throw var8;
      }

      if (groups_copy != null) {
         groups_copy.close();
      }

   }

   public static void doPostMoveLogic() {
      ImplicitlySharedSet groups_copy = groups.clone();

      try {
         try {
            Iterator var1 = groups_copy.iterator();

            while(var1.hasNext()) {
               MinecartGroup group = (MinecartGroup)var1.next();
               Iterator var3 = group.iterator();

               while(var3.hasNext()) {
                  MinecartMember<?> m = (MinecartMember)var3.next();
                  ((CommonMinecart)m.getEntity()).doPostTick();
               }
            }
         } catch (Throwable var6) {
            TrainCarts.plugin.handle(var6);
         }
      } catch (Throwable var7) {
         if (groups_copy != null) {
            try {
               groups_copy.close();
            } catch (Throwable var5) {
               var7.addSuppressed(var5);
            }
         }

         throw var7;
      }

      if (groups_copy != null) {
         groups_copy.close();
      }

   }

   public static MinecartGroup create(MinecartMember<?>... members) {
      return create((String)null, members);
   }

   public static MinecartGroup create(String name, MinecartMember<?>... members) {
      Util.checkMainThread("MinecartGroupStore::create(name, members)");
      validateMembersArray(members);
      MinecartGroup g = new MinecartGroup(members[0].getTrainCarts());
      if (name != null) {
         g.setProperties(TrainPropertiesStore.create(name));
      }

      addMembersAndFinalize(g, members);
      return g;
   }

   public static MinecartGroup createSplitFrom(TrainProperties properties, MinecartMember<?>... members) {
      Util.checkMainThread("MinecartGroupStore::createSplitFrom(from, members)");
      validateMembersArray(members);
      MinecartGroup g = new MinecartGroup(members[0].getTrainCarts());
      g.setProperties(TrainPropertiesStore.createSplitFrom(properties));
      addMembersAndFinalize(g, members);
      g.getSignTracker().refresh();
      return g;
   }

   private static void validateMembersArray(MinecartMember<?>[] members) {
      int numMembers = members.length;
      if (numMembers == 0) {
         throw new IllegalArgumentException("Members array is empty, can't create a train with zero carts");
      } else {
         for(int i = 0; i < numMembers; ++i) {
            MinecartMember<?> member = members[i];
            if (member == null) {
               throw new IllegalArgumentException("Member at index " + i + " of members array is null");
            }

            if (member.getEntity() == null) {
               throw new IllegalArgumentException("Member at index " + i + " of members array was never spawned as an entity");
            }

            if (((CommonMinecart)member.getEntity()).isRemoved()) {
               Location lastLoc = ((CommonMinecart)member.getEntity()).getLocation();
               String worldName = lastLoc.getWorld() == null ? "null" : lastLoc.getWorld().getName();
               throw new IllegalArgumentException(String.format("Member at index %d of members array is dead (world=%s, x=%.3f, y=%.3f, z=%.3f)", i, worldName, lastLoc.getX(), lastLoc.getY(), lastLoc.getZ()));
            }
         }

      }
   }

   private static void addMembersAndFinalize(MinecartGroup group, MinecartMember<?>... members) {
      MinecartMember[] var2 = members;
      int var3 = members.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MinecartMember<?> member = var2[var4];
         member.setUnloaded(false);
         group.add(member);
      }

      group.updateDirection();
      group.getAverageForce();
      groups.add(group);
      GroupCreateEvent.call(group);
      group.onGroupCreated();
   }

   public static boolean isPerWorldSpawnLimitReached(Block at, int numberOfCartsToSpawn) {
      return isPerWorldSpawnLimitReached(at.getLocation(), numberOfCartsToSpawn);
   }

   public static boolean isPerWorldSpawnLimitReached(Location at, int numberOfCartsToSpawn) {
      if (TCConfig.maxCartsPerWorld < 0) {
         return false;
      } else {
         TrainCarts traincarts = TrainCarts.plugin;
         int countSpawned = 0;
         ImplicitlySharedSet groups_copy = groups.clone();

         try {
            Iterator var5 = groups_copy.iterator();

            while(var5.hasNext()) {
               MinecartGroup group = (MinecartGroup)var5.next();
               if (!group.isUnloaded() && group.getWorld() == at.getWorld()) {
                  countSpawned += group.size();
               }
            }
         } catch (Throwable var8) {
            if (groups_copy != null) {
               try {
                  groups_copy.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (groups_copy != null) {
            groups_copy.close();
         }

         if (TCConfig.maxCartsPerWorldCountUnloaded) {
            countSpawned += traincarts.getOfflineGroups().getStoredMemberCount(at.getWorld());
         }

         if (countSpawned + numberOfCartsToSpawn <= TCConfig.maxCartsPerWorld) {
            return false;
         } else {
            long now = System.currentTimeMillis();
            if (lastMaxPerWorldLogTimestamp == 0L || now - lastMaxPerWorldLogTimestamp > 30000L) {
               traincarts.getLogger().warning("Could not spawn " + numberOfCartsToSpawn + " carts in world '" + at.getWorld().getName() + "' at x=" + at.getBlockX() + " y=" + at.getBlockY() + " z=" + at.getBlockZ() + " because it exceeds limit (" + (countSpawned + numberOfCartsToSpawn) + "/" + TCConfig.maxCartsPerWorld + ")");
               lastMaxPerWorldLogTimestamp = now;
            }

            return true;
         }
      }
   }

   public static MinecartGroup spawn(SpawnableGroup spawnableGroup, List<Location> spawnLocations) {
      List<SpawnableMember> members = spawnableGroup.getMembers();
      if (members.size() > spawnLocations.size()) {
         return null;
      } else {
         SpawnableGroup.SpawnLocationList locations = new SpawnableGroup.SpawnLocationList();

         for(int i = 0; i < members.size(); ++i) {
            Location loc = (Location)spawnLocations.get(i);
            locations.addMember((SpawnableMember)members.get(i), loc.getDirection(), loc);
         }

         return spawn(spawnableGroup, locations);
      }
   }

   public static MinecartGroup spawn(SpawnableGroup spawnableGroup, SpawnableGroup.SpawnLocationList locations) {
      return spawn(spawnableGroup, locations, 0.0D);
   }

   public static MinecartGroup spawn(SpawnableGroup spawnableGroup, SpawnableGroup.SpawnLocationList locations, double initialSpeed) {
      if (locations.locations.isEmpty()) {
         throw new IllegalArgumentException("Spawn Location List has zero locations to spawn, cannot spawn a train with zero carts");
      } else {
         MinecartGroup group = new MinecartGroup(spawnableGroup.getTrainCarts());
         group.setProperties(TrainPropertiesStore.createFromConfig(spawnableGroup.getConfig()));
         groups.add(group);

         for(int i = locations.locations.size() - 1; i >= 0; --i) {
            group.add(((SpawnableMember.SpawnLocation)locations.locations.get(i)).spawn(initialSpeed));
         }

         group.updateDirection();
         GroupCreateEvent.call(group);
         group.onGroupCreated();
         return group;
      }
   }

   /** @deprecated */
   @Deprecated
   public static MinecartGroup spawn(Location[] at, EntityType... types) {
      return spawn(TrainCarts.plugin, at, types);
   }

   public static MinecartGroup spawn(TrainCarts plugin, Location[] at, EntityType... types) {
      Util.checkMainThread("MinecartGroupStore::spawn(at, types)");
      if (at.length == 0) {
         throw new IllegalArgumentException("One or more locations must be specified, cannot spawn a train with zero carts");
      } else if (at.length != types.length) {
         throw new IllegalArgumentException("Number of locations is not equal to the number entity types to spawn");
      } else {
         MinecartGroup g = new MinecartGroup(plugin);

         for(int i = 0; i < at.length; ++i) {
            g.add(MinecartMemberStore.spawn(plugin, at[i], types[i]));
         }

         groups.add(g);
         GroupCreateEvent.call(g);
         g.onGroupCreated();
         return g;
      }
   }

   public static Collection<MinecartGroup> matchAll(String nameExpression) {
      return (Collection)TrainPropertiesStore.matchAll(nameExpression).stream().map(TrainProperties::getHolder).filter(Objects::nonNull).collect(StreamUtil.toUnmodifiableList());
   }

   public static ImplicitlySharedSet<MinecartGroup> getGroups() {
      return groups;
   }

   public static MinecartGroup get(Entity e) {
      MinecartMember<?> mm = MinecartMemberStore.getFromEntity(e);
      return mm == null ? null : mm.getGroup();
   }

   public static MinecartGroupStore.LinkResult link(MinecartMember<?> m1, MinecartMember<?> m2) {
      if (m1 != null && m2 != null && m1 != m2 && m1.isInteractable() && m2.isInteractable()) {
         MinecartGroup g1 = m1.getGroup();
         MinecartGroup g2 = m2.getGroup();
         if (g1 == g2) {
            return MinecartGroupStore.LinkResult.ALREADY_LINKED;
         } else if (!m1.isDerailed() && !m2.isDerailed()) {
            if (TCConfig.maxCartsPerTrain >= 0 && g1.size() + g2.size() > TCConfig.maxCartsPerTrain) {
               return MinecartGroupStore.LinkResult.TOO_LONG;
            } else {
               TrainProperties prop1 = g1.getProperties();
               TrainProperties prop2 = g2.getProperties();
               if ((prop1.isPoweredMinecartRequired() || prop2.isPoweredMinecartRequired()) && g1.size(EntityType.MINECART_FURNACE) == 0 && g2.size(EntityType.MINECART_FURNACE) == 0) {
                  return MinecartGroupStore.LinkResult.POWERED_CART_REQUIRED;
               } else if (!MinecartMember.isTrackConnected(m1, m2)) {
                  return MinecartGroupStore.LinkResult.DIFFERENT_TRACKS;
               } else {
                  int m1index = g1.indexOf(m1);
                  int m2index = g2.indexOf(m2);
                  if (g2.canConnect(m1, m2index) && g1.canConnect(m2, m1index)) {
                     if (m1index != 0 && m1index != g1.size() - 1 || m2index != 0 && m2index != g2.size() - 1) {
                        return MinecartGroupStore.LinkResult.IS_MIDDLE_CARTS;
                     } else if (GroupLinkEvent.call(g1, g2).isCancelled()) {
                        return MinecartGroupStore.LinkResult.LINK_CANCELLED;
                     } else {
                        if (g1.size() > g2.size() || g1.size() == g2.size() && g1.getTicksLived() > g2.getTicksLived()) {
                           g2.getProperties().load(g1.getProperties());
                           String name = g1.getProperties().getTrainName();
                           g1.getProperties().setTrainName(TrainProperties.generateTrainName());
                           g2.getProperties().setTrainName(name);
                        }

                        g1.getSignTracker().clear();
                        g2.getSignTracker().clear();
                        if (m1index == 0 && m2index == 0) {
                           Collections.reverse(g1);
                           g2.addAll(0, g1);
                        } else if (m1index == 0 && m2index == g2.size() - 1) {
                           g2.addAll(g1);
                        } else if (m1index == g1.size() - 1 && m2index == 0) {
                           g2.addAll(0, g1);
                        } else {
                           if (m1index != g1.size() - 1 || m2index != g2.size() - 1) {
                              return MinecartGroupStore.LinkResult.IS_MIDDLE_CARTS;
                           }

                           Collections.reverse(g1);
                           g2.addAll(g1);
                        }

                        g2.getAverageForce();
                        g2.updateDirection();
                        g2.getSignTracker().updatePosition();
                        g1.remove();
                        if (TCConfig.playHissWhenLinked) {
                           m2.playLinkEffect();
                        }

                        return MinecartGroupStore.LinkResult.SUCCESS;
                     }
                  } else {
                     return MinecartGroupStore.LinkResult.DIFFERENT_TRACKS;
                  }
               }
            }
         } else {
            return MinecartGroupStore.LinkResult.DERAILED;
         }
      } else {
         return MinecartGroupStore.LinkResult.INVALID;
      }
   }

   public static void notifyPhysicsChange() {
      hasPhysicsChanges = true;
   }

   public static enum LinkResult {
      INVALID(false),
      ALREADY_LINKED(true),
      IS_MIDDLE_CARTS(true),
      DERAILED(false),
      TOO_LONG(false),
      POWERED_CART_REQUIRED(false),
      DIFFERENT_TRACKS(true),
      LINK_CANCELLED(false),
      SUCCESS(true);

      private final boolean cancelCollision;

      private LinkResult(boolean cancelCollision) {
         this.cancelCollision = cancelCollision;
      }

      public boolean isCancelCollision() {
         return this.cancelCollision;
      }

      // $FF: synthetic method
      private static MinecartGroupStore.LinkResult[] $values() {
         return new MinecartGroupStore.LinkResult[]{INVALID, ALREADY_LINKED, IS_MIDDLE_CARTS, DERAILED, TOO_LONG, POWERED_CART_REQUIRED, DIFFERENT_TRACKS, LINK_CANCELLED, SUCCESS};
      }
   }
}
