package com.bergerkiller.bukkit.tc.offline.train;

import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.LongHashSet;
import com.bergerkiller.bukkit.common.wrappers.LongHashSet.LongIterator;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import org.bukkit.World;

public final class OfflineGroup {
   public final String name;
   public final OfflineWorld world;
   public final List<OfflineDataBlock> actions;
   public final List<OfflineDataBlock> skippedSigns;
   public final OfflineMember[] members;
   private LongHashSet chunks;
   private LongHashSet loadedChunks;
   private boolean loaded;
   private boolean isBeingRemoved;

   public static OfflineGroup save(MinecartGroup group) {
      try {
         return new OfflineGroup(group);
      } catch (IOException var2) {
         throw new RuntimeException("Unexpected IO Exception", var2);
      }
   }

   private OfflineGroup(MinecartGroup group) throws IOException {
      this(group.getProperties().getTrainName(), OfflineWorld.of(group.getWorld()), group.getTrainCarts().getActionRegistry().saveTracker(group.getActions()), group.getTrainCarts().getTrackedSignLookup().serializeUniqueKeys(group.getSignTracker().getSignSkipTracker().getSkippedSigns(), "skipped-sign", RailLookup.TrackedSign::getUniqueKey), group, OfflineMember::new);
   }

   <T> OfflineGroup(String name, OfflineWorld world, List<OfflineDataBlock> actions, List<OfflineDataBlock> skippedSigns, Collection<T> memberData, OfflineGroup.MemberFactory<T> memberFactory) throws IOException {
      this.chunks = null;
      this.loadedChunks = null;
      this.isBeingRemoved = false;
      this.name = name;
      this.world = world;
      this.actions = actions;
      this.skippedSigns = skippedSigns;
      this.members = memberFactory.createMany(this, memberData);
      this.loaded = false;
   }

   private OfflineGroup(OfflineGroup original, String newName) {
      this.chunks = null;
      this.loadedChunks = null;
      this.isBeingRemoved = false;
      this.name = newName;
      this.world = original.world;
      this.members = original.members;
      this.chunks = original.chunks;
      this.loadedChunks = original.loadedChunks;
      this.loaded = original.loaded;
      this.isBeingRemoved = original.isBeingRemoved;
      this.actions = original.actions;
      this.skippedSigns = original.skippedSigns;
   }

   public OfflineGroup withName(String newName) {
      return new OfflineGroup(this, newName);
   }

   public OfflineGroup withMembers(List<OfflineMember> newMembers) {
      try {
         return new OfflineGroup(this.name, this.world, this.actions, this.skippedSigns, newMembers, (cgroup, cmember) -> {
            return cmember;
         });
      } catch (IOException var3) {
         throw new RuntimeException("Unexpected io exception", var3);
      }
   }

   public boolean isLoadedAsGroup() {
      return this.loaded;
   }

   public LongHashSet getChunks() {
      LongHashSet chunks = this.chunks;
      if (chunks == null) {
         int chunkCount = 25 + (int)(0.0D * (double)this.members.length);
         chunks = new LongHashSet(chunkCount);
         OfflineMember[] var3 = this.members;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            OfflineMember wm = var3[var5];

            for(int x = wm.cx - 2; x <= wm.cx + 2; ++x) {
               for(int z = wm.cz - 2; z <= wm.cz + 2; ++z) {
                  chunks.add(MathUtil.longHashToLong(x, z));
               }
            }
         }

         this.chunks = chunks;
      }

      return chunks;
   }

   public LongHashSet getLoadedChunks() {
      LongHashSet loadedChunks = this.loadedChunks;
      if (loadedChunks == null) {
         this.loadedChunks = loadedChunks = new LongHashSet(this.getChunks().size());
      }

      return loadedChunks;
   }

   public void forAllChunks(OfflineGroup.ChunkCoordConsumer action) {
      LongIterator iter = this.getChunks().longIterator();

      while(iter.hasNext()) {
         long chunk = iter.next();
         action.accept(MathUtil.longHashMsw(chunk), MathUtil.longHashLsw(chunk));
      }

   }

   public void forAllChunks(LongConsumer action) {
      LongIterator iter = this.getChunks().longIterator();

      while(iter.hasNext()) {
         action.accept(iter.next());
      }

   }

   public boolean isMoving() {
      OfflineMember[] var1 = this.members;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         OfflineMember member = var1[var3];
         if (member.isMoving()) {
            return true;
         }
      }

      return false;
   }

   void setBeingRemoved() {
      this.isBeingRemoved = true;
   }

   public boolean testFullyLoaded() {
      if (this.isBeingRemoved) {
         return false;
      } else {
         return this.getLoadedChunks().size() == this.getChunks().size();
      }
   }

   protected boolean updateLoadedChunks(OfflineGroupWorldLive offlineMap) {
      LongHashSet loadedChunks = this.getLoadedChunks();
      loadedChunks.clear();
      World world = this.world.getLoadedWorld();
      if (world != null && offlineMap.canRestoreGroups()) {
         this.forAllChunks((chunk) -> {
            if (WorldUtil.isChunkEntitiesLoaded(world, MathUtil.longHashMsw(chunk), MathUtil.longHashLsw(chunk))) {
               loadedChunks.add(chunk);
            }

         });
         if (offlineMap.getManager().lastUnloadChunk != null) {
            loadedChunks.remove(offlineMap.getManager().lastUnloadChunk);
         }

         return this.testFullyLoaded();
      } else {
         return false;
      }
   }

   public List<ForcedChunk> forceLoadChunks(World world) {
      List<ForcedChunk> chunks = new ArrayList();
      this.forAllChunks((cx, cz) -> {
         chunks.add(WorldUtil.forceChunkLoaded(world, cx, cz));
      });
      return chunks;
   }

   public MinecartGroup create(TrainCarts traincarts) {
      ArrayList<MinecartMember<?>> groupMembers = new ArrayList(this.members.length);
      int missingNo = 0;
      int cx = 0;
      int cz = 0;
      World world = this.world.getLoadedWorld();
      OfflineMember[] var7 = this.members;
      int i = var7.length;

      for(int var9 = 0; var9 < i; ++var9) {
         OfflineMember offlineMember = var7[var9];
         MinecartMember<?> mm = offlineMember.create(traincarts, world);
         if (mm != null) {
            groupMembers.add(mm);
         } else {
            ++missingNo;
            cx = offlineMember.cx;
            cz = offlineMember.cz;
         }
      }

      if (missingNo > 0) {
         traincarts.log(Level.WARNING, missingNo + " carts of group '" + this.name + "' are missing near chunk [" + cx + ", " + cz + "]! (externally edited?)");
      }

      this.loaded = true;
      if (groupMembers.isEmpty()) {
         TrainPropertiesStore.remove(this.name);
         return null;
      } else {
         MinecartGroup group = MinecartGroup.create(this.name, (MinecartMember[])groupMembers.toArray(new MinecartMember[0]));
         this.load(group);

         for(i = 0; i < this.members.length; ++i) {
            OfflineMember offlineMember = this.members[i];
            MinecartMember member;
            if (i < group.size() && offlineMember.entityUID.equals(((CommonMinecart)((MinecartMember)group.get(i)).getEntity()).getUniqueId())) {
               member = (MinecartMember)group.get(i);
            } else {
               member = null;
               Iterator var16 = group.iterator();

               while(var16.hasNext()) {
                  MinecartMember<?> groupMember = (MinecartMember)var16.next();
                  if (offlineMember.entityUID.equals(((CommonMinecart)groupMember.getEntity()).getUniqueId())) {
                     member = groupMember;
                     break;
                  }
               }

               if (member == null) {
                  continue;
               }
            }

            offlineMember.load(member);
         }

         return group;
      }
   }

   void load(MinecartGroup group) {
      group.getTrainCarts().getActionRegistry().loadTracker(group.getActions(), this.actions);
      Iterator var2 = group.getTrainCarts().getTrackedSignLookup().deserializeUniqueKeys(this.skippedSigns).iterator();

      while(var2.hasNext()) {
         Object signKey = var2.next();
         group.getSignTracker().addOfflineSkippedSignKey(signKey);
      }

      group.getSignTracker().clearUpdates();
   }

   @FunctionalInterface
   public interface MemberFactory<T> {
      OfflineMember create(OfflineGroup var1, T var2) throws IOException;

      default OfflineMember[] createMany(OfflineGroup group, Collection<T> data) throws IOException {
         int index = 0;
         OfflineMember[] members = new OfflineMember[data.size()];

         Object member;
         for(Iterator var5 = data.iterator(); var5.hasNext(); members[index++] = this.create(group, member)) {
            member = var5.next();
         }

         return members;
      }
   }

   @FunctionalInterface
   public interface ChunkCoordConsumer {
      void accept(int var1, int var2);
   }
}
