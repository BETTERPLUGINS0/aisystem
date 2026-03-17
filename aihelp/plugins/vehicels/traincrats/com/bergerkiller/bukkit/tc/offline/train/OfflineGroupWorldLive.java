package com.bergerkiller.bukkit.tc.offline.train;

import com.bergerkiller.bukkit.common.chunk.ChunkFutureProvider;
import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.LongHashMap;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class OfflineGroupWorldLive extends OfflineGroupWorld {
   protected final OfflineGroupManager manager;
   private Set<OfflineGroup> groups = new HashSet();
   private LongHashMap<HashSet<OfflineGroup>> groupmap = new LongHashMap();
   private Set<UUID> minecartEntityUUIDsBeingDestroyed = new HashSet();
   private boolean isDuringWorldUnloadEvent = false;

   public OfflineGroupWorldLive(OfflineGroupManager manager, OfflineWorld world) {
      super(world);
      this.manager = manager;
   }

   public OfflineGroupManager getManager() {
      return this.manager;
   }

   public Collection<OfflineGroup> getGroups() {
      return this.groups;
   }

   public OfflineGroupWorld createSnapshot() {
      return snapshot(this.world, this.groups);
   }

   public void add(OfflineGroup group) {
      this.groups.add(group);
      group.forAllChunks((chunk) -> {
         if (!group.getLoadedChunks().contains(chunk)) {
            this.getOrCreateChunk(chunk).add(group);
         }

      });
   }

   public void setIsDuringWorldUnloadEvent(boolean isDuringWorldUnloadEvent) {
      this.isDuringWorldUnloadEvent = isDuringWorldUnloadEvent;
   }

   public boolean canRestoreGroups() {
      return !this.isDuringWorldUnloadEvent && this.world.isLoaded();
   }

   public CompletableFuture<Boolean> destroyAsync(World world, OfflineGroup group) {
      ChunkFutureProvider futureProvider = ChunkFutureProvider.of(TrainCarts.plugin);
      group.setBeingRemoved();
      List<UUID> minecartEntityUUIDs = (List)Stream.of(group.members).map((m) -> {
         return m.entityUID;
      }).collect(Collectors.toList());
      Set<UUID> minecartEntityUUIDsRemaining = new HashSet(minecartEntityUUIDs);
      this.minecartEntityUUIDsBeingDestroyed.addAll(minecartEntityUUIDs);
      CompletableFuture<Boolean> result = new CompletableFuture();
      List<ForcedChunk> chunks = group.forceLoadChunks(world);
      CompletableFuture[] chunkLoadEntitiesFuture = (CompletableFuture[])chunks.stream().map((forcedChunk) -> {
         return futureProvider.whenEntitiesLoaded(world, forcedChunk.getX(), forcedChunk.getZ()).thenAccept((chunk) -> {
            try {
               if (!minecartEntityUUIDsRemaining.isEmpty()) {
                  Iterator var4 = (new ArrayList(WorldUtil.getEntities(chunk))).iterator();

                  while(var4.hasNext()) {
                     Entity e = (Entity)var4.next();
                     if (minecartEntityUUIDsRemaining.remove(e.getUniqueId())) {
                        e.remove();
                        if (minecartEntityUUIDsRemaining.isEmpty()) {
                           result.complete(Boolean.TRUE);
                           break;
                        }
                     }
                  }
               }
            } finally {
               forcedChunk.close();
            }

         });
      }).toArray((x$0) -> {
         return new CompletableFuture[x$0];
      });
      CompletableFuture.allOf(chunkLoadEntitiesFuture).thenAccept((u) -> {
         result.complete(Boolean.FALSE);
      });
      return result.thenApply((found) -> {
         this.remove(group);
         TrainPropertiesStore.remove(group.name);
         this.minecartEntityUUIDsBeingDestroyed.removeAll(minecartEntityUUIDs);
         return found;
      });
   }

   public boolean isDestroyingMinecart(UUID minecartUUID) {
      return this.minecartEntityUUIDsBeingDestroyed.contains(minecartUUID);
   }

   public void remove(OfflineGroup group) {
      this.groups.remove(group);
      group.forAllChunks((chunk) -> {
         Set<OfflineGroup> groups = this.getOrCreateChunk(chunk);
         if (groups != null) {
            groups.remove(group);
            if (groups.isEmpty()) {
               this.groupmap.remove(chunk);
            }
         }

      });
   }

   public boolean removeCart(UUID memberUUID) {
      Iterator var2 = this.groups.iterator();

      while(var2.hasNext()) {
         OfflineGroup group = (OfflineGroup)var2.next();
         OfflineMember[] var4 = group.members;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            OfflineMember member = var4[var6];
            if (member.entityUID.equals(memberUUID)) {
               ArrayList<OfflineMember> newMembers = new ArrayList();
               OfflineMember[] var9 = group.members;
               int var10 = var9.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  OfflineMember m = var9[var11];
                  if (!m.entityUID.equals(memberUUID)) {
                     newMembers.add(m);
                  }
               }

               this.remove(group);
               if (!newMembers.isEmpty()) {
                  this.add(group.withMembers(newMembers));
               }

               return true;
            }
         }
      }

      return false;
   }

   public final OfflineGroup remove(String groupName) {
      Iterator var2 = this.groups.iterator();

      OfflineGroup group;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         group = (OfflineGroup)var2.next();
      } while(!group.name.equals(groupName));

      this.remove(group);
      return group;
   }

   public Set<OfflineGroup> removeFromChunk(Chunk chunk) {
      return this.removeFromChunk(chunk.getX(), chunk.getZ());
   }

   public Set<OfflineGroup> removeFromChunk(int x, int z) {
      return this.removeFromChunk(MathUtil.longHashToLong(x, z));
   }

   public Set<OfflineGroup> removeFromChunk(long chunk) {
      Set<OfflineGroup> rval = (Set)this.groupmap.remove(chunk);
      if (rval != null) {
         Iterator var4 = rval.iterator();

         while(var4.hasNext()) {
            OfflineGroup group = (OfflineGroup)var4.next();
            group.getLoadedChunks().add(chunk);
         }
      }

      return rval;
   }

   public Set<OfflineGroup> getFromChunk(Chunk chunk) {
      return this.getFromChunk(chunk.getX(), chunk.getZ());
   }

   public Set<OfflineGroup> getFromChunk(int x, int z) {
      return this.getFromChunk(MathUtil.longHashToLong(x, z));
   }

   public Set<OfflineGroup> getFromChunk(long chunk) {
      return (Set)this.groupmap.get(chunk);
   }

   public Set<OfflineGroup> getOrCreateChunk(long chunk) {
      HashSet<OfflineGroup> rval = (HashSet)this.groupmap.get(chunk);
      if (rval == null) {
         rval = new HashSet(1);
         this.groupmap.put(chunk, rval);
      }

      return rval;
   }
}
