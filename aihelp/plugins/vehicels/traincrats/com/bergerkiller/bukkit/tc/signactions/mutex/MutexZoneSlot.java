package com.bergerkiller.bukkit.tc.signactions.mutex;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.events.MutexZoneConflictEvent;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.signactions.mutex.railslot.MutexRailSlot;
import com.bergerkiller.bukkit.tc.signactions.mutex.railslot.MutexRailSlotMap;
import com.bergerkiller.bukkit.tc.statements.Statement;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.block.Block;

public class MutexZoneSlot {
   private static final int TICK_DELAY_CLEAR_AUTOMATIC = 6;
   private final String name;
   private final List<MutexZoneSlot.EnteredGroup> entered = new ArrayList(2);
   private List<MutexZone> zones;
   private List<String> statements;
   private int tickLastHardEntered = 0;

   protected MutexZoneSlot(String name) {
      this.name = name;
      this.zones = Collections.emptyList();
      this.statements = Collections.emptyList();
   }

   public String getName() {
      return this.name;
   }

   public String getNameWithoutWorldUUID() {
      if (!this.zones.isEmpty()) {
         String uuid_str = ((MutexZone)this.zones.get(0)).signBlock.getWorldUUID().toString() + "_";
         if (this.name.startsWith(uuid_str)) {
            return this.name.substring(uuid_str.length());
         }
      }

      return this.name;
   }

   public boolean isAnonymous() {
      return this.name.isEmpty();
   }

   protected MutexZoneSlot addZone(MutexZone zone) {
      if (this.zones.isEmpty()) {
         this.zones = Collections.singletonList(zone);
      } else {
         this.zones = new ArrayList(this.zones);
         this.zones.add(zone);
      }

      this.refreshStatements();
      return this;
   }

   public void removeZone(MutexZone zone) {
      if (this.zones.size() == 1 && this.zones.get(0) == zone) {
         this.zones = Collections.emptyList();
         this.statements = Collections.emptyList();
      } else if (this.zones.size() > 1) {
         this.zones.remove(zone);
         this.refreshStatements();
      }

   }

   public List<MutexZone> getZones() {
      return this.zones;
   }

   public boolean hasZones() {
      return !this.zones.isEmpty();
   }

   public List<MutexZoneSlot.EnteredGroup> getEnteredGroups() {
      return this.entered;
   }

   public List<String> getStatements() {
      return this.statements;
   }

   private void refreshStatements() {
      this.statements = (List)this.zones.stream().sorted((z0, z1) -> {
         return z0.signBlock.getPosition().compareTo(z1.signBlock.getPosition());
      }).map((z) -> {
         return z.statement;
      }).filter((s) -> {
         return !s.isEmpty();
      }).collect(Collectors.toList());
   }

   public void onTick() {
      if (!this.entered.isEmpty()) {
         ListIterator<MutexZoneSlot.EnteredGroup> iter = this.entered.listIterator();
         boolean hasHardEnteredGroup = false;
         boolean trainsHaveLeft = false;

         while(iter.hasNext()) {
            MutexZoneSlot.EnteredGroup enteredGroup = (MutexZoneSlot.EnteredGroup)iter.next();
            if (!enteredGroup.refresh(this, (newGroup) -> {
               iter.set(newGroup);
               this.swapEnteredGroup(enteredGroup, newGroup);
            })) {
               iter.remove();
               this.swapDeactivatedEnteredGroups(enteredGroup, (MutexZoneSlot.EnteredGroup)null);
               trainsHaveLeft = true;
            } else if (enteredGroup.hardEnter) {
               hasHardEnteredGroup = true;
            }
         }

         if (trainsHaveLeft && !hasHardEnteredGroup) {
            this.setLevers(false);
         }
      }

   }

   public void unload(MinecartGroup group) {
      ListIterator iter = this.entered.listIterator();

      while(iter.hasNext()) {
         MutexZoneSlot.EnteredGroup entered = (MutexZoneSlot.EnteredGroup)iter.next();
         if (entered.isGroup(group)) {
            MutexZoneSlot.EnteredGroup unloaded = entered.unload();
            if (entered != unloaded) {
               iter.set(unloaded);
               this.swapDeactivatedEnteredGroups(entered, unloaded);
            }
            break;
         }
      }

   }

   public MutexZoneSlot.LoadedEnteredGroup findEntered(MinecartGroup group) {
      Iterator var2 = this.entered.iterator();

      MutexZoneSlot.EnteredGroup entered;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         entered = (MutexZoneSlot.EnteredGroup)var2.next();
      } while(!(entered instanceof MutexZoneSlot.LoadedEnteredGroup) || !entered.isGroup(group));

      return (MutexZoneSlot.LoadedEnteredGroup)entered;
   }

   public MutexZoneSlot.LoadedEnteredGroup track(MinecartGroup group, double distanceToMutex) {
      int nowTicks = group.getObstacleTracker().getTickCounter();
      List<String> statements = this.getStatements();
      if (!statements.isEmpty()) {
         SignActionEvent signEvent = null;
         if (this.zones.size() == 1) {
            Block signBlock = ((MutexZone)this.zones.get(0)).getSignBlock();
            if (signBlock != null && (Boolean)MaterialUtil.ISSIGN.get(signBlock)) {
               RailLookup.TrackedSign trackedSign = RailLookup.TrackedSign.forRealSign((Block)signBlock, ((MutexZone)this.zones.get(0)).isSignFrontText(), (RailPiece)null);
               signEvent = new SignActionEvent(trackedSign, group);
               signEvent.setAction(SignActionType.GROUP_ENTER);
            }
         }

         if (!Statement.hasMultiple((MinecartGroup)group, this.getStatements(), signEvent)) {
            boolean wasGroupHardEntered = false;
            boolean hasHardEnteredGroup = false;
            Iterator iter = this.entered.iterator();

            while(iter.hasNext()) {
               MutexZoneSlot.EnteredGroup enteredGroup = (MutexZoneSlot.EnteredGroup)iter.next();
               if (enteredGroup.isGroup(group)) {
                  iter.remove();
                  this.swapDeactivatedEnteredGroups(enteredGroup, (MutexZoneSlot.EnteredGroup)null);
                  wasGroupHardEntered = enteredGroup.hardEnter;
               } else if (enteredGroup.hardEnter) {
                  hasHardEnteredGroup = true;
               }
            }

            if (wasGroupHardEntered && !hasHardEnteredGroup) {
               this.setLevers(false);
            }

            return new MutexZoneSlot.IgnoredEnteredGroup(this, group, distanceToMutex, nowTicks);
         }
      }

      Iterator var11 = this.entered.iterator();

      MutexZoneSlot.EnteredGroup enteredGroup;
      do {
         if (!var11.hasNext()) {
            MutexZoneSlot.LoadedEnteredGroup enteredGroup = new MutexZoneSlot.LoadedEnteredGroup(this, group, distanceToMutex, nowTicks, nowTicks);
            this.entered.add(enteredGroup);
            return enteredGroup;
         }

         enteredGroup = (MutexZoneSlot.EnteredGroup)var11.next();
      } while(!enteredGroup.isGroup(group));

      MutexZoneSlot.LoadedEnteredGroup loadedEnteredGroup = enteredGroup.load(this, group);
      if (enteredGroup != loadedEnteredGroup) {
         this.swapEnteredGroup(enteredGroup, loadedEnteredGroup);
      }

      loadedEnteredGroup.deactivateByOtherGroups();
      loadedEnteredGroup.probeTick = nowTicks;
      if (loadedEnteredGroup.active) {
         loadedEnteredGroup.distanceToMutex = Math.min(loadedEnteredGroup.distanceToMutex, distanceToMutex);
      } else {
         loadedEnteredGroup.active = true;
         loadedEnteredGroup.distanceToMutex = distanceToMutex;
      }

      return loadedEnteredGroup;
   }

   private void setLevers(boolean down) {
      Iterator var2 = this.zones.iterator();

      while(var2.hasNext()) {
         MutexZone zone = (MutexZone)var2.next();
         zone.setLevers(down);
      }

   }

   public List<MinecartGroup> getCurrentGroups() {
      if (this.entered.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<MinecartGroup> result = new ArrayList(this.entered.size());
         Iterator var2 = this.entered.iterator();

         while(var2.hasNext()) {
            MutexZoneSlot.EnteredGroup enteredGroup = (MutexZoneSlot.EnteredGroup)var2.next();
            if (enteredGroup.active && enteredGroup.hardEnter && enteredGroup instanceof MutexZoneSlot.LoadedEnteredGroup) {
               result.add(((MutexZoneSlot.LoadedEnteredGroup)enteredGroup).group);
            }
         }

         return result;
      }
   }

   public List<MinecartGroup> getProspectiveGroups() {
      if (this.entered.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<MinecartGroup> result = new ArrayList(this.entered.size());
         Iterator var2 = this.entered.iterator();

         while(var2.hasNext()) {
            MutexZoneSlot.EnteredGroup enteredGroup = (MutexZoneSlot.EnteredGroup)var2.next();
            if (enteredGroup.active && enteredGroup instanceof MutexZoneSlot.LoadedEnteredGroup) {
               result.add(((MutexZoneSlot.LoadedEnteredGroup)enteredGroup).group);
            }
         }

         return result;
      }
   }

   private void swapEnteredGroup(MutexZoneSlot.EnteredGroup toReplace, MutexZoneSlot.EnteredGroup replacement) {
      swapEnteredGroup(this.entered, toReplace, replacement);
      this.swapDeactivatedEnteredGroups(toReplace, replacement);
   }

   private void swapDeactivatedEnteredGroups(MutexZoneSlot.EnteredGroup toReplace, MutexZoneSlot.EnteredGroup replacement) {
      Iterator var3 = this.entered.iterator();

      while(var3.hasNext()) {
         MutexZoneSlot.EnteredGroup group = (MutexZoneSlot.EnteredGroup)var3.next();
         swapEnteredGroup(group.groupsDeactivatingMe, toReplace, replacement);
         swapEnteredGroup(group.otherGroupsToDeactivate, toReplace, replacement);
      }

   }

   private static void swapEnteredGroup(List<MutexZoneSlot.EnteredGroup> groups, MutexZoneSlot.EnteredGroup toReplace, MutexZoneSlot.EnteredGroup replacement) {
      int index = groups.indexOf(toReplace);
      if (index != -1) {
         if (replacement != null) {
            groups.set(index, replacement);
         } else {
            groups.remove(index);
         }
      }

   }

   public abstract static class EnteredGroup {
      public boolean hardEnter = false;
      public boolean active = true;
      public double distanceToMutex;
      protected final MutexRailSlotMap occupiedRails;
      protected final ArrayList<MutexZoneSlot.EnteredGroup> otherGroupsToDeactivate;
      protected final ArrayList<MutexZoneSlot.EnteredGroup> groupsDeactivatingMe;
      protected IntVector3 groupsDeactivatingMeConflictRail;

      public EnteredGroup(double distanceToMutex) {
         this.occupiedRails = new MutexRailSlotMap();
         this.distanceToMutex = distanceToMutex;
         this.otherGroupsToDeactivate = new ArrayList(2);
         this.groupsDeactivatingMe = new ArrayList(2);
         this.groupsDeactivatingMeConflictRail = null;
      }

      protected EnteredGroup(MutexZoneSlot.EnteredGroup copy) {
         this.hardEnter = copy.hardEnter;
         this.active = copy.active;
         this.distanceToMutex = copy.distanceToMutex;
         this.occupiedRails = copy.occupiedRails;
         this.otherGroupsToDeactivate = copy.otherGroupsToDeactivate;
         this.groupsDeactivatingMe = copy.groupsDeactivatingMe;
         this.groupsDeactivatingMeConflictRail = copy.groupsDeactivatingMeConflictRail;
      }

      public abstract boolean isGroup(MinecartGroup var1);

      public abstract String getTrainName();

      public abstract MutexZoneSlot.LoadedEnteredGroup load(MutexZoneSlot var1, MinecartGroup var2);

      public abstract MutexZoneSlot.UnloadedEnteredGroup unload();

      public abstract int age();

      protected abstract boolean containsVerify(IntVector3 var1);

      protected abstract boolean refresh(MutexZoneSlot var1, Consumer<MutexZoneSlot.EnteredGroup> var2);
   }

   public static class UnloadedEnteredGroup extends MutexZoneSlot.EnteredGroup {
      public final TrainProperties trainProperties;
      public final int creationServerTime;

      private UnloadedEnteredGroup(TrainProperties trainProperties, double distanceToMutex, int age) {
         super(distanceToMutex);
         this.trainProperties = trainProperties;
         this.creationServerTime = CommonUtil.getServerTicks() - age;
      }

      public UnloadedEnteredGroup(MutexZoneSlot.LoadedEnteredGroup loadedGroup) {
         super(loadedGroup);
         this.trainProperties = loadedGroup.group.getProperties();
         this.creationServerTime = CommonUtil.getServerTicks() - loadedGroup.age();
      }

      public void save(TrainCarts plugin, OfflineDataBlock root) {
         OfflineDataBlock enteredGroupData;
         try {
            enteredGroupData = root.addChild("entered-group", (stream) -> {
               stream.writeUTF(this.getTrainName());
               stream.writeDouble(this.distanceToMutex);
               stream.writeInt(this.age());
               Util.writeVariableLengthInt(stream, this.otherGroupsToDeactivate.size());
               Iterator var2 = this.otherGroupsToDeactivate.iterator();

               MutexZoneSlot.EnteredGroup otherGroup;
               while(var2.hasNext()) {
                  otherGroup = (MutexZoneSlot.EnteredGroup)var2.next();
                  stream.writeUTF(otherGroup.getTrainName());
               }

               Util.writeVariableLengthInt(stream, this.groupsDeactivatingMe.size());
               var2 = this.groupsDeactivatingMe.iterator();

               while(var2.hasNext()) {
                  otherGroup = (MutexZoneSlot.EnteredGroup)var2.next();
                  stream.writeUTF(otherGroup.getTrainName());
               }

               stream.writeBoolean(this.groupsDeactivatingMeConflictRail != null);
               if (this.groupsDeactivatingMeConflictRail != null) {
                  this.groupsDeactivatingMeConflictRail.write(stream);
               }

            });
         } catch (Throwable var6) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save mutex entered group data of train " + this.getTrainName(), var6);
            return;
         }

         try {
            this.occupiedRails.save(enteredGroupData);
         } catch (Throwable var5) {
            root.children.remove(root.children.size() - 1);
            plugin.getLogger().log(Level.SEVERE, "Failed to save mutex entered group rail slot data of train " + this.getTrainName(), var5);
         }
      }

      private static MutexZoneSlot.UnloadedEnteredGroupData loadData(TrainCarts plugin, OfflineDataBlock enteredGroupData) throws IOException {
         DataInputStream stream = enteredGroupData.readData();

         Object var13;
         label48: {
            MutexZoneSlot.UnloadedEnteredGroup group;
            List otherGroupsToDeactivateNames;
            List groupsDeactivatingMeNames;
            try {
               TrainProperties trainProperties = TrainPropertiesStore.get(stream.readUTF());
               if (trainProperties == null) {
                  var13 = null;
                  break label48;
               }

               double distanceToMutex = stream.readDouble();
               int age = stream.readInt();
               otherGroupsToDeactivateNames = readListOfStrings(stream);
               groupsDeactivatingMeNames = readListOfStrings(stream);
               IntVector3 groupsDeactivatingMeConflictRail = null;
               if (stream.readBoolean()) {
                  groupsDeactivatingMeConflictRail = IntVector3.read(stream);
               }

               group = new MutexZoneSlot.UnloadedEnteredGroup(trainProperties, distanceToMutex, age);
               group.groupsDeactivatingMeConflictRail = groupsDeactivatingMeConflictRail;
            } catch (Throwable var12) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }
               }

               throw var12;
            }

            if (stream != null) {
               stream.close();
            }

            group.occupiedRails.load(enteredGroupData);
            return new MutexZoneSlot.UnloadedEnteredGroupData(group, otherGroupsToDeactivateNames, groupsDeactivatingMeNames);
         }

         if (stream != null) {
            stream.close();
         }

         return (MutexZoneSlot.UnloadedEnteredGroupData)var13;
      }

      public static List<MutexZoneSlot.UnloadedEnteredGroup> loadAll(TrainCarts plugin, OfflineDataBlock mutexZoneSlotData) {
         List<OfflineDataBlock> enteredGroupDataList = mutexZoneSlotData.findChildren("entered-group");
         if (enteredGroupDataList.isEmpty()) {
            return Collections.emptyList();
         } else {
            List<MutexZoneSlot.UnloadedEnteredGroupData> unloadedGroupDataList = new ArrayList(enteredGroupDataList.size());
            Iterator var4 = enteredGroupDataList.iterator();

            MutexZoneSlot.UnloadedEnteredGroupData data;
            while(var4.hasNext()) {
               OfflineDataBlock enteredGroupData = (OfflineDataBlock)var4.next();

               try {
                  data = loadData(plugin, enteredGroupData);
                  if (data != null) {
                     unloadedGroupDataList.add(data);
                  }
               } catch (Throwable var7) {
                  plugin.getLogger().log(Level.SEVERE, "Failed to load mutex entered group data", var7);
               }
            }

            List<MutexZoneSlot.UnloadedEnteredGroup> unloadedGroups = new ArrayList(unloadedGroupDataList.size());
            Iterator var9 = unloadedGroupDataList.iterator();

            while(var9.hasNext()) {
               data = (MutexZoneSlot.UnloadedEnteredGroupData)var9.next();
               unloadedGroups.add(data.load(unloadedGroupDataList));
            }

            return Collections.unmodifiableList(unloadedGroups);
         }
      }

      private static List<String> readListOfStrings(DataInputStream stream) throws IOException {
         int count = Util.readVariableLengthInt(stream);
         if (count <= 0) {
            return Collections.emptyList();
         } else {
            List<String> result = new ArrayList(count);

            for(int i = 0; i < count; ++i) {
               result.add(stream.readUTF());
            }

            return Collections.unmodifiableList(result);
         }
      }

      public boolean isGroup(MinecartGroup group) {
         return group.getProperties() == this.trainProperties;
      }

      public String getTrainName() {
         return this.trainProperties.getTrainName();
      }

      public MutexZoneSlot.LoadedEnteredGroup load(MutexZoneSlot slot, MinecartGroup group) {
         return new MutexZoneSlot.LoadedEnteredGroup(slot, group, this);
      }

      public MutexZoneSlot.UnloadedEnteredGroup unload() {
         return this;
      }

      public int age() {
         return CommonUtil.getServerTicks() - this.creationServerTime;
      }

      protected boolean containsVerify(IntVector3 rail) {
         return this.occupiedRails.isFullyLocked() || this.occupiedRails.isSmartLocked(rail);
      }

      protected boolean refresh(MutexZoneSlot slot, Consumer<MutexZoneSlot.EnteredGroup> swap) {
         if (this.trainProperties.isRemoved()) {
            return false;
         } else {
            MinecartGroup group = this.trainProperties.getHolder();
            if (group != null) {
               swap.accept(this.load(slot, group));
            }

            return true;
         }
      }
   }

   public static class LoadedEnteredGroup extends MutexZoneSlot.EnteredGroup {
      private final MutexZoneSlot slot;
      public final MinecartGroup group;
      protected final int creationTick;
      protected int probeTick;
      public int occupiedTick;
      private MutexZoneConflictEvent conflict = null;

      public LoadedEnteredGroup(MutexZoneSlot slot, MinecartGroup group, double distanceToMutex, int creationTick, int nowTicks) {
         super(distanceToMutex);
         this.slot = slot;
         this.group = group;
         this.creationTick = creationTick;
         this.probeTick = nowTicks;
         this.occupiedTick = nowTicks;
      }

      public LoadedEnteredGroup(MutexZoneSlot slot, MinecartGroup group, MutexZoneSlot.UnloadedEnteredGroup unloadedGroup) {
         super(unloadedGroup);
         int nowTicks = group.getObstacleTracker().getTickCounter();
         this.slot = slot;
         this.group = group;
         this.creationTick = nowTicks - unloadedGroup.age();
         this.probeTick = nowTicks;
         this.occupiedTick = nowTicks;
         this.occupiedRails.keepAlive(nowTicks);
      }

      public boolean isGroup(MinecartGroup group) {
         return this.group == group;
      }

      public String getTrainName() {
         return this.group.getProperties().getTrainName();
      }

      public MutexZoneSlot.LoadedEnteredGroup load(MutexZoneSlot slot, MinecartGroup group) {
         return this;
      }

      public MutexZoneSlot.UnloadedEnteredGroup unload() {
         return new MutexZoneSlot.UnloadedEnteredGroup(this);
      }

      public int age() {
         return this.getObstacleTickCounter() - this.creationTick;
      }

      public int serverTickLastProbed() {
         return CommonUtil.getServerTicks() + this.probeTick - this.getObstacleTickCounter();
      }

      public boolean isOccupiedFully() {
         return this.occupiedRails.isFullyLocked();
      }

      public List<MutexRailSlot> getLastPath() {
         return this.occupiedRails.getLastPath();
      }

      public MutexZoneConflictEvent getConflict() {
         return this.conflict;
      }

      public MutexZoneSlot.EnterResult enter(MutexZoneSlotType type, IntVector3 railBlock, boolean hard) {
         MutexZoneSlot.EnterResult successResult = MutexZoneSlot.EnterResult.SUCCESS;
         if (this.wasOccupiedLastTick()) {
            successResult = this.conflict != null ? MutexZoneSlot.EnterResult.CONFLICT_ONGOING : MutexZoneSlot.EnterResult.OCCUPIED_DISCOVER;
         }

         boolean wasFullyLocked = this.occupiedRails.isFullyLocked();
         boolean addedNewSlot = this.occupiedRails.add(type, railBlock, this.probeTick);
         if (wasFullyLocked && hard == this.hardEnter && this.conflict == null) {
            return successResult;
         } else if (type == MutexZoneSlotType.SMART && !addedNewSlot && hard == this.hardEnter && this.conflict == null) {
            return successResult;
         } else {
            Iterator var8 = this.slot.entered.iterator();

            while(true) {
               MutexZoneSlot.EnteredGroup enteredGroup;
               label108:
               do {
                  do {
                     do {
                        if (!var8.hasNext()) {
                           if (hard && successResult == MutexZoneSlot.EnterResult.SUCCESS && !this.hardEnter) {
                              this.hardEnter = true;
                              this.slot.tickLastHardEntered = CommonUtil.getServerTicks();
                              this.slot.setLevers(true);
                           }

                           if (successResult == MutexZoneSlot.EnterResult.SUCCESS && this.conflict != null) {
                              this.conflict = null;
                              this.occupiedRails.clearOldRails(this.probeTick);
                           }

                           return successResult;
                        }

                        enteredGroup = (MutexZoneSlot.EnteredGroup)var8.next();
                     } while(enteredGroup == this);

                     if (enteredGroup.active) {
                        continue label108;
                     }
                  } while(enteredGroup.age() <= this.age() || this.slot.tickLastHardEntered >= this.serverTickLastProbed() + 5 || this.creationTick != this.probeTick && !this.wasOccupiedLastTick() || !enteredGroup.containsVerify(railBlock));

                  this.hardEnter = false;
                  this.deactivate(railBlock);
                  return MutexZoneSlot.EnterResult.OCCUPIED;
               } while(!enteredGroup.containsVerify(railBlock));

               if (hard) {
                  if (!enteredGroup.hardEnter && this.age() > enteredGroup.age()) {
                     this.deactivateOtherGroup(enteredGroup, railBlock);
                     continue;
                  }

                  boolean hadConflict = this.conflict != null;
                  if (hadConflict || this.creationTick == this.probeTick || !this.wasOccupiedLastTick()) {
                     if (enteredGroup instanceof MutexZoneSlot.LoadedEnteredGroup) {
                        this.conflict = new MutexZoneConflictEvent(this.group, ((MutexZoneSlot.LoadedEnteredGroup)enteredGroup).group, this.slot, railBlock);
                        this.occupiedTick = this.probeTick;
                        return hadConflict ? MutexZoneSlot.EnterResult.CONFLICT_ONGOING : MutexZoneSlot.EnterResult.CONFLICT;
                     }

                     return hadConflict ? MutexZoneSlot.EnterResult.CONFLICT_ONGOING : MutexZoneSlot.EnterResult.CONFLICT;
                  }
               }

               this.hardEnter = false;
               this.deactivate(railBlock);
               return MutexZoneSlot.EnterResult.OCCUPIED;
            }
         }
      }

      private boolean wasOccupiedLastTick() {
         return this.probeTick - this.occupiedTick <= 1;
      }

      private void deactivate(IntVector3 conflictRail) {
         this.active = false;
         this.occupiedRails.clearConflict(conflictRail);
         this.occupiedTick = this.probeTick;
         if (!this.otherGroupsToDeactivate.isEmpty()) {
            Iterator var2 = this.otherGroupsToDeactivate.iterator();

            while(var2.hasNext()) {
               MutexZoneSlot.EnteredGroup group = (MutexZoneSlot.EnteredGroup)var2.next();
               group.groupsDeactivatingMe.remove(this);
               if (group.groupsDeactivatingMe.isEmpty()) {
                  group.groupsDeactivatingMeConflictRail = null;
               }
            }

            this.otherGroupsToDeactivate.clear();
         }

      }

      private void deactivateByOtherGroups() {
         if (!this.groupsDeactivatingMe.isEmpty()) {
            Iterator var1 = this.groupsDeactivatingMe.iterator();

            while(var1.hasNext()) {
               MutexZoneSlot.EnteredGroup g = (MutexZoneSlot.EnteredGroup)var1.next();
               g.otherGroupsToDeactivate.remove(this);
            }

            this.groupsDeactivatingMe.clear();
            this.deactivate(this.groupsDeactivatingMeConflictRail);
            this.groupsDeactivatingMeConflictRail = null;
         }

      }

      private void deactivateOtherGroup(MutexZoneSlot.EnteredGroup otherGroup, IntVector3 conflictRail) {
         if (!this.otherGroupsToDeactivate.contains(otherGroup)) {
            this.otherGroupsToDeactivate.add(otherGroup);
            otherGroup.groupsDeactivatingMe.add(this);
            otherGroup.groupsDeactivatingMeConflictRail = conflictRail;
         }

      }

      protected boolean containsVerify(IntVector3 rail) {
         int nowTicks = this.probeTick;
         return this.occupiedRails.isFullyLockedVerify(this.group, nowTicks) || this.occupiedRails.isSmartLockedVerify(this.group, nowTicks, rail);
      }

      protected boolean refresh(MutexZoneSlot slot, Consumer<MutexZoneSlot.EnteredGroup> swap) {
         if (!this.group.isUnloaded() && MinecartGroupStore.getGroups().contains(this.group)) {
            int nowTicks = this.getObstacleTickCounter();
            if (nowTicks - this.probeTick < 6) {
               return true;
            } else if (!this.occupiedRails.verifyHasRailsUsedByGroup(this.group)) {
               return false;
            } else {
               this.probeTick = nowTicks;
               return true;
            }
         } else {
            return false;
         }
      }

      private int getObstacleTickCounter() {
         return this.group.getObstacleTracker().getTickCounter();
      }
   }

   private final class IgnoredEnteredGroup extends MutexZoneSlot.LoadedEnteredGroup {
      public IgnoredEnteredGroup(MutexZoneSlot slot, MinecartGroup group, double distanceToMutex, int nowTicks) {
         super(slot, group, distanceToMutex, nowTicks, nowTicks);
      }

      public MutexZoneSlot.EnterResult enter(MutexZoneSlotType type, IntVector3 railBlock, boolean hard) {
         return MutexZoneSlot.EnterResult.IGNORED;
      }
   }

   private static class UnloadedEnteredGroupData {
      public final MutexZoneSlot.UnloadedEnteredGroup group;
      public final List<String> otherGroupsToDeactivateNames;
      public final List<String> groupsDeactivatingMeNames;

      public UnloadedEnteredGroupData(MutexZoneSlot.UnloadedEnteredGroup group, List<String> otherGroupsToDeactivateNames, List<String> groupsDeactivatingMeNames) {
         this.group = group;
         this.otherGroupsToDeactivateNames = otherGroupsToDeactivateNames;
         this.groupsDeactivatingMeNames = groupsDeactivatingMeNames;
      }

      public MutexZoneSlot.UnloadedEnteredGroup load(List<MutexZoneSlot.UnloadedEnteredGroupData> otherEnteredGroups) {
         loadEnteredGroupList(this.otherGroupsToDeactivateNames, otherEnteredGroups, this.group.otherGroupsToDeactivate);
         loadEnteredGroupList(this.groupsDeactivatingMeNames, otherEnteredGroups, this.group.groupsDeactivatingMe);
         return this.group;
      }

      private static void loadEnteredGroupList(List<String> groupNames, List<MutexZoneSlot.UnloadedEnteredGroupData> otherEnteredGroups, List<MutexZoneSlot.EnteredGroup> groupsTarget) {
         groupsTarget.clear();
         Iterator var3 = groupNames.iterator();

         while(true) {
            while(var3.hasNext()) {
               String name = (String)var3.next();
               Iterator var5 = otherEnteredGroups.iterator();

               while(var5.hasNext()) {
                  MutexZoneSlot.UnloadedEnteredGroupData data = (MutexZoneSlot.UnloadedEnteredGroupData)var5.next();
                  if (name.equals(data.group.getTrainName())) {
                     groupsTarget.add(data.group);
                     break;
                  }
               }
            }

            return;
         }
      }
   }

   public static enum EnterResult {
      IGNORED(false, false),
      SUCCESS(false, false),
      CONFLICT(false, true),
      CONFLICT_ONGOING(false, true),
      OCCUPIED(true, false),
      OCCUPIED_DISCOVER(true, false);

      private final boolean occupied;
      private final boolean conflict;

      private EnterResult(boolean occupied, boolean conflict) {
         this.occupied = occupied;
         this.conflict = conflict;
      }

      public boolean isOccupied() {
         return this.occupied;
      }

      public boolean isConflict() {
         return this.conflict;
      }

      // $FF: synthetic method
      private static MutexZoneSlot.EnterResult[] $values() {
         return new MutexZoneSlot.EnterResult[]{IGNORED, SUCCESS, CONFLICT, CONFLICT_ONGOING, OCCUPIED, OCCUPIED_DISCOVER};
      }
   }
}
