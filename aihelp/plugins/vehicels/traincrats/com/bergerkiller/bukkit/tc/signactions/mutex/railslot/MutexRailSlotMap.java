package com.bergerkiller.bukkit.tc.signactions.mutex.railslot;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.WorldRailLookup;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneSlotType;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MutexRailSlotMap {
   private static final Map<IntVector3, MutexRailSlot> INITIAL_RAILS = Collections.emptyMap();
   private final LinkedHashMap<IntVector3, MutexRailSlot> railsLive = new LinkedHashMap();
   private final ArrayList<MutexRailSlot> railsFull = new ArrayList();
   private Map<IntVector3, MutexRailSlot> rails;
   private MutexRailSlot conflict;

   public MutexRailSlotMap() {
      this.rails = INITIAL_RAILS;
      this.conflict = null;
   }

   public List<MutexRailSlot> getLastPath() {
      ArrayList<MutexRailSlot> result = new ArrayList(this.railsLive.values());
      if (this.conflict != null) {
         result.add(this.conflict);
      }

      return result;
   }

   public void clearConflict(IntVector3 conflictRail) {
      MutexRailSlot prevConflict = this.conflict;
      this.rails = INITIAL_RAILS;
      this.railsFull.clear();
      this.conflict = (MutexRailSlot)this.railsLive.remove(conflictRail);
      if (this.conflict == null) {
         this.conflict = prevConflict;
      }

   }

   public void clearOldRails(int nowTicks) {
      Iterator iter = this.rails.values().iterator();

      while(iter.hasNext()) {
         MutexRailSlot slot = (MutexRailSlot)iter.next();
         if (slot.ticksLastProbed() < nowTicks) {
            this.onSlotRemoved(slot);
            iter.remove();
         }
      }

   }

   public void keepAlive(int nowTicks) {
      this.railsLive.values().forEach((slot) -> {
         slot.probe(nowTicks);
      });
   }

   public boolean add(MutexZoneSlotType type, IntVector3 railBlock, int nowTicks) {
      Map<IntVector3, MutexRailSlot> currRails = this.rails;
      if (currRails == INITIAL_RAILS) {
         this.rails = (Map)(currRails = this.railsLive);
         ((Map)currRails).clear();
         this.conflict = null;
      }

      MutexRailSlot slot = (MutexRailSlot)((Map)currRails).computeIfAbsent(railBlock, MutexRailSlot::new);
      boolean added = slot.isNew();
      boolean wasFullLocking = slot.isFullLocking();
      slot.probe(type, nowTicks);
      if (!wasFullLocking && slot.isFullLocking()) {
         this.railsFull.add(slot);
      }

      return added;
   }

   public boolean remove(IntVector3 railBlock) {
      Map<IntVector3, MutexRailSlot> rails = this.rails;
      if (rails.isEmpty()) {
         return false;
      } else {
         MutexRailSlot slot = (MutexRailSlot)rails.remove(railBlock);
         if (slot == null) {
            return false;
         } else {
            this.onSlotRemoved(slot);
            return true;
         }
      }
   }

   public boolean isFullyLocked() {
      return !this.railsFull.isEmpty();
   }

   public boolean isFullyLockedVerify(MinecartGroup group, int nowTicks) {
      List<MutexRailSlot> railsFull = this.railsFull;
      if (!railsFull.isEmpty()) {
         Iterator iter = this.railsFull.iterator();

         while(iter.hasNext()) {
            MutexRailSlot slot = (MutexRailSlot)iter.next();
            if (nowTicks == slot.ticksLastProbed() || isRailUsedByGroup(slot.rail(), group)) {
               return true;
            }

            this.railsLive.remove(slot.rail());
            iter.remove();
         }
      }

      return false;
   }

   public boolean isSmartLocked(IntVector3 rail) {
      return this.rails.containsKey(rail);
   }

   public boolean isSmartLockedVerify(MinecartGroup group, int nowTicks, IntVector3 rail) {
      MutexRailSlot slot = (MutexRailSlot)this.rails.get(rail);
      if (slot == null) {
         return false;
      } else if (nowTicks == slot.ticksLastProbed()) {
         return true;
      } else if (!group.isEmpty() && !group.isUnloaded()) {
         if (isRailUsedByGroup(rail, group)) {
            return true;
         } else {
            this.onSlotRemoved(slot);
            this.rails.remove(rail);
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean verifyHasRailsUsedByGroup(MinecartGroup group) {
      Iterator iter = this.rails.values().iterator();

      while(iter.hasNext()) {
         MutexRailSlot slot = (MutexRailSlot)iter.next();
         if (isRailUsedByGroup(slot.rail(), group)) {
            return true;
         }

         this.onSlotRemoved(slot);
         iter.remove();
      }

      return false;
   }

   private void onSlotRemoved(MutexRailSlot slot) {
      if (slot.isFullLocking()) {
         this.railsFull.remove(slot);
      }

   }

   private static boolean isRailUsedByGroup(IntVector3 rail, MinecartGroup group) {
      if (!group.isEmpty() && !group.isUnloaded()) {
         WorldRailLookup railLookup = group.head().railLookup();
         Iterator var3 = railLookup.lookupCachedRailPieces(railLookup.getOfflineWorld().getBlockAt(rail)).iterator();

         while(var3.hasNext()) {
            RailLookup.CachedRailPiece railPiece = (RailLookup.CachedRailPiece)var3.next();
            Iterator var5 = railPiece.cachedMembers().iterator();

            while(var5.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var5.next();
               if (!member.isUnloaded() && !((CommonMinecart)member.getEntity()).isRemoved() && member.getGroup() == group) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void save(OfflineDataBlock root) throws IOException {
      root.addChild("rail-slots", (stream) -> {
         stream.writeBoolean(this.rails == INITIAL_RAILS);
         Util.writeVariableLengthInt(stream, this.railsLive.size());
         Iterator var2 = this.railsLive.values().iterator();

         while(var2.hasNext()) {
            MutexRailSlot slot = (MutexRailSlot)var2.next();
            slot.writeTo(stream);
         }

         stream.writeBoolean(this.conflict != null);
         if (this.conflict != null) {
            this.conflict.writeTo(stream);
         }

      });
   }

   public void load(OfflineDataBlock root) throws IOException {
      DataInputStream stream = root.findChildOrThrow("rail-slots").readData();

      try {
         boolean isSetToInitial = stream.readBoolean();
         int numRailSlots = Util.readVariableLengthInt(stream);
         this.railsLive.clear();
         this.railsFull.clear();
         MutexZoneSlotType[] types = MutexZoneSlotType.values();

         for(int num = 0; num < numRailSlots; ++num) {
            MutexRailSlot slot = MutexRailSlot.read(stream);
            this.railsLive.put(slot.rail(), slot);
            if (slot.isFullLocking()) {
               this.railsFull.add(slot);
            }
         }

         this.rails = (Map)(isSetToInitial ? INITIAL_RAILS : this.railsLive);
         if (stream.readBoolean()) {
            this.conflict = MutexRailSlot.read(stream);
            MutexRailSlot existing = (MutexRailSlot)this.railsLive.get(this.conflict.rail());
            if (existing != null && existing.type() == this.conflict.type()) {
               this.conflict = existing;
            }
         } else {
            this.conflict = null;
         }
      } catch (Throwable var9) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (stream != null) {
         stream.close();
      }

   }
}
