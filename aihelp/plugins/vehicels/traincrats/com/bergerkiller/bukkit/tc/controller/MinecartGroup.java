package com.bergerkiller.bukkit.tc.controller;

import com.bergerkiller.bukkit.common.bases.ExtendedEntity;
import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.bases.mutable.VectorAbstract;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.CommonEntityController;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;
import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.inventory.MergedInventory;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.LongHashSet;
import com.bergerkiller.bukkit.common.wrappers.LongHashSet.LongIterator;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.actions.MemberActionLaunchDirection;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.controller.components.ActionTrackerGroup;
import com.bergerkiller.bukkit.tc.controller.components.AnimationController;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerGroup;
import com.bergerkiller.bukkit.tc.controller.components.ObstacleTracker;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.components.RailTracker;
import com.bergerkiller.bukkit.tc.controller.components.RailTrackerGroup;
import com.bergerkiller.bukkit.tc.controller.components.SignTracker;
import com.bergerkiller.bukkit.tc.controller.components.SignTrackerGroup;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatus;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatusProvider;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.events.GroupRemoveEvent;
import com.bergerkiller.bukkit.tc.events.GroupUnloadEvent;
import com.bergerkiller.bukkit.tc.events.MemberAddEvent;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.bergerkiller.bukkit.tc.events.MemberRemoveEvent;
import com.bergerkiller.bukkit.tc.exception.GroupUnloadedException;
import com.bergerkiller.bukkit.tc.exception.MemberMissingException;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroup;
import com.bergerkiller.bukkit.tc.offline.train.OfflineGroupManager;
import com.bergerkiller.bukkit.tc.properties.CartPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.IPropertiesHolder;
import com.bergerkiller.bukkit.tc.properties.SaveLockOrientationMode;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.CartLockOrientation;
import com.bergerkiller.bukkit.tc.properties.standard.type.ChunkLoadOptions;
import com.bergerkiller.bukkit.tc.properties.standard.type.SlowdownMode;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCache;
import com.bergerkiller.bukkit.tc.utils.ChunkArea;
import com.bergerkiller.bukkit.tc.utils.TrackWalkingPoint;
import com.bergerkiller.generated.net.minecraft.world.level.chunk.ChunkHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

public class MinecartGroup extends MinecartGroupStore implements IPropertiesHolder, AnimationController, TrainStatusProvider, TrainCarts.Provider {
   private static final long serialVersionUID = 3L;
   private static final LongHashSet chunksBuffer = new LongHashSet(50);
   private final TrainCarts traincarts;
   protected final ChunkArea chunkArea = new ChunkArea();
   private boolean chunkAreaValid = false;
   private final SignTrackerGroup signTracker = new SignTrackerGroup(this);
   private final RailTrackerGroup railTracker = new RailTrackerGroup(this);
   private final ActionTrackerGroup actionTracker = new ActionTrackerGroup(this);
   private final ObstacleTracker obstacleTracker = new ObstacleTracker(this);
   private final AttachmentControllerGroup attachmentController = new AttachmentControllerGroup(this);
   protected long lastSync = Long.MIN_VALUE;
   private TrainProperties prop = null;
   private boolean breakPhysics = false;
   private int teleportImmunityTick = 0;
   private double updateSpeedFactor = 1.0D;
   private int updateStepCount = 1;
   private int updateStepNr = 1;
   private boolean unloaded = false;

   protected MinecartGroup(TrainCarts traincarts) {
      this.traincarts = traincarts;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public TrainProperties getProperties() {
      if (this.prop == null) {
         if (this.isUnloaded()) {
            throw new IllegalStateException("Group is unloaded");
         }

         this.prop = TrainPropertiesStore.create();
         Iterator var1 = this.iterator();

         while(var1.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var1.next();
            this.prop.add(member.getProperties());
         }

         TrainPropertiesStore.bindGroupToProperties(this.prop, this);
      }

      return this.prop;
   }

   public void setProperties(TrainProperties properties) {
      if (properties == null) {
         throw new IllegalArgumentException("Can not set properties to null");
      } else if (this.isUnloaded()) {
         throw new IllegalStateException("Group is unloaded");
      } else if (this.prop != properties) {
         if (this.prop != null) {
            TrainPropertiesStore.remove(this.prop.getTrainName());
            TrainPropertiesStore.unbindGroupFromProperties(this.prop, this);
         }

         this.prop = properties;
         TrainPropertiesStore.bindGroupToProperties(this.prop, this);
      }
   }

   public ConfigurationNode exportConfig() {
      ConfigurationNode exportedConfig = this.saveConfig();
      exportedConfig.remove("claims");
      exportedConfig.set("usedModels", this.getAttachments().getUsedModelsAsExport());
      return exportedConfig;
   }

   public ConfigurationNode saveConfig() {
      return this.saveConfig(SaveLockOrientationMode.AUTOMATIC);
   }

   public ConfigurationNode saveConfig(SaveLockOrientationMode setSaveLockMode) {
      ConfigurationNode savedConfig = this.getProperties().saveToConfig().clone();
      savedConfig.remove("carts");
      List<ConfigurationNode> carts = (List)this.stream().map(MinecartMember::saveConfig).collect(Collectors.toCollection(ArrayList::new));
      Iterator var4;
      ConfigurationNode cart;
      if (setSaveLockMode == SaveLockOrientationMode.DISABLED) {
         var4 = carts.iterator();

         while(var4.hasNext()) {
            cart = (ConfigurationNode)var4.next();
            StandardProperties.LOCK_ORIENTATION_FLIPPED.writeToConfig(cart, Optional.empty());
         }
      } else if (setSaveLockMode == SaveLockOrientationMode.ENABLED_OVERRIDE) {
         var4 = carts.iterator();

         while(var4.hasNext()) {
            cart = (ConfigurationNode)var4.next();
            StandardProperties.LOCK_ORIENTATION_FLIPPED.writeToConfig(cart, Optional.of(CartLockOrientation.locked((Boolean)cart.get("flipped", false))));
         }
      } else if (setSaveLockMode == SaveLockOrientationMode.ENABLED || setSaveLockMode == SaveLockOrientationMode.AUTOMATIC && this.isSavedTrainOrientationLocked()) {
         int trainFlippedCounter = 0;
         Iterator var9 = carts.iterator();

         ConfigurationNode cart;
         while(var9.hasNext()) {
            cart = (ConfigurationNode)var9.next();
            CartLockOrientation ori = (CartLockOrientation)StandardProperties.LOCK_ORIENTATION_FLIPPED.readFromConfig(cart).orElse(CartLockOrientation.NONE);
            if (ori != CartLockOrientation.NONE) {
               if (ori.isFlipped() == (Boolean)cart.get("flipped", false)) {
                  --trainFlippedCounter;
               } else {
                  ++trainFlippedCounter;
               }
            }
         }

         if (trainFlippedCounter > 0) {
            carts.forEach(StandardProperties::reverseSavedCart);
            Collections.reverse(carts);
         }

         var9 = carts.iterator();

         while(var9.hasNext()) {
            cart = (ConfigurationNode)var9.next();
            StandardProperties.LOCK_ORIENTATION_FLIPPED.writeToConfig(cart, Optional.of(CartLockOrientation.locked((Boolean)cart.get("flipped", false))));
         }
      }

      savedConfig.setNodeList("carts", carts);
      return savedConfig;
   }

   public boolean isSavedTrainOrientationLocked() {
      Iterator var1 = this.iterator();

      MinecartMember member;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         member = (MinecartMember)var1.next();
      } while(member.getProperties().get(StandardProperties.LOCK_ORIENTATION_FLIPPED) == CartLockOrientation.NONE);

      return true;
   }

   public SignTrackerGroup getSignTracker() {
      return this.signTracker;
   }

   public ActionTrackerGroup getActions() {
      return this.actionTracker;
   }

   public RailTrackerGroup getRailTracker() {
      return this.railTracker;
   }

   public AttachmentControllerGroup getAttachments() {
      return this.attachmentController;
   }

   public MinecartMember<?> head(int index) {
      return (MinecartMember)this.get(index);
   }

   public MinecartMember<?> head() {
      return this.head(0);
   }

   public MinecartMember<?> tail(int index) {
      return (MinecartMember)this.get(this.size() - 1 - index);
   }

   public MinecartMember<?> tail() {
      return this.tail(0);
   }

   public MinecartMember<?> middle() {
      return (MinecartMember)this.get((int)Math.floor((double)this.size() / 2.0D));
   }

   public Iterator<MinecartMember<?>> iterator() {
      final Iterator<MinecartMember<?>> listIter = super.iterator();
      return new Iterator<MinecartMember<?>>() {
         public boolean hasNext() {
            return listIter.hasNext();
         }

         public MinecartMember<?> next() {
            try {
               return (MinecartMember)listIter.next();
            } catch (ConcurrentModificationException var2) {
               throw new MemberMissingException();
            }
         }

         public void remove() {
            listIter.remove();
         }
      };
   }

   public MinecartMember<?>[] toArray() {
      return (MinecartMember[])super.toArray(new MinecartMember[0]);
   }

   public boolean connect(MinecartMember<?> contained, MinecartMember<?> with) {
      if (this.size() <= 1) {
         this.add(with);
      } else if (this.head() == contained && this.canConnect(with, 0)) {
         this.add(0, (MinecartMember)with);
      } else {
         if (this.tail() != contained || !this.canConnect(with, this.size() - 1)) {
            return false;
         }

         this.add(with);
      }

      return true;
   }

   public boolean containsIndex(int index) {
      return !this.isEmpty() && index >= 0 && index < this.size();
   }

   public World getWorld() {
      return this.isEmpty() ? null : ((MinecartMember)this.get(0)).getWorld();
   }

   public int size(EntityType carttype) {
      int rval = 0;
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var3.next();
         if (((CommonMinecart)mm.getEntity()).getType() == carttype) {
            ++rval;
         }
      }

      return rval;
   }

   public boolean isValid() {
      return !this.isEmpty() && (this.size() == 1 || !this.getProperties().isPoweredMinecartRequired() || this.size(EntityType.MINECART_FURNACE) > 0);
   }

   public void add(int index, MinecartMember<?> member) {
      if (member.isUnloaded()) {
         throw new IllegalArgumentException("Can not add unloaded members to groups");
      } else {
         super.add(index, member);
         this.fireMemberAddEvent(member);
         this.onMemberAdded(member);
      }
   }

   public boolean add(MinecartMember<?> member) {
      if (member.isUnloaded()) {
         throw new IllegalArgumentException("Can not add unloaded members to groups");
      } else {
         super.add(member);
         this.fireMemberAddEvent(member);
         this.onMemberAdded(member);
         return true;
      }
   }

   public boolean addAll(int index, Collection<? extends MinecartMember<?>> members) {
      super.addAll(index, members);
      MinecartMember<?>[] memberArr = (MinecartMember[])members.toArray(new MinecartMember[0]);
      MinecartMember[] var4 = memberArr;
      int var5 = memberArr.length;

      int var6;
      MinecartMember member;
      for(var6 = 0; var6 < var5; ++var6) {
         member = var4[var6];
         if (member.isUnloaded()) {
            throw new IllegalArgumentException("Can not add unloaded members to groups");
         }

         this.fireMemberAddEvent(member);
      }

      var4 = memberArr;
      var5 = memberArr.length;

      for(var6 = 0; var6 < var5; ++var6) {
         member = var4[var6];
         this.onMemberAdded(member);
      }

      return true;
   }

   public boolean addAll(Collection<? extends MinecartMember<?>> members) {
      super.addAll(members);
      MinecartMember<?>[] memberArr = (MinecartMember[])members.toArray(new MinecartMember[0]);
      MinecartMember[] var3 = memberArr;
      int var4 = memberArr.length;

      int var5;
      MinecartMember member;
      for(var5 = 0; var5 < var4; ++var5) {
         member = var3[var5];
         if (member.isUnloaded()) {
            throw new IllegalArgumentException("Can not add unloaded members to groups");
         }

         this.fireMemberAddEvent(member);
      }

      var3 = memberArr;
      var4 = memberArr.length;

      for(var5 = 0; var5 < var4; ++var5) {
         member = var3[var5];
         this.onMemberAdded(member);
      }

      return true;
   }

   public boolean removeSilent(MinecartMember<?> member) {
      int index = this.indexOf(member);
      if (index == -1) {
         return false;
      } else {
         this.removeMember(index);
         if (this.isEmpty()) {
            this.remove();
         }

         return true;
      }
   }

   public boolean remove(Object o) {
      int index = this.indexOf(o);
      return index != -1 && this.remove(index) != null;
   }

   public MinecartMember<?> remove(int index) {
      MinecartMember<?> removed = this.removeMember(index);
      if (this.isEmpty()) {
         this.remove();
      } else {
         if (TCConfig.playHissWhenCartRemoved) {
            removed.playLinkEffect();
         }

         this.split(index);
      }

      return removed;
   }

   private MinecartMember<?> removeMember(int index) {
      this.chunkAreaValid = false;
      notifyPhysicsChange();
      MinecartMember<?> member = (MinecartMember)super.get(index);
      MemberRemoveEvent.call(member);
      super.remove(index);
      this.getActions().removeActions(member);
      this.onMemberRemoved(member);
      member.group = null;
      return member;
   }

   private void onCompositionChanged() {
      this.chunkAreaValid = false;
      this.attachmentController.notifyGroupCompositionChanged();
   }

   private void fireMemberAddEvent(MinecartMember<?> member) {
      boolean wasGroupNull = false;
      if (member.group == null) {
         member.group = this;
         wasGroupNull = true;
      }

      CommonUtil.callEvent(new MemberAddEvent(member, this));
      if (wasGroupNull && member.group == this) {
         member.group = null;
      }

   }

   private void onMemberAdded(MinecartMember<?> member) {
      this.onCompositionChanged();
      notifyPhysicsChange();
      member.setGroup(this);
      this.getSignTracker().updatePosition();
      this.getProperties().add(member.getProperties());
   }

   private void onMemberRemoved(MinecartMember<?> member) {
      this.onCompositionChanged();
      this.getSignTracker().onMemberRemoved(member);
      this.getProperties().remove(member.getProperties());
      this.getRailTracker().removeMemberRails(member);
      RailLookup.removeMemberFromAll(member);
   }

   public MinecartGroup split(int at) {
      Util.checkMainThread("MinecartGroup::split()");
      if (at <= 0) {
         return this;
      } else if (at >= this.size()) {
         return null;
      } else {
         List<MinecartMember<?>> splitMembers = new ArrayList();
         int count = this.size();

         for(int i = at; i < count; ++i) {
            splitMembers.add(this.removeMember(this.size() - 1));
         }

         MinecartGroup gnew = MinecartGroupStore.createSplitFrom(this.getProperties(), (MinecartMember[])splitMembers.toArray(new MinecartMember[0]));
         if (!this.isValid()) {
            this.remove();
         } else {
            this.onGroupCreated();
         }

         return gnew;
      }
   }

   public void clear() {
      this.unregisterFromServer(false);
      TrainProperties properties = this.getProperties();
      MinecartMember[] var2 = this.toArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MinecartMember<?> mm = var2[var4];
         properties.remove(mm.getProperties());
         if (((CommonMinecart)mm.getEntity()).isRemoved()) {
            mm.onDie(true);
         } else {
            mm.group = null;
            mm.group = MinecartGroupStore.createSplitFrom(properties, mm);
         }
      }

      super.clear();
   }

   public void remove() {
      Util.checkMainThread("MinecartGroup::remove()");
      if (groups.remove(this)) {
         GroupRemoveEvent.call(this);
         this.clear();
         if (this.prop != null) {
            TrainPropertiesStore.remove(this.prop.getTrainName());
            TrainPropertiesStore.unbindGroupFromProperties(this.prop, this);
            this.prop = null;
         }

      }
   }

   public void destroy() {
      List<MinecartMember<?>> copy = new ArrayList(this);
      Iterator var2 = copy.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var2.next();
         ((CommonMinecart)mm.getEntity()).remove();
      }

      this.remove();
   }

   public boolean isUnloaded() {
      return this.unloaded;
   }

   public void unload() {
      if (!this.unloaded) {
         Util.checkMainThread("MinecartGroup::unload()");
         this.unloaded = true;

         Iterator var1;
         MinecartMember member;
         try {
            var1 = this.iterator();

            while(true) {
               if (!var1.hasNext()) {
                  GroupUnloadEvent.call(this);
                  OfflineGroup offlineGroup = OfflineGroupManager.saveGroup(this);
                  this.unregisterFromServer(true);
                  if (offlineGroup != null) {
                     this.traincarts.getOfflineGroups().storeGroup(offlineGroup);
                  }

                  this.stop(false);
                  break;
               }

               member = (MinecartMember)var1.next();
               member.group = this;
               member.setUnloaded(false);
            }
         } finally {
            groups.remove(this);
         }

         var1 = this.iterator();

         while(var1.hasNext()) {
            member = (MinecartMember)var1.next();
            member.group = null;
            member.unloadedLastPlayerTakable = this.getProperties().isPlayerTakeable();
            member.setUnloaded(true);
            ((CommonMinecart)member.getEntity()).doPostTick();
         }

         super.clear();
         if (this.prop != null) {
            TrainPropertiesStore.unbindGroupFromProperties(this.prop, this);
         }

         this.prop = null;
      }
   }

   private void unregisterFromServer(boolean unloaded) {
      this.getSignTracker().unload(unloaded ? SignTracker.ClearMode.UNLOAD : SignTracker.ClearMode.LEAVE);
      this.getRailTracker().unload();
      this.getActions().clear();
      MutexZoneCache.unloadGroupInSlots(this);
      this.chunkArea.reset();
      this.chunkAreaValid = false;
      this.onCompositionChanged();
   }

   public void respawn() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var1.next();
         mm.respawn();
      }

   }

   public void playLinkEffect() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var1.next();
         mm.playLinkEffect();
      }

   }

   public void stop() {
      this.stop(false);
   }

   public void stop(boolean cancelLocationChange) {
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> m = (MinecartMember)var2.next();
         m.stop(cancelLocationChange);
      }

   }

   public void limitSpeed() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var1.next();
         mm.limitSpeed();
      }

   }

   public void eject() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var1.next();
         mm.eject();
      }

   }

   public void teleportAndGo(Block railBlock, BlockFace direction) {
      double force = this.getAverageForce();
      this.teleport(railBlock, direction);
      this.stop();
      this.getActions().clear();
      if (Math.abs(force) > 0.01D) {
         this.tail().getActions().addActionLaunch(direction, 1.0D, force);
      }

   }

   public void teleportAndGo(Block railBlock, Vector direction) {
      double forwardVelocity = this.getAverageForce();
      this.teleport(railBlock, direction);
      this.stop();
      this.getActions().clear();
      if (Math.abs(forwardVelocity) > 0.01D) {
         MemberActionLaunchDirection action = new MemberActionLaunchDirection();
         action.initDistance(1.0D, forwardVelocity, direction);
         this.tail().getActions().addGroupAction(action);
      }

   }

   public void teleport(Block startRailBlock, BlockFace direction) {
      this.teleport(startRailBlock, FaceUtil.faceToVector(direction));
   }

   public void teleport(Block startRailBlock, Vector direction) {
      Location[] locations = new Location[this.size()];
      TrackWalkingPoint walker = new TrackWalkingPoint(startRailBlock, direction);
      walker.skipFirst();

      for(int i = 0; i < locations.length; ++i) {
         boolean canMove;
         if (i == 0) {
            canMove = walker.move(0.0D);
         } else {
            canMove = walker.move(((MinecartMember)this.get(i - 1)).getPreferredDistance((MinecartMember)this.get(i)));
         }

         if (canMove) {
            locations[i] = walker.state.positionLocation();
         } else {
            if (i <= 0) {
               return;
            }

            locations[i] = locations[i - 1].clone();
         }
      }

      this.teleport(locations, true);
   }

   public void teleport(Location[] locations) {
      this.teleport(locations, false);
   }

   public void teleport(Location[] locations, boolean reversed) {
      if (!LogicUtil.nullOrEmpty(locations) && locations.length == this.size()) {
         this.teleportImmunityTick = 10;
         this.getSignTracker().clear();
         this.getSignTracker().updatePosition();
         this.breakPhysics();
         Iterator var3 = this.iterator();

         MinecartMember member;
         while(var3.hasNext()) {
            member = (MinecartMember)var3.next();
            member.getAttachments().startTeleport();
         }

         locations = (Location[])locations.clone();

         int i;
         for(i = 0; i < this.size(); ++i) {
            if (((MinecartMember)this.get(i)).isOrientationInverted()) {
               int locIndx = reversed ? locations.length - i - 1 : i;
               locations[locIndx] = Util.invertRotation(locations[locIndx].clone());
            }
         }

         if (reversed) {
            for(i = 0; i < locations.length; ++i) {
               this.teleportMember((MinecartMember)this.get(i), locations[locations.length - i - 1]);
            }
         } else {
            for(i = 0; i < locations.length; ++i) {
               this.teleportMember((MinecartMember)this.get(i), locations[i]);
            }
         }

         this.updateDirection();
         this.updateChunkInformation(!this.canUnload(), false);
         this.updateWheels();
         this.getSignTracker().updatePosition();
         var3 = this.iterator();

         while(var3.hasNext()) {
            member = (MinecartMember)var3.next();
            member.getAttachments().finishTeleport();
         }

      }
   }

   private void teleportMember(MinecartMember<?> member, Location location) {
      member.getWheels().startTeleport();
      ((CommonMinecart)member.getEntity()).teleport(location);
      member.getOrientation();
   }

   public void flipOrientation() {
      if (!this.isEmpty()) {
         if (this.size() == 1) {
            this.head().flipOrientation();
         } else {
            double shiftDistance = 0.5D * ((double)((CommonMinecart)this.tail().getEntity()).getWidth() - (double)((CommonMinecart)this.head().getEntity()).getWidth());
            MinecartGroup.FlippedMember currentMember = null;
            boolean areAllCartsReachable = true;

            for(int i = this.size() - 1; i >= 1; --i) {
               double distance = this.head(i).calculateRailDistanceToMemberAhead(this.head(i - 1));
               if (Double.isNaN(distance)) {
                  distance = this.head(i).getPreferredDistance(this.head(i - 1));
                  areAllCartsReachable = false;
               }

               MinecartGroup.FlippedMember next = new MinecartGroup.FlippedMember(this.head(i), distance);
               next.next = currentMember;
               currentMember = next;
            }

            MinecartGroup.FlippedMember next = new MinecartGroup.FlippedMember(this.head(), Math.max(0.0D, -shiftDistance));
            next.next = currentMember;
            currentMember = next;
            RailState current;
            if (areAllCartsReachable) {
               RailTracker.TrackedRailWalker walker = this.tail().getRailTracker().getTrackedRailWalker();
               if (shiftDistance > 0.0D) {
                  walker.invertMotion();
                  shiftDistance -= walker.move(shiftDistance);
                  walker.invertMotion();
               }

               if (shiftDistance > 0.0D) {
                  walker.invertMotion();
                  walker.state().initEnterDirection();
                  TrackWalkingPoint p = new TrackWalkingPoint(walker.state());
                  p.skipFirst();
                  p.move(shiftDistance);
                  current = p.state;
                  current.position().invertMotion();
                  current.initEnterDirection();
               } else {
                  do {
                     currentMember.distanceRemaining -= walker.move(currentMember.distanceRemaining);
                     if (!(currentMember.distanceRemaining <= 0.0D)) {
                        break;
                     }

                     currentMember.flippedState = walker.state().clone();
                     currentMember.flippedState.initEnterDirection();
                     currentMember = currentMember.next;
                  } while(currentMember != null);

                  current = walker.state();
                  current.initEnterDirection();
               }
            } else {
               RailTracker.TrackedRail currentRail = null;

               for(int i = this.size() - 1; i >= 0; --i) {
                  MinecartMember<?> member = (MinecartMember)this.get(i);
                  if (!member.isDerailed()) {
                     currentRail = member.getRailTracker().getRail();
                     break;
                  }
               }

               if (currentRail == null) {
                  this.flipOrientationFallback();
                  return;
               }

               current = currentRail.state.clone();
               current.initEnterDirection();
            }

            if (currentMember != null) {
               TrackWalkingPoint p = new TrackWalkingPoint(current);
               p.skipFirst();

               do {
                  if (!p.move(currentMember.distanceRemaining)) {
                     this.flipOrientationFallback();
                     return;
                  }

                  currentMember.flippedState = p.state.clone();
                  currentMember = currentMember.next;
               } while(currentMember != null);
            }

            this.applyFlippedStates(next);
         }
      }
   }

   private void flipOrientationFallback() {
      MinecartGroup.FlippedMember current = null;

      for(int i = 0; i < this.size(); ++i) {
         MinecartMember<?> member = this.head(i);
         MinecartMember<?> swapped = this.tail(i);
         if (member != swapped) {
            MinecartGroup.FlippedMember flipped = new MinecartGroup.FlippedMember(member, 0.0D);
            flipped.flippedState = swapped.getRailTracker().getState().clone();
            flipped.next = current;
            current = flipped;
         }
      }

      this.applyFlippedStates(current);
   }

   private void applyFlippedStates(MinecartGroup.FlippedMember rootMember) {
      for(MinecartGroup.FlippedMember currentMember = rootMember; currentMember != null; currentMember = currentMember.next) {
         currentMember.apply();
      }

      this.updateDirection();
      this.updateWheels();
      this.getAttachments().syncRespawn();
   }

   public boolean isTeleportImmune() {
      return this.teleportImmunityTick > 0;
   }

   public void shareForce() {
      double f = this.getAverageForce();
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         MinecartMember<?> m = (MinecartMember)var3.next();
         m.setForwardForce(f);
      }

   }

   public void setForwardForce(double force) {
      Iterator var3 = this.iterator();

      while(true) {
         while(var3.hasNext()) {
            MinecartMember<?> mm = (MinecartMember)var3.next();
            double currvel = mm.getForce();
            if (!(currvel <= 0.01D) && !(Math.abs(force) < 0.01D)) {
               ((CommonMinecart)mm.getEntity()).vel.multiply(force / currvel);
            } else {
               mm.setForwardForce(force);
            }
         }

         return;
      }
   }

   public List<String> getAnimationNames() {
      if (this.isEmpty()) {
         return Collections.emptyList();
      } else {
         return this.size() == 1 ? ((MinecartMember)this.get(0)).getAnimationNames() : Collections.unmodifiableList((List)this.stream().flatMap((m) -> {
            return m.getAnimationNames().stream();
         }).distinct().collect(Collectors.toList()));
      }
   }

   public Set<String> getAnimationScenes(String animationName) {
      if (this.isEmpty()) {
         return Collections.emptySet();
      } else {
         return this.size() == 1 ? ((MinecartMember)this.get(0)).getAnimationScenes(animationName) : Collections.unmodifiableSet((Set)this.stream().flatMap((m) -> {
            return m.getAnimationScenes(animationName).stream();
         }).collect(Collectors.toSet()));
      }
   }

   public boolean playNamedAnimation(String name) {
      return AnimationController.super.playNamedAnimation(name);
   }

   public boolean playNamedAnimation(AnimationOptions options) {
      boolean success = false;

      MinecartMember member;
      for(Iterator var3 = this.iterator(); var3.hasNext(); success |= member.playNamedAnimation(options)) {
         member = (MinecartMember)var3.next();
      }

      return success;
   }

   public boolean playNamedAnimationFor(int[] targetPath, AnimationOptions options) {
      boolean success = false;

      MinecartMember member;
      for(Iterator var4 = this.iterator(); var4.hasNext(); success |= member.playNamedAnimationFor(targetPath, options)) {
         member = (MinecartMember)var4.next();
      }

      return success;
   }

   public boolean playAnimationFor(int[] targetPath, Animation animation) {
      boolean success = false;

      MinecartMember member;
      for(Iterator var4 = this.iterator(); var4.hasNext(); success |= member.playAnimationFor(targetPath, animation)) {
         member = (MinecartMember)var4.next();
      }

      return success;
   }

   public boolean canConnect(MinecartMember<?> mm, int at) {
      if (this.size() == 1) {
         return true;
      } else if (this.size() == 0) {
         return false;
      } else {
         CommonMinecart connectedEnd;
         CommonMinecart otherEnd;
         if (at == 0) {
            if (!this.head().isNearOf(mm)) {
               return false;
            }

            connectedEnd = (CommonMinecart)this.head().getEntity();
            otherEnd = (CommonMinecart)this.tail().getEntity();
         } else {
            if (at != this.size() - 1) {
               return false;
            }

            if (!this.tail().isNearOf(mm)) {
               return false;
            }

            connectedEnd = (CommonMinecart)this.tail().getEntity();
            otherEnd = (CommonMinecart)this.head().getEntity();
         }

         return connectedEnd.loc.distanceSquared(mm.getEntity()) < otherEnd.loc.distanceSquared(mm.getEntity());
      }
   }

   private void refreshRailTrackerIfChanged() {
      MinecartMember member;
      for(Iterator var1 = this.iterator(); var1.hasNext(); hasPhysicsChanges |= member.railDetectPositionChange()) {
         member = (MinecartMember)var1.next();
      }

      if (hasPhysicsChanges) {
         hasPhysicsChanges = false;
         this.getRailTracker().refresh();
      }

   }

   public void updateDirection() {
      if (this.size() == 1) {
         this.refreshRailTrackerIfChanged();
         this.head().updateDirection();
      } else if (this.size() > 1) {
         int var1 = 0;

         while(true) {
            this.refreshRailTrackerIfChanged();
            Iterator var2 = this.iterator();

            while(var2.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var2.next();
               member.updateDirection();
            }

            if (var1++ == 2) {
               break;
            }

            double fforce = 0.0D;

            MinecartMember m;
            VectorAbstract vel;
            for(Iterator var4 = this.iterator(); var4.hasNext(); fforce += m.getRailTracker().getState().position().motDot(vel.getX(), vel.getY(), vel.getZ())) {
               m = (MinecartMember)var4.next();
               vel = ((CommonMinecart)m.getEntity()).vel;
            }

            if (fforce >= 0.0D) {
               break;
            }

            this.reverseDataStructures();
            notifyPhysicsChange();
         }
      }

   }

   public void reverse() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var1.next();
         mm.reverseDirection();
      }

      this.reverseDataStructures();
      notifyPhysicsChange();
      this.updateDirection();
   }

   private void reverseDataStructures() {
      Collections.reverse(this);
      this.getRailTracker().reverseRailData();
   }

   private void updateWheels() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var1.next();
         member.getWheels().update();
      }

   }

   public double getAverageForce() {
      if (this.isEmpty()) {
         return 0.0D;
      } else if (this.size() == 1) {
         return ((MinecartMember)this.get(0)).getForce();
      } else {
         double force = 0.0D;

         MinecartMember m;
         for(Iterator var3 = this.iterator(); var3.hasNext(); force += m.getForwardForce()) {
            m = (MinecartMember)var3.next();
         }

         return force / (double)this.size();
      }
   }

   public List<Material> getTypes() {
      ArrayList<Material> types = new ArrayList(this.size());
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var2.next();
         types.add(((CommonMinecart)mm.getEntity()).getCombinedItem());
      }

      return types;
   }

   public boolean hasPassenger() {
      Iterator var1 = this.iterator();

      MinecartMember mm;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         mm = (MinecartMember)var1.next();
      } while(!((CommonMinecart)mm.getEntity()).hasPassenger());

      return true;
   }

   public boolean hasFuel() {
      Iterator var1 = this.iterator();

      MinecartMember mm;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         mm = (MinecartMember)var1.next();
      } while(!(mm instanceof MinecartMemberFurnace) || !((CommonMinecartFurnace)((MinecartMemberFurnace)mm).getEntity()).hasFuel());

      return true;
   }

   public boolean hasItems() {
      Iterator var1 = this.iterator();

      MinecartMember mm;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         mm = (MinecartMember)var1.next();
      } while(!(mm instanceof MinecartMemberChest) || !((MinecartMemberChest)mm).hasItems());

      return true;
   }

   public boolean hasItem(ItemParser item) {
      Iterator var2 = this.iterator();

      MinecartMember mm;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         mm = (MinecartMember)var2.next();
      } while(!(mm instanceof MinecartMemberChest) || !((MinecartMemberChest)mm).hasItem(item));

      return true;
   }

   public boolean isMoving() {
      return !this.isEmpty() && this.head().isMoving();
   }

   public boolean isMovingOrWaiting() {
      return this.isMoving() || this.getActions().isWaitAction();
   }

   public boolean canUnload() {
      if (!this.getProperties().isKeepingChunksLoaded() || TCConfig.keepChunksLoadedOnlyWhenMoving && !this.isMovingOrWaiting()) {
         Iterator var1 = this.iterator();

         MinecartMember member;
         do {
            if (!var1.hasNext()) {
               return !this.isTeleportImmune();
            }

            member = (MinecartMember)var1.next();
         } while(member.getEntity() == null || !((CommonMinecart)member.getEntity()).hasPlayerPassenger());

         return false;
      } else {
         return false;
      }
   }

   public boolean isRemoved() {
      return !groups.contains(this);
   }

   public Inventory getInventory() {
      Inventory[] source = (Inventory[])this.stream().map(CommonEntityController::getEntity).map(ExtendedEntity::getEntity).filter((e) -> {
         return e instanceof InventoryHolder;
      }).map((e) -> {
         return ((InventoryHolder)e).getInventory();
      }).toArray((x$0) -> {
         return new Inventory[x$0];
      });
      return new MergedInventory(source);
   }

   public Inventory getPlayerInventory() {
      Inventory[] source = (Inventory[])this.stream().flatMap((m) -> {
         return ((CommonMinecart)m.getEntity()).getPlayerPassengers().stream();
      }).map(HumanEntity::getInventory).toArray((x$0) -> {
         return new Inventory[x$0];
      });
      return new MergedInventory(source);
   }

   /** @deprecated */
   @Deprecated
   public void keepChunksLoaded(boolean keepLoaded) {
      this.keepChunksLoaded(keepLoaded ? ChunkLoadOptions.Mode.FULL : ChunkLoadOptions.Mode.DISABLED);
   }

   public void keepChunksLoaded(ChunkLoadOptions.Mode mode) {
      Iterator var2 = this.chunkArea.getAll().iterator();

      while(var2.hasNext()) {
         ChunkArea.OwnedChunk chunk = (ChunkArea.OwnedChunk)var2.next();
         chunk.keepLoaded(mode);
      }

   }

   public ChunkArea getChunkArea() {
      return this.chunkArea;
   }

   public boolean isInChunk(World world, long chunkLongCoord) {
      if (this.getWorld() != world) {
         return false;
      } else if (this.chunkAreaValid) {
         return this.chunkArea.containsChunk(chunkLongCoord);
      } else {
         int center_chunkX = MathUtil.longHashMsw(chunkLongCoord);
         int center_chunkZ = MathUtil.longHashLsw(chunkLongCoord);
         LongIterator chunkIter = this.loadChunksBuffer().longIterator();

         long chunk;
         do {
            if (!chunkIter.hasNext()) {
               return false;
            }

            chunk = chunkIter.next();
         } while(Math.abs(MathUtil.longHashMsw(chunk) - center_chunkX) > 2 || Math.abs(MathUtil.longHashLsw(chunk) - center_chunkZ) > 2);

         return true;
      }
   }

   public void onPropertiesChanged() {
      this.getSignTracker().update();
      MinecartMember[] var1 = this.toArray();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MinecartMember<?> member = var1[var3];
         member.onPropertiesChanged();
      }

   }

   public int getTicksLived() {
      int ticksLived = 0;

      MinecartMember member;
      for(Iterator var2 = this.iterator(); var2.hasNext(); ticksLived = Math.max(ticksLived, ((CommonMinecart)member.getEntity()).getTicksLived())) {
         member = (MinecartMember)var2.next();
      }

      return ticksLived;
   }

   public double getUpdateSpeedFactor() {
      return this.updateSpeedFactor;
   }

   public int getUpdateStepCount() {
      return this.updateStepCount;
   }

   public boolean isFirstUpdateStep() {
      return this.updateStepNr == 1;
   }

   public boolean isLastUpdateStep() {
      return this.updateStepNr == this.updateStepCount;
   }

   public void breakPhysics() {
      this.breakPhysics = true;
   }

   public List<TrainStatus> getStatusInfo() {
      List<TrainStatus> info = new ArrayList(3);
      info.addAll(this.getActions().getStatusInfo());
      Iterator var2 = this.iterator();

      MinecartMember member;
      while(var2.hasNext()) {
         member = (MinecartMember)var2.next();
         info.addAll(member.getActions().getStatusInfo());
      }

      info.addAll(this.obstacleTracker.getStatusInfo());
      var2 = this.iterator();

      while(var2.hasNext()) {
         member = (MinecartMember)var2.next();
         if (member.isDerailed()) {
            info.add(new TrainStatus.Derailed());
            break;
         }
      }

      if (this.getProperties().getSpeedLimit() <= 1.0E-5D) {
         info.add(new TrainStatus.WaitingZeroSpeedLimit());
      } else if (((CommonMinecart)this.head().getEntity()).getMaxSpeed() <= 1.0E-5D) {
         info.add(new TrainStatus.NotMovingSpeedLimited());
      } else {
         double speed = this.head().getRealSpeedLimited();
         if (speed <= 1.0E-5D) {
            info.add(new TrainStatus.NotMoving());
         } else {
            info.add(new TrainStatus.Moving(speed));
         }
      }

      if (this.getProperties().isKeepingChunksLoaded()) {
         info.add(new TrainStatus.KeepingChunksLoaded());
      }

      return info;
   }

   public int hashCode() {
      return System.identityHashCode(this);
   }

   public boolean equals(Object other) {
      return other == this;
   }

   public MinecartMember<?> getAt(IntVector3 position) {
      return this.getRailTracker().getMemberFromRails(position);
   }

   private boolean doConnectionCheck() {
      int i;
      for(i = 0; i < this.size() - 1; ++i) {
         if (((MinecartMember)this.get(i)).getRailTracker().isTrainSplit()) {
            for(int j = i + 1; j < this.size(); ++j) {
               ((CommonMinecart)((MinecartMember)this.get(j)).getEntity()).vel.divide(this.updateSpeedFactor);
            }

            MinecartGroup gnew = this.split(i + 1);
            if (gnew != null) {
               int time = (int)MathUtil.clamp(2.0D / gnew.head().getRealSpeed(), 20.0D, 40.0D);
               Iterator var4 = gnew.iterator();

               while(var4.hasNext()) {
                  MinecartMember<?> mm1 = (MinecartMember)var4.next();
                  Iterator var6 = this.iterator();

                  while(var6.hasNext()) {
                     MinecartMember<?> mm2 = (MinecartMember)var6.next();
                     mm1.ignoreCollision(((CommonMinecart)mm2.getEntity()).getEntity(), time);
                  }
               }
            }

            return false;
         }
      }

      for(i = 0; i < this.size() - 1; ++i) {
         MinecartMember<?> m1 = (MinecartMember)this.get(i);
         MinecartMember<?> m2 = (MinecartMember)this.get(i + 1);
         if ((m1.isDerailed() || m2.isDerailed()) && ((CommonMinecart)m1.getEntity()).loc.distance(((CommonMinecart)m2.getEntity()).loc) >= m1.getMaximumDistance(m2)) {
            this.split(i + 1);
            return false;
         }
      }

      return true;
   }

   private LongHashSet loadChunksBuffer() {
      chunksBuffer.clear();
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         MinecartMember<?> mm = (MinecartMember)var1.next();
         chunksBuffer.add(((CommonMinecart)mm.getEntity()).loc.x.chunk(), ((CommonMinecart)mm.getEntity()).loc.z.chunk());
      }

      return chunksBuffer;
   }

   public void onGroupCreated() {
      this.onPropertiesChanged();
      if (this.getProperties().isKeepingChunksLoaded()) {
         this.updateChunkInformation(true, false);
      }

   }

   private void updateChunkInformation(boolean keepChunksLoaded, boolean isRemoving) {
      ChunkLoadOptions options = keepChunksLoaded ? this.getProperties().getChunkLoadOptions() : ChunkLoadOptions.DEFAULT;
      int radius = keepChunksLoaded ? Math.min(TCConfig.maxKeepChunksLoadedRadius, options.radius()) : 2;
      this.chunkArea.refresh(this.getWorld(), radius, this.loadChunksBuffer());
      this.chunkAreaValid = true;
      Iterator var5;
      ChunkArea.OwnedChunk chunk;
      if (keepChunksLoaded) {
         var5 = this.chunkArea.getAdded().iterator();

         while(var5.hasNext()) {
            chunk = (ChunkArea.OwnedChunk)var5.next();
            chunk.keepLoaded(options.mode());
         }

         var5 = this.chunkArea.getAll().iterator();

         while(var5.hasNext()) {
            chunk = (ChunkArea.OwnedChunk)var5.next();
            if (chunk.getDistance() <= 1 && chunk.getPreviousDistance() > 1) {
               chunk.loadChunk();
            }
         }
      } else if (!isRemoving) {
         var5 = this.chunkArea.getAdded().iterator();

         while(var5.hasNext()) {
            chunk = (ChunkArea.OwnedChunk)var5.next();
            if (!chunk.isLoaded()) {
               this.unload();
               throw new GroupUnloadedException();
            }
         }
      }

   }

   public void logCartInfo(String header) {
      StringBuilder msg = new StringBuilder(this.size() * 7 + 10);
      msg.append(header);
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         MinecartMember<?> member = (MinecartMember)var3.next();
         msg.append(" [");
         msg.append(member.getDirection());
         msg.append(" - ").append(((CommonMinecart)member.getEntity()).vel);
         msg.append("]");
      }

      this.traincarts.log(Level.INFO, msg.toString());
   }

   public ObstacleTracker getObstacleTracker() {
      return this.obstacleTracker;
   }

   public List<ObstacleTracker.Obstacle> findObstaclesAhead(double distance, boolean trains, boolean railObstacles) {
      return this.obstacleTracker.findObstaclesAhead(distance, trains, railObstacles, 0.0D);
   }

   public boolean isObstacleAhead(double distance, boolean trains, boolean railObstacles) {
      return !this.findObstaclesAhead(distance, trains, railObstacles).isEmpty();
   }

   public ObstacleTracker.ObstacleSpeedLimit findObstacleSpeedLimit(double distance) {
      return this.findObstacleSpeedLimit(distance, this.getProperties().getWaitDeceleration());
   }

   public ObstacleTracker.ObstacleSpeedLimit findObstacleSpeedLimit(double distance, double deceleration) {
      double waitDistance = this.getProperties().getWaitDistance();
      List<ObstacleTracker.Obstacle> obstacles = this.obstacleTracker.findObstaclesAhead(distance, waitDistance > 0.0D, true, waitDistance);
      return ObstacleTracker.minimumSpeedLimit(obstacles, deceleration);
   }

   private void tickActions() {
      this.getActions().doTick();
   }

   protected void doPhysics(TrainCarts plugin) {
      if (this.isUnloaded()) {
         groups.remove(this);
      } else {
         for(int i = 0; i < this.size(); ++i) {
            MinecartMember<?> member = (MinecartMember)super.get(i);
            if (member.getEntity() == null) {
               CartPropertiesStore.remove(member.getProperties().getUUID());
               this.onMemberRemoved(member);
               super.remove(i--);
            } else if (member.group != this) {
               this.onMemberRemoved(member);
               super.remove(i--);
            }
         }

         MinecartMember m;
         boolean finishedRemoving;
         do {
            finishedRemoving = true;

            for(int i = 0; i < this.size(); ++i) {
               m = (MinecartMember)super.get(i);
               if (((CommonMinecart)m.getEntity()).isRemoved()) {
                  this.remove(i);
                  finishedRemoving = false;
                  break;
               }
            }
         } while(!finishedRemoving);

         if (super.isEmpty()) {
            this.remove();
         } else {
            Iterator var18;
            if (this.canUnload()) {
               var18 = this.iterator();

               while(var18.hasNext()) {
                  m = (MinecartMember)var18.next();
                  if (m.isUnloaded()) {
                     this.unload();
                     return;
                  }
               }
            } else {
               var18 = this.iterator();

               while(var18.hasNext()) {
                  m = (MinecartMember)var18.next();
                  m.setUnloaded(false);
               }
            }

            if (plugin.getTrainUpdateController().isTicking()) {
               try {
                  double totalforce = this.getAverageForce();
                  double speedlimit = this.getProperties().getSpeedLimit();
                  double realtimeFactor = this.getProperties().hasRealtimePhysics() ? plugin.getTrainUpdateController().getRealtimeFactor() : 1.0D;
                  if (realtimeFactor * totalforce > 0.4D && realtimeFactor * speedlimit > 0.4D) {
                     this.updateStepCount = (int)Math.ceil(realtimeFactor * speedlimit / 0.4D);
                     this.updateSpeedFactor = realtimeFactor / (double)this.updateStepCount;
                  } else {
                     this.updateStepCount = 1;
                     this.updateSpeedFactor = realtimeFactor;
                  }

                  Iterator var9;
                  MinecartMember mm;
                  if (this.updateStepCount > 1) {
                     var9 = this.iterator();

                     while(var9.hasNext()) {
                        mm = (MinecartMember)var9.next();
                        ((CommonMinecart)mm.getEntity()).vel.multiply(this.updateSpeedFactor);
                     }
                  }

                  for(int i = 1; i <= this.updateStepCount; ++i) {
                     this.updateStepNr = i;

                     while(!this.doPhysics_step()) {
                     }
                  }

                  var9 = this.iterator();

                  while(var9.hasNext()) {
                     mm = (MinecartMember)var9.next();
                     ((CommonMinecart)mm.getEntity()).vel.divide(this.updateSpeedFactor);
                     double newMaxSpeed = ((CommonMinecart)mm.getEntity()).getMaxSpeed() / this.updateSpeedFactor;
                     newMaxSpeed = Math.min(newMaxSpeed, this.getProperties().getSpeedLimit());
                     ((CommonMinecart)mm.getEntity()).setMaxSpeed(newMaxSpeed);
                  }

                  this.updateSpeedFactor = 1.0D;
                  var9 = this.iterator();

                  while(true) {
                     int cx;
                     int cz;
                     CommonEntity entity;
                     do {
                        do {
                           if (!var9.hasNext()) {
                              return;
                           }

                           mm = (MinecartMember)var9.next();
                           entity = mm.getEntity();
                        } while(!entity.isInLoadedChunk());

                        cx = entity.getChunkX();
                        cz = entity.getChunkZ();
                     } while(cx == entity.loc.x.chunk() && cz == entity.loc.z.chunk());

                     ChunkHandle.fromBukkit(entity.getWorld().getChunkAt(cx, cz)).markDirty();
                  }
               } catch (GroupUnloadedException var14) {
               } catch (Throwable var15) {
                  TrainProperties p = this.getProperties();
                  plugin.log(Level.SEVERE, "Failed to perform physics on train '" + p.getTrainName() + "' at " + p.getLocation() + ":");
                  plugin.handle(var15);
               }

            }
         }
      }
   }

   private boolean doPhysics_step() throws GroupUnloadedException {
      this.breakPhysics = false;

      try {
         if (this.isEmpty()) {
            this.remove();
            throw new GroupUnloadedException();
         } else {
            double forwardMovingSpeed = Math.min(this.getProperties().getSpeedLimit() * this.updateSpeedFactor, 0.4D);
            Iterator var3 = this.iterator();

            MinecartMember member;
            while(var3.hasNext()) {
               member = (MinecartMember)var3.next();
               member.checkMissing();
               ((CommonMinecart)member.getEntity()).setMaxSpeed(forwardMovingSpeed);
            }

            Iterator var12 = this.iterator();

            MinecartMember member;
            while(var12.hasNext()) {
               member = (MinecartMember)var12.next();
               member.getAttachments().fixNetworkController();
            }

            if (this.teleportImmunityTick > 0) {
               --this.teleportImmunityTick;
            }

            this.updateDirection();
            this.getSignTracker().refresh();
            var12 = this.iterator();

            while(var12.hasNext()) {
               member = (MinecartMember)var12.next();
               member.checkMissing();
               if (member.hasBlockChanged() | member.forcedBlockUpdate.clear()) {
                  MemberBlockChangeEvent.call(member, member.getLastBlock(), member.getBlock());
                  member.checkMissing();
                  member.onBlockChange(member.getLastBlock(), member.getBlock());
                  this.getSignTracker().updatePosition();
                  member.checkMissing();
               }
            }

            this.getSignTracker().refresh();
            this.updateDirection();
            if (!this.doConnectionCheck()) {
               return true;
            } else {
               this.tickActions();
               this.updateDirection();
               var12 = this.iterator();

               while(var12.hasNext()) {
                  member = (MinecartMember)var12.next();
                  member.onPhysicsStart();
               }

               var12 = this.iterator();

               while(var12.hasNext()) {
                  member = (MinecartMember)var12.next();
                  member.onPhysicsPreMove();
               }

               if (this.isEmpty()) {
                  return false;
               } else {
                  if (this.getProperties().isSlowingDown(SlowdownMode.GRAVITY)) {
                     forwardMovingSpeed = this.getProperties().getGravity() * this.getUpdateSpeedFactor() * this.getUpdateSpeedFactor();
                     var3 = this.iterator();

                     while(var3.hasNext()) {
                        member = (MinecartMember)var3.next();
                        if (!member.isUnloaded() && !member.isMovementControlled()) {
                           member.getRailLogic().onGravity(member, forwardMovingSpeed);
                        }
                     }
                  }

                  this.updateDirection();
                  var12 = this.iterator();

                  while(var12.hasNext()) {
                     member = (MinecartMember)var12.next();
                     member.getRailTracker().getRailType().onPreMove(member);
                  }

                  this.updateDirection();
                  if (this.size() > 1) {
                     forwardMovingSpeed = this.getAverageForce();
                     boolean performUpdate = true;

                     for(int i = 0; i < this.size() - 1; ++i) {
                        if (((MinecartMember)this.get(i)).getRailTracker().isTrainSplit()) {
                           performUpdate = false;
                           break;
                        }
                     }

                     if (performUpdate) {
                        Iterator var17 = this.iterator();

                        while(var17.hasNext()) {
                           MinecartMember<?> m = (MinecartMember)var17.next();
                           m.setForwardForce(forwardMovingSpeed);
                        }
                     }
                  } else {
                     forwardMovingSpeed = this.head().getForce();
                  }

                  if (this.isFirstUpdateStep()) {
                     this.obstacleTracker.update(forwardMovingSpeed / this.getUpdateSpeedFactor());
                  }

                  double thres = this.obstacleTracker.getSpeedLimit();
                  if (thres == Double.MAX_VALUE) {
                     thres = this.getProperties().getSpeedLimit();
                  }

                  thres = Math.min(0.4D, this.updateSpeedFactor * thres);
                  Iterator var16 = this.iterator();

                  MinecartMember member;
                  while(var16.hasNext()) {
                     member = (MinecartMember)var16.next();
                     ((CommonMinecart)member.getEntity()).setMaxSpeed(thres);
                  }

                  var3 = this.iterator();

                  while(var3.hasNext()) {
                     member = (MinecartMember)var3.next();
                     member.calculateSpeedFactor();
                  }

                  var3 = this.iterator();

                  while(var3.hasNext()) {
                     member = (MinecartMember)var3.next();
                     member.onPhysicsPostMove();
                     if (this.breakPhysics) {
                        return true;
                     }
                  }

                  if (this.isLastUpdateStep()) {
                     notifyPhysicsChange();
                  }

                  this.updateDirection();
                  if (!this.doConnectionCheck()) {
                     return true;
                  } else {
                     this.updateChunkInformation(!this.canUnload(), false);
                     this.updateWheels();
                     if (!this.isEmpty() && this.getProperties().isKeepingChunksLoaded()) {
                        thres = TCConfig.unloadRunawayTrainDistance * TCConfig.unloadRunawayTrainDistance;
                        var16 = this.iterator();

                        while(var16.hasNext()) {
                           member = (MinecartMember)var16.next();
                           Location derailedStartPos = member.getFirstKnownDerailedPosition();
                           if (derailedStartPos != null) {
                              double distanceSqSinceDerailed = ((CommonMinecart)member.getEntity()).loc.distanceSquared(derailedStartPos);
                              if (distanceSqSinceDerailed > thres) {
                                 Location loc = ((CommonMinecart)member.getEntity()).getLocation();
                                 this.traincarts.getLogger().log(Level.WARNING, "A cart of train " + this.getProperties().getTrainName() + " at world=" + loc.getWorld().getName() + " x=" + loc.getBlockX() + " y=" + loc.getBlockY() + " z=" + loc.getBlockZ() + " derailed and went moving/flying off into nowhere!");
                                 this.traincarts.getLogger().log(Level.WARNING, "The train's keepChunksLoaded property has been  reset to false to prevent endless chunks being generated");
                                 this.traincarts.getLogger().log(Level.WARNING, "The derailment likely occurred at x=" + derailedStartPos.getBlockX() + " y=" + derailedStartPos.getBlockY() + " z=" + derailedStartPos.getBlockZ());
                                 this.getProperties().setKeepChunksLoaded(false);
                                 break;
                              }
                           }
                        }
                     }

                     return true;
                  }
               }
            }
         }
      } catch (MemberMissingException var11) {
         return false;
      }
   }

   private static class FlippedMember {
      public final MinecartMember<?> member;
      public final boolean orientationInverted;
      public final double velocity;
      public double distanceRemaining;
      public RailState flippedState;
      public MinecartGroup.FlippedMember next;

      public FlippedMember(MinecartMember<?> member, double distanceFromBehind) {
         this.member = member;
         this.orientationInverted = member.isOrientationInverted();
         this.velocity = member.getForce();
         this.distanceRemaining = distanceFromBehind;
         this.flippedState = null;
         this.next = null;
      }

      public void apply() {
         Location position = this.flippedState.position().toLocation(this.flippedState.railBlock());
         Vector velocityVec = this.flippedState.motionVector().clone().multiply(this.velocity);
         Vector upVector = this.flippedState.position().getWheelOrientation().upVector();
         Vector forwardVector = this.flippedState.motionVector();
         if (!this.orientationInverted) {
            forwardVector.multiply(-1.0D);
         }

         Quaternion orientation = Quaternion.fromLookDirection(forwardVector, upVector);
         ((CommonMinecart)this.member.getEntity()).setPosition(position.getX(), position.getY(), position.getZ());
         ((CommonMinecart)this.member.getEntity()).setVelocity(velocityVec);
         this.member.setOrientation(orientation);
         this.member.getWheels().startTeleport();
      }
   }
}
