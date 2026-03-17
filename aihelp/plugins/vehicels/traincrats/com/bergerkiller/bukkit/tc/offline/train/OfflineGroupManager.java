package com.bergerkiller.bukkit.tc.offline.train;

import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.offline.OfflineWorldMap;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;

public class OfflineGroupManager implements TrainCarts.Provider {
   private final TrainCarts plugin;
   private final OfflineGroupFileHandler fileHandler;
   Long lastUnloadChunk = null;
   private boolean chunkLoadReq = false;
   private boolean isRefreshingGroups = false;
   private Map<String, OfflineGroup> containedTrains = new HashMap();
   private HashSet<UUID> containedMinecarts = new HashSet();
   private final OfflineWorldMap<OfflineGroupManager.OfflineGroupWorldLiveImpl> worlds = new OfflineWorldMap();

   public OfflineGroupManager(TrainCarts plugin) {
      this.plugin = plugin;
      this.fileHandler = new OfflineGroupFileHandler(this);
   }

   public TrainCarts getTrainCarts() {
      return this.plugin;
   }

   private OfflineGroupManager.OfflineGroupWorldLiveImpl get(OfflineWorld world) {
      OfflineGroupManager.OfflineGroupWorldLiveImpl map = (OfflineGroupManager.OfflineGroupWorldLiveImpl)this.worlds.get(world);
      if (map == null) {
         map = new OfflineGroupManager.OfflineGroupWorldLiveImpl(this, world);
         this.worlds.put(world, map);
      }

      return map;
   }

   private OfflineGroupManager.OfflineGroupWorldLiveImpl get(World world) {
      OfflineGroupManager.OfflineGroupWorldLiveImpl map = (OfflineGroupManager.OfflineGroupWorldLiveImpl)this.worlds.get(world);
      if (map == null) {
         map = new OfflineGroupManager.OfflineGroupWorldLiveImpl(this, OfflineWorld.of(world));
         this.worlds.put(world, map);
      }

      return map;
   }

   public void unloadWorld(World world) {
      ArrayList<MinecartGroup> groupsOnWorld = new ArrayList();
      Iterator var3 = MinecartGroup.getGroups().cloneAsIterable().iterator();

      while(var3.hasNext()) {
         MinecartGroup group = (MinecartGroup)var3.next();
         if (group.getWorld() == world) {
            groupsOnWorld.add(group);
         }
      }

      synchronized(this) {
         OfflineGroupManager.OfflineGroupWorldLiveImpl map = this.get(world);
         map.setIsDuringWorldUnloadEvent(true);

         try {
            groupsOnWorld.forEach(MinecartGroup::unload);
            map.getGroups().forEach((groupx) -> {
               groupx.updateLoadedChunks(map);
            });
         } finally {
            map.setIsDuringWorldUnloadEvent(false);
         }

      }
   }

   public synchronized void loadChunk(Chunk chunk) {
      this.chunkLoadReq = true;
      if (!this.isRefreshingGroups) {
         OfflineGroupManager.OfflineGroupWorldLiveImpl map = (OfflineGroupManager.OfflineGroupWorldLiveImpl)this.worlds.get(chunk.getWorld());
         if (map != null && map.canRestoreGroups()) {
            if (map.isEmpty()) {
               this.worlds.remove(chunk.getWorld());
            } else {
               Set<OfflineGroup> groups = map.removeFromChunk(chunk);
               if (groups != null) {
                  Iterator var4 = groups.iterator();

                  while(var4.hasNext()) {
                     OfflineGroup group = (OfflineGroup)var4.next();
                     if (group.testFullyLoaded()) {
                        if (group.updateLoadedChunks(map)) {
                           map.restoreGroup(group);
                        } else {
                           map.add(group);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public synchronized void unloadChunk(Chunk chunk) {
      long chunkCoordLong = MathUtil.longHashToLong(chunk.getX(), chunk.getZ());
      this.lastUnloadChunk = chunkCoordLong;
      World chunkWorld = chunk.getWorld();
      Iterator var5 = MinecartGroup.getGroups().cloneAsIterable().iterator();

      while(var5.hasNext()) {
         MinecartGroup group = (MinecartGroup)var5.next();
         if (group.isInChunk(chunkWorld, chunkCoordLong)) {
            unloadChunkForGroup(group, chunk);
         }
      }

      var5 = WorldUtil.getEntities(chunk).iterator();

      while(var5.hasNext()) {
         Entity entity = (Entity)var5.next();
         if (entity instanceof Minecart) {
            MinecartMember<?> member = MinecartMemberStore.getFromEntity(entity);
            if (member != null && member.isInteractable()) {
               unloadChunkForGroup(member.getGroup(), chunk);
            }
         }
      }

      OfflineGroupWorldLive map = (OfflineGroupWorldLive)this.worlds.get(chunk.getWorld());
      if (map != null) {
         if (map.isEmpty()) {
            this.worlds.remove(chunk.getWorld());
         } else {
            Set<OfflineGroup> groupset = map.getFromChunk(chunk);
            if (groupset != null) {
               Iterator var12 = groupset.iterator();

               while(var12.hasNext()) {
                  OfflineGroup group = (OfflineGroup)var12.next();
                  group.getLoadedChunks().remove(MathUtil.longHashToLong(chunk.getX(), chunk.getZ()));
               }
            }
         }
      }

      this.lastUnloadChunk = null;
   }

   private static void unloadChunkForGroup(MinecartGroup group, Chunk chunk) {
      if (group.canUnload()) {
         group.unload();
      } else if (group.getChunkArea().containsChunk(chunk.getX(), chunk.getZ())) {
         group.getTrainCarts().log(Level.SEVERE, "Chunk " + chunk.getX() + "/" + chunk.getZ() + " of group " + group.getProperties().getTrainName() + " unloaded unexpectedly!");
      } else {
         group.getTrainCarts().log(Level.SEVERE, "Chunk " + chunk.getX() + "/" + chunk.getZ() + " of group " + group.getProperties().getTrainName() + " unloaded because chunk area wasn't up to date!");
      }

   }

   public synchronized void refresh() {
      Iterator var1 = WorldUtil.getWorlds().iterator();

      while(var1.hasNext()) {
         World world = (World)var1.next();
         this.refresh(world);
      }

   }

   public synchronized void refresh(World world) {
      OfflineGroupManager.OfflineGroupWorldLiveImpl map = (OfflineGroupManager.OfflineGroupWorldLiveImpl)this.worlds.get(world);
      if (map != null) {
         if (map.isEmpty()) {
            this.worlds.remove(world);
         } else if (map.canRestoreGroups()) {
            map.refreshGroups();
         }
      }

   }

   public synchronized List<OfflineGroupWorld> createSnapshot() {
      List<OfflineGroupWorld> worldSnapshots = new ArrayList(this.worlds.size());
      Iterator iter = this.worlds.values().iterator();

      while(iter.hasNext()) {
         OfflineGroupWorldLive world = (OfflineGroupWorldLive)iter.next();
         if (world.isEmpty()) {
            iter.remove();
         } else {
            worldSnapshots.add(world.createSnapshot());
         }
      }

      return Collections.unmodifiableList(worldSnapshots);
   }

   synchronized void load(List<OfflineGroupWorld> worlds) {
      int totalgroups = 0;
      int totalmembers = 0;
      int worldcount = worlds.size();
      Iterator var5 = worlds.iterator();

      while(var5.hasNext()) {
         OfflineGroupWorld world = (OfflineGroupWorld)var5.next();
         OfflineGroupWorldLive liveWorld = this.get(world.getWorld());

         for(Iterator var8 = world.getGroups().iterator(); var8.hasNext(); ++totalgroups) {
            OfflineGroup group = (OfflineGroup)var8.next();
            liveWorld.add(group);
            totalmembers += group.members.length;
         }
      }

      String msg = totalgroups + " Train";
      if (totalgroups == 1) {
         msg = msg + " has";
      } else {
         msg = msg + "s have";
      }

      msg = msg + " been loaded in " + worldcount + " world";
      if (worldcount != 1) {
         msg = msg + "s";
      }

      msg = msg + ". (" + totalmembers + " Minecart";
      if (totalmembers != 1) {
         msg = msg + "s";
      }

      msg = msg + ")";
      this.plugin.log(Level.INFO, msg);
   }

   public synchronized Map<OfflineGroup, List<ForcedChunk>> getForceLoadedChunks() {
      Map<OfflineGroup, List<ForcedChunk>> chunks = new HashMap();
      Iterator var2 = WorldUtil.getWorlds().iterator();

      while(var2.hasNext()) {
         World world = (World)var2.next();
         chunks.putAll(this.getForceLoadedChunks(world));
      }

      return chunks;
   }

   public synchronized Map<OfflineGroup, List<ForcedChunk>> getForceLoadedChunks(World world) {
      Map<OfflineGroup, List<ForcedChunk>> chunks = new HashMap();
      OfflineGroupWorldLive map = (OfflineGroupWorldLive)this.worlds.get(world);
      if (map != null && !map.isEmpty() && map.canRestoreGroups()) {
         Iterator var4 = map.getGroups().iterator();

         while(true) {
            OfflineGroup group;
            TrainProperties prop;
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return chunks;
                     }

                     group = (OfflineGroup)var4.next();
                     prop = TrainProperties.get(group.name);
                  } while(prop == null);
               } while(!prop.isKeepingChunksLoaded());
            } while(TCConfig.keepChunksLoadedOnlyWhenMoving && !group.isMoving());

            chunks.put(group, group.forceLoadChunks(world));
         }
      } else {
         return chunks;
      }
   }

   public boolean isDestroyingGroupOf(Minecart minecart) {
      return this.get(minecart.getWorld()).isDestroyingMinecart(minecart.getUniqueId());
   }

   public CompletableFuture<Boolean> destroyGroupAsync(String groupName) {
      OfflineGroup group = (OfflineGroup)this.containedTrains.get(groupName);
      if (group == null) {
         return CompletableFuture.completedFuture(Boolean.FALSE);
      } else {
         World world = group.world.getLoadedWorld();
         if (world == null) {
            this.removeGroup(groupName);
            TrainPropertiesStore.remove(groupName);
            return CompletableFuture.completedFuture(Boolean.TRUE);
         } else {
            OfflineGroupWorldLive map;
            synchronized(this) {
               map = (OfflineGroupWorldLive)this.worlds.get(group.world);
               if (map == null) {
                  return CompletableFuture.completedFuture(Boolean.FALSE);
               }
            }

            return map.destroyAsync(world, group);
         }
      }
   }

   public CompletableFuture<Integer> destroyAllAsync(World world, boolean includingVanilla) {
      if (TrainCarts.isWorldDisabled(world)) {
         return CompletableFuture.completedFuture(0);
      } else {
         int count = 0;
         Iterator var5 = MinecartGroup.getGroups().cloneAsIterable().iterator();

         while(var5.hasNext()) {
            MinecartGroup g = (MinecartGroup)var5.next();
            if (g.getWorld() == world) {
               if (!g.isEmpty()) {
                  ++count;
               }

               g.destroy();
            }
         }

         if (includingVanilla) {
            count += destroyMinecartsInLoadedChunks(world);
         }

         removeBuggedMinecarts(world);
         OfflineGroupWorldLive map;
         Object offlineGroups;
         synchronized(this) {
            map = (OfflineGroupWorldLive)this.worlds.get(world);
            if (map == null) {
               offlineGroups = Collections.emptyList();
            } else {
               offlineGroups = new ArrayList(map.getGroups());
            }
         }

         if (((List)offlineGroups).isEmpty()) {
            return CompletableFuture.completedFuture(count);
         } else {
            CompletableFuture<Boolean>[] destroyFutures = (CompletableFuture[])((List)offlineGroups).stream().map((group) -> {
               return map.destroyAsync(world, group);
            }).toArray((x$0) -> {
               return new CompletableFuture[x$0];
            });
            return CompletableFuture.allOf(destroyFutures).thenApply((unused) -> {
               int countx = count;
               CompletableFuture[] var5 = destroyFutures;
               int var6 = destroyFutures.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  CompletableFuture future = var5[var7];

                  try {
                     if ((Boolean)future.get()) {
                        ++countx;
                     }
                  } catch (ExecutionException | InterruptedException var10) {
                     this.plugin.getLogger().log(Level.SEVERE, "Unhandled error destroying carts", var10);
                  }
               }

               return countx;
            });
         }
      }
   }

   public CompletableFuture<Integer> destroyAllAsync(boolean includingVanilla) {
      CompletableFuture<Integer>[] futures = (CompletableFuture[])Bukkit.getWorlds().stream().map((world) -> {
         return this.destroyAllAsync(world, includingVanilla);
      }).toArray((x$0) -> {
         return new CompletableFuture[x$0];
      });
      return CompletableFuture.allOf(futures).thenApply((unused) -> {
         int total = 0;
         CompletableFuture[] var4 = futures;
         int var5 = futures.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            CompletableFuture future = var4[var6];

            try {
               total += (Integer)future.get();
            } catch (ExecutionException | InterruptedException var11) {
               this.plugin.getLogger().log(Level.SEVERE, "Unhandled error destroying carts", var11);
            }
         }

         TrainProperties.clearAll();
         synchronized(this) {
            this.worlds.clear();
         }

         return total;
      });
   }

   private static int destroyMinecartsInLoadedChunks(World world) {
      int count = 0;
      Iterator var2 = WorldUtil.getChunks(world).iterator();

      while(var2.hasNext()) {
         Chunk chunk = (Chunk)var2.next();
         Entity[] var4 = chunk.getEntities();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Entity e = var4[var6];
            if (e instanceof Minecart && !e.isDead()) {
               e.remove();
               Util.markChunkDirty(chunk);
               if (MinecartMemberStore.getFromEntity(e) == null) {
                  ++count;
               }
            }
         }
      }

      var2 = world.getEntities().iterator();

      while(var2.hasNext()) {
         Entity e = (Entity)var2.next();
         if (e instanceof Minecart && !e.isDead()) {
            e.remove();
            Chunk chunk = WorldUtil.getChunk(world, EntityUtil.getChunkX(e), EntityUtil.getChunkZ(e));
            if (chunk != null) {
               Util.markChunkDirty(chunk);
            }
         }
      }

      return count;
   }

   public static void removeBuggedMinecarts(World world) {
      Set<Entity> toRemove = new HashSet();
      Set<Entity> worldentities = new HashSet();
      Iterator var3 = WorldUtil.getEntities(world).iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         worldentities.add(entity);
      }

      var3 = WorldUtil.getChunks(world).iterator();

      while(var3.hasNext()) {
         Chunk chunk = (Chunk)var3.next();
         Iterator iter = WorldUtil.getEntities(chunk).iterator();

         while(iter.hasNext()) {
            Entity e = (Entity)iter.next();
            if (!worldentities.contains(e)) {
               iter.remove();
               toRemove.add(e);
            }
         }

         Iterator var9 = toRemove.iterator();

         while(var9.hasNext()) {
            Entity e = (Entity)var9.next();
            WorldUtil.removeEntity(e);
         }

         toRemove.clear();
      }

   }

   public void load() {
      this.fileHandler.load();
   }

   public void save(TrainCarts.SaveMode saveMode) {
      this.fileHandler.save(saveMode);
   }

   public synchronized void deinit() {
      this.worlds.clear();
      this.containedMinecarts.clear();
      this.containedTrains.clear();
   }

   public static OfflineGroup saveGroup(MinecartGroup group) {
      if (group != null && group.isValid()) {
         World world = group.getWorld();
         return world == null ? null : OfflineGroup.save(group);
      } else {
         return null;
      }
   }

   public static List<OfflineGroupWorld> saveAllGroups() {
      Map<OfflineWorld, List<OfflineGroup>> worlds = new IdentityHashMap();
      Iterator var1 = MinecartGroupStore.getGroups().cloneAsIterable().iterator();

      while(var1.hasNext()) {
         MinecartGroup group = (MinecartGroup)var1.next();
         OfflineGroup offlineGroup = saveGroup(group);
         if (offlineGroup != null) {
            ((List)worlds.computeIfAbsent(offlineGroup.world, (w) -> {
               return new ArrayList();
            })).add(offlineGroup);
         }
      }

      return OfflineGroupWorld.snapshot(worlds);
   }

   public synchronized void storeGroup(OfflineGroup group) {
      OfflineGroupManager.OfflineGroupWorldLiveImpl map = this.get(group.world);
      group.updateLoadedChunks(map);
      map.add(group);
   }

   public synchronized boolean containsMinecart(UUID uniqueId) {
      return this.containedMinecarts.contains(uniqueId);
   }

   public synchronized int getStoredMemberCount(World world) {
      OfflineGroupManager.OfflineGroupWorldLiveImpl map = (OfflineGroupManager.OfflineGroupWorldLiveImpl)this.worlds.get(world);
      return map == null ? 0 : map.totalMemberCount();
   }

   public synchronized int getStoredCount() {
      return this.containedTrains.size();
   }

   public synchronized int getStoredCountInLoadedWorlds() {
      int count = 0;
      Iterator var2 = this.worlds.values().iterator();

      while(var2.hasNext()) {
         OfflineGroupManager.OfflineGroupWorldLiveImpl map = (OfflineGroupManager.OfflineGroupWorldLiveImpl)var2.next();
         if (map.canRestoreGroups()) {
            count += map.totalGroupCount();
         }
      }

      return count;
   }

   public synchronized boolean contains(String trainname) {
      return this.containedTrains.containsKey(trainname);
   }

   public synchronized boolean containsInLoadedWorld(String trainname) {
      OfflineGroup offlineGroup = (OfflineGroup)this.containedTrains.get(trainname);
      return offlineGroup != null && offlineGroup.world.isLoaded();
   }

   public synchronized void rename(String oldtrainname, String newtrainname) {
      Iterator var3 = this.worlds.values().iterator();

      while(var3.hasNext()) {
         OfflineGroupWorldLive map = (OfflineGroupWorldLive)var3.next();
         Iterator var5 = map.iterator();

         while(var5.hasNext()) {
            OfflineGroup group = (OfflineGroup)var5.next();
            if (group.name.equals(oldtrainname)) {
               map.remove(group);
               map.add(group.withName(newtrainname));
               return;
            }
         }
      }

   }

   public synchronized void removeMember(UUID memberUUID) {
      if (this.containedMinecarts.remove(memberUUID)) {
         Iterator var2 = this.worlds.values().iterator();

         while(var2.hasNext()) {
            OfflineGroupWorldLive map = (OfflineGroupWorldLive)var2.next();
            if (map.removeCart(memberUUID)) {
               break;
            }
         }
      }

   }

   public synchronized void removeGroup(String groupName) {
      Iterator var2 = this.worlds.values().iterator();

      while(var2.hasNext()) {
         OfflineGroupWorldLive map = (OfflineGroupWorldLive)var2.next();
         OfflineGroup group = map.remove(groupName);
         if (group != null) {
            break;
         }
      }

   }

   public synchronized OfflineGroup findGroup(String groupName) {
      Iterator var2 = this.worlds.values().iterator();

      while(var2.hasNext()) {
         OfflineGroupWorldLive map = (OfflineGroupWorldLive)var2.next();
         Iterator var4 = map.getGroups().iterator();

         while(var4.hasNext()) {
            OfflineGroup group = (OfflineGroup)var4.next();
            if (group.name.equals(groupName)) {
               return group;
            }
         }
      }

      return null;
   }

   public OfflineMember findMember(String groupName, UUID uuid) {
      OfflineGroup group = this.findGroup(groupName);
      if (group != null) {
         OfflineMember[] var4 = group.members;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            OfflineMember member = var4[var6];
            if (member.entityUID.equals(uuid)) {
               return member;
            }
         }
      }

      return null;
   }

   private static final class OfflineGroupWorldLiveImpl extends OfflineGroupWorldLive {
      public OfflineGroupWorldLiveImpl(OfflineGroupManager manager, OfflineWorld world) {
         super(manager, world);
      }

      public void restoreGroup(OfflineGroup group) {
         this.remove(group);
         group.create(this.manager.plugin);
      }

      public void refreshGroups() {
         this.manager.isRefreshingGroups = true;
         ArrayList groupsBuffer = new ArrayList(this.totalGroupCount());

         try {
            do {
               this.manager.chunkLoadReq = false;
               groupsBuffer.clear();
               groupsBuffer.addAll(this.getGroups());
               Iterator var2 = groupsBuffer.iterator();

               while(var2.hasNext()) {
                  OfflineGroup group = (OfflineGroup)var2.next();
                  if (group.updateLoadedChunks(this)) {
                     this.restoreGroup(group);
                  }
               }
            } while(this.manager.chunkLoadReq);
         } catch (Throwable var4) {
            this.manager.plugin.getLogger().log(Level.SEVERE, "Unhandled error handling train restoring", var4);
         }

         this.manager.isRefreshingGroups = false;
      }

      public void add(OfflineGroup group) {
         super.add(group);
         this.manager.containedTrains.put(group.name, group);
         OfflineMember[] var2 = group.members;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            OfflineMember member = var2[var4];
            this.manager.containedMinecarts.add(member.entityUID);
         }

      }

      public void remove(OfflineGroup group) {
         super.remove(group);
         this.manager.containedTrains.remove(group.name);
         OfflineMember[] var2 = group.members;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            OfflineMember member = var2[var4];
            this.manager.containedMinecarts.remove(member.entityUID);
         }

      }
   }
}
