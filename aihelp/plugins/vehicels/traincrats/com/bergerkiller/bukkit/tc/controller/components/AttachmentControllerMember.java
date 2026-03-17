package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.ToggledState;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.controller.EntityNetworkController;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TCSeatChangeListener;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentNameLookup;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentWorldFeatures;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfig;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfigListener;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfigModelTracker;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfigTracker;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModel;
import com.bergerkiller.bukkit.tc.attachments.config.SavedAttachmentModelStore;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.helper.AttachmentUpdateTransformHelper;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberNetwork;
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatChangeEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatEnterEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberBeforeSeatExitEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatChangeEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatEnterEvent;
import com.bergerkiller.bukkit.tc.events.seat.MemberSeatExitEvent;
import com.bergerkiller.bukkit.tc.utils.SetCallbackCollector;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AttachmentControllerMember implements AttachmentConfigListener, AttachmentManager, SavedAttachmentModelStore.ModelUsing, AttachmentNameLookup.Supplier {
   private final MinecartMember<?> member;
   private final TrainCarts plugin;
   private AttachmentModel model;
   private AttachmentConfigModelTracker modelTracker;
   private Attachment rootAttachment;
   private List<CartAttachmentSeat> seatAttachments = Collections.emptyList();
   private final Map<Entity, CartAttachmentSeat> cachedSeatAttachmentsByPassenger = new HashMap();
   private List<Attachment> flattenedAttachments = Collections.emptyList();
   private final Map<Attachment, AttachmentNameLookup> cachedNameLookups = new IdentityHashMap();
   private Map<Entity, AttachmentControllerMember.SeatHint> seatHints = new HashMap();
   private final Map<Player, AttachmentViewer> viewers = new IdentityHashMap();
   private final Map<Entity, Vector> previousSeatPositions = new IdentityHashMap();
   private final AttachmentWorldFeatures.Tracker worldFeaturesTracker = new AttachmentWorldFeatures.Tracker();
   private boolean changeListenerSeatsAddedOrRemoved = false;
   protected final ToggledState networkInvalid = new ToggledState();
   private boolean attached = false;
   private boolean hidden = false;
   private long animationCurrentTime = 0L;
   private double animationDeltaTime = 0.0D;
   private boolean teleporting = false;
   private boolean recreateAfterTeleport = false;
   private Set<Player> viewersAddedWhileTeleporting = Collections.emptySet();

   public AttachmentControllerMember(MinecartMember<?> member) {
      this.member = member;
      this.plugin = member.getTrainCarts();
   }

   public boolean isAttached() {
      return this.attached;
   }

   public synchronized void onAttached() {
      if (!this.teleporting) {
         this.animationCurrentTime = System.currentTimeMillis();
         this.animationDeltaTime = 0.0D;
         this.attached = true;
         this.createRootAttachmentAndStartTracking();
      }
   }

   public synchronized void onDetached() {
      if (this.teleporting) {
         this.recreateAfterTeleport = true;
         this.makeHiddenForAll();
      } else {
         this.attached = false;

         try {
            this.destroyRootAttachmentAndStopTracking();
         } finally {
            this.viewers.clear();
         }

      }
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public synchronized void setHidden(boolean hidden) {
      if (this.hidden != hidden) {
         this.hidden = hidden;
         if (hidden) {
            this.destroyRootAttachmentAndStopTracking();
         } else {
            this.createRootAttachmentAndStartTracking();
         }

      }
   }

   public void startTeleport() {
      this.teleporting = true;
      this.makeHiddenForAll();
      this.viewersAddedWhileTeleporting = Collections.emptySet();
   }

   public void finishTeleport() {
      if (this.teleporting) {
         this.teleporting = false;

         try {
            if (this.recreateAfterTeleport) {
               this.onDetached();
               this.onAttached();
            }

            Iterator var1 = this.viewersAddedWhileTeleporting.iterator();

            while(var1.hasNext()) {
               Player viewer = (Player)var1.next();
               this.makeVisible(viewer);
            }
         } finally {
            this.viewersAddedWhileTeleporting = Collections.emptySet();
            this.recreateAfterTeleport = false;
         }
      }

   }

   public MinecartMember<?> getMember() {
      return this.member;
   }

   public void fixNetworkController() {
      if (this.networkInvalid.clear()) {
         EntityNetworkController<?> controller = ((CommonMinecart)this.member.getEntity()).getNetworkController();
         if (!(controller instanceof MinecartMemberNetwork)) {
            ((CommonMinecart)this.member.getEntity()).setNetworkController(new MinecartMemberNetwork(this.plugin));
         }
      }

   }

   public Attachment getRootAttachment() {
      if (!this.attached) {
         throw new IllegalStateException("This member has no network presence and was probably unloaded");
      } else if (this.hidden) {
         throw new IllegalStateException("This member's attachments are temporarily hidden");
      } else {
         if (this.rootAttachment == null) {
            AttachmentModel model = AttachmentModel.getDefaultModel(((CommonMinecart)this.member.getEntity()).getType());
            this.onAttachmentAdded(model.getRoot().get());
         }

         return this.rootAttachment;
      }
   }

   public List<Attachment> getAllAttachments() {
      return this.flattenedAttachments;
   }

   public synchronized AttachmentNameLookup getNameLookup() {
      return this.getNameLookup(this.getRootAttachment());
   }

   public synchronized AttachmentNameLookup getNameLookup(Attachment root) {
      return (AttachmentNameLookup)this.cachedNameLookups.compute(root, (r, prev) -> {
         return prev != null && prev.isValid() ? prev : AttachmentNameLookup.create(root);
      });
   }

   public double getAnimationDeltaTime() {
      return this.animationDeltaTime;
   }

   public synchronized void onPassengersChanged(List<Entity> oldPassengers, List<Entity> newPassengers) {
      Iterator var3 = this.seatAttachments.iterator();

      while(var3.hasNext()) {
         CartAttachmentSeat seat = (CartAttachmentSeat)var3.next();
         Entity oldPassenger = seat.getEntity();
         if (!newPassengers.contains(oldPassenger)) {
            seat.setEntity((Entity)null);
         }
      }

      var3 = newPassengers.iterator();

      while(var3.hasNext()) {
         Entity newPassenger = (Entity)var3.next();
         boolean isInSeat = false;
         Iterator var6 = this.seatAttachments.iterator();

         while(var6.hasNext()) {
            CartAttachmentSeat seat = (CartAttachmentSeat)var6.next();
            if (seat.getEntity() == newPassenger) {
               isInSeat = true;
               break;
            }
         }

         if (!isInSeat) {
            CartAttachmentSeat newSeat = this.findNewSeatForEntity(newPassenger);
            if (newSeat != null) {
               newSeat.setEntity(newPassenger);
            }
         }
      }

   }

   public synchronized boolean changeSeatsLookingAt(Entity passenger) {
      return this.changeSeats(passenger, this.findNewSeatForEntity(passenger), true);
   }

   public synchronized boolean changeSeats(Entity passenger, CartAttachmentSeat new_seat, boolean playerInitiated) {
      if (new_seat != null && new_seat.getController() != this) {
         throw new IllegalArgumentException("Cannot change seats to a seat of another member");
      } else {
         CartAttachmentSeat old_seat = this.findSeatOfExistingPassenger(passenger);
         return old_seat != null && new_seat != null && old_seat != new_seat ? handleSeatChange(passenger, old_seat, new_seat, playerInitiated) : false;
      }
   }

   public static boolean handleSeatChange(Entity passenger, CartAttachmentSeat old_seat, CartAttachmentSeat new_seat, boolean isPlayerInitiated) {
      if (old_seat == new_seat) {
         return false;
      } else {
         resetCachedSeatsByPassenger(old_seat);
         resetCachedSeatsByPassenger(new_seat);
         Location seatPosition = null;
         Location exitPosition = null;
         boolean exitPreserveRotation = true;
         if (old_seat != null && new_seat != null) {
            MemberBeforeSeatChangeEvent event = new MemberBeforeSeatChangeEvent(old_seat, new_seat, passenger, isPlayerInitiated);
            seatPosition = event.getSeatPosition();
            if (((MemberBeforeSeatChangeEvent)CommonUtil.callEvent(event)).isCancelled()) {
               return false;
            }

            new_seat = event.getEnteredSeat();
            exitPosition = event.getExitPosition();
            if (old_seat == new_seat) {
               return false;
            }
         } else if (old_seat != null) {
            seatPosition = old_seat.getPosition(passenger);
            exitPosition = old_seat.getEjectPosition(passenger);
            exitPreserveRotation = old_seat.isEjectRotationPreserved();
            MemberBeforeSeatExitEvent event = new MemberBeforeSeatExitEvent(old_seat, passenger, seatPosition, exitPosition, exitPreserveRotation, isPlayerInitiated);
            if (((MemberBeforeSeatExitEvent)CommonUtil.callEvent(event)).isCancelled()) {
               return false;
            }

            exitPosition = event.getExitPosition();
            exitPreserveRotation = event.isExitPlayerRotationPreserved();
         } else {
            if (new_seat == null) {
               return false;
            }

            MemberBeforeSeatEnterEvent event = new MemberBeforeSeatEnterEvent(new_seat, passenger, isPlayerInitiated, false, true);
            if (((MemberBeforeSeatEnterEvent)CommonUtil.callEvent(event)).isCancelled()) {
               return false;
            }

            new_seat = event.getSeat();
         }

         if (old_seat != null && new_seat != null && old_seat.getMember() == new_seat.getMember()) {
            old_seat.setEntity((Entity)null);
            new_seat.setEntity(passenger);
            CommonUtil.callEvent(new MemberSeatChangeEvent(old_seat, new_seat, passenger, seatPosition, exitPosition, isPlayerInitiated));
            CommonUtil.callEvent(new MemberSeatEnterEvent(new_seat, passenger, isPlayerInitiated, true, false));
            return true;
         } else {
            try {
               TCSeatChangeListener.suppressSeatChangeEvents = true;
               boolean enteredNewSeat;
               if (old_seat != null) {
                  if (!((CommonMinecart)old_seat.getMember().getEntity()).removePassenger(passenger) && ((CommonMinecart)old_seat.getMember().getEntity()).isPassenger(passenger)) {
                     enteredNewSeat = false;
                     return enteredNewSeat;
                  }

                  if (old_seat.getEntity() == passenger) {
                     old_seat.setEntity((Entity)null);
                  }
               }

               enteredNewSeat = false;
               if (new_seat != null) {
                  if (new_seat.getEntity() == null) {
                     new_seat.getController().storeSeatHint(passenger, new_seat);
                     enteredNewSeat = ((CommonMinecart)new_seat.getMember().getEntity()).addPassenger(passenger);
                     new_seat.getController().storeSeatHint(passenger, (CartAttachmentSeat)null);
                     enteredNewSeat &= new_seat.getEntity() == null;
                     if (enteredNewSeat) {
                        new_seat.setEntity(passenger);
                     }
                  } else if (new_seat.getEntity() == passenger) {
                     enteredNewSeat = true;
                  } else {
                     enteredNewSeat = false;
                  }
               }

               if (old_seat != null) {
                  if (enteredNewSeat) {
                     CommonUtil.callEvent(new MemberSeatChangeEvent(old_seat, new_seat, passenger, seatPosition, exitPosition, isPlayerInitiated));
                  } else {
                     CommonUtil.callEvent(new MemberSeatExitEvent(old_seat, passenger, seatPosition, exitPosition, exitPreserveRotation, isPlayerInitiated));
                  }
               }

               if (enteredNewSeat) {
                  CommonUtil.callEvent(new MemberSeatEnterEvent(new_seat, passenger, isPlayerInitiated, old_seat != null, old_seat == null || old_seat.getMember() != new_seat.getMember()));
               }

               boolean var8 = true;
               return var8;
            } finally {
               TCSeatChangeListener.suppressSeatChangeEvents = false;
               resetCachedSeatsByPassenger(old_seat);
               resetCachedSeatsByPassenger(new_seat);
            }
         }
      }
   }

   private static void resetCachedSeatsByPassenger(CartAttachmentSeat seat) {
      if (seat != null && seat.getManager() instanceof AttachmentControllerMember) {
         AttachmentControllerMember controller = (AttachmentControllerMember)seat.getManager();
         synchronized(controller) {
            controller.cachedSeatAttachmentsByPassenger.clear();
         }
      }

   }

   public boolean hasSeatHint(Entity passenger) {
      return this.seatHints.containsKey(passenger);
   }

   public synchronized CartAttachmentSeat findNewSeatForEntity(Entity passenger) {
      if (this.seatAttachments.isEmpty()) {
         return null;
      } else {
         AttachmentControllerMember.SeatHint seatHint = (AttachmentControllerMember.SeatHint)this.seatHints.get(passenger);
         List sortedSeats;
         if (seatHint != null && !seatHint.isExpired()) {
            sortedSeats = seatHint.seats;
         } else {
            Vector position = new Vector();
            EntityHandle handle = EntityHandle.fromBukkit(passenger);
            position.setX(handle.getLastX());
            position.setY(handle.getLastY());
            position.setZ(handle.getLastZ());
            sortedSeats = this.getSeatsClosestToPosition(position);
         }

         Iterator var7 = sortedSeats.iterator();

         CartAttachmentSeat seat;
         do {
            if (!var7.hasNext()) {
               return null;
            }

            seat = (CartAttachmentSeat)var7.next();
         } while(!seat.canEnter(passenger));

         return seat;
      }
   }

   private List<CartAttachmentSeat> getSeatsClosestToPosition(Vector position) {
      if (this.seatAttachments.size() <= 1) {
         return this.seatAttachments;
      } else {
         ArrayList<CartAttachmentSeat> result = new ArrayList(this.seatAttachments);
         Collections.sort(result, (o1, o2) -> {
            double d1 = o1.getTransform().toVector().distanceSquared(position);
            double d2 = o2.getTransform().toVector().distanceSquared(position);
            return Double.compare(d1, d2);
         });
         return Collections.unmodifiableList(result);
      }
   }

   private List<CartAttachmentSeat> getSeatsClosestToHitTest(Location eyeLocation) {
      if (this.seatAttachments.size() <= 1) {
         return this.seatAttachments;
      } else {
         Matrix4x4 cameraTransform = new Matrix4x4();
         cameraTransform.translateRotate(eyeLocation);
         cameraTransform.invert();
         ArrayList<CartAttachmentSeat> result = new ArrayList(this.seatAttachments);
         Collections.sort(result, (o1, o2) -> {
            double d1 = getViewDistance(cameraTransform, o1.getTransform().toVector());
            double d2 = getViewDistance(cameraTransform, o2.getTransform().toVector());
            return Double.compare(d1, d2);
         });
         return Collections.unmodifiableList(result);
      }
   }

   private static double getViewDistance(Matrix4x4 cameraTransform, Vector pos) {
      pos = pos.clone();
      cameraTransform.transformPoint(pos);
      return pos.getZ() >= 1.0E-6D && pos.getZ() <= 5.0D ? Math.sqrt(pos.getX() * pos.getX() + pos.getY() * pos.getY()) : 5.0D + pos.length();
   }

   public synchronized CartAttachmentSeat findSeat(Entity passenger) {
      CartAttachmentSeat seat = this.findSeatOfExistingPassenger(passenger);
      return seat != null ? seat : this.findNewSeatForEntity(passenger);
   }

   public synchronized CartAttachmentSeat findSeatOfExistingPassenger(Entity passenger) {
      if (this.seatAttachments.isEmpty()) {
         return null;
      } else {
         CartAttachmentSeat seat = (CartAttachmentSeat)this.cachedSeatAttachmentsByPassenger.get(passenger);
         if (seat != null && seat.getEntity() == passenger) {
            return seat;
         } else {
            Iterator var4 = this.seatAttachments.iterator();

            CartAttachmentSeat seat;
            do {
               if (!var4.hasNext()) {
                  return null;
               }

               seat = (CartAttachmentSeat)var4.next();
            } while(seat.getEntity() != passenger);

            this.cachedSeatAttachmentsByPassenger.put(passenger, seat);
            return seat;
         }
      }
   }

   public synchronized void makeVisible(Player viewer) {
      if (this.teleporting) {
         if (this.viewersAddedWhileTeleporting.isEmpty()) {
            this.viewersAddedWhileTeleporting = new LinkedHashSet();
         }

         this.viewersAddedWhileTeleporting.add(viewer);
      } else {
         AttachmentViewer attachmentViewer = this.asAttachmentViewer(viewer);
         this.viewers.put(viewer, attachmentViewer);
         if (!this.hidden) {
            HelperMethods.makeVisibleRecursive(this.getRootAttachment(), true, attachmentViewer);
         }

      }
   }

   public synchronized void makeHidden(Player viewer) {
      if (this.teleporting) {
         if (!this.viewersAddedWhileTeleporting.isEmpty()) {
            this.viewersAddedWhileTeleporting.remove(viewer);
         }

      } else {
         AttachmentViewer attachmentViewer = (AttachmentViewer)this.viewers.remove(viewer);
         if (attachmentViewer == null) {
            attachmentViewer = this.asAttachmentViewer(viewer);
         }

         if (!this.hidden && this.rootAttachment != null) {
            HelperMethods.makeHiddenRecursive(this.rootAttachment, true, attachmentViewer);
         }

      }
   }

   public Set<Player> getViewers() {
      return this.viewers.keySet();
   }

   public Collection<AttachmentViewer> getAttachmentViewers() {
      return this.viewers.values();
   }

   public AttachmentViewer asAttachmentViewer(Player player) {
      return this.plugin.getAttachmentViewer(player);
   }

   public synchronized boolean isViewer(Player player) {
      return this.viewers.containsKey(player);
   }

   public synchronized boolean isAttachment(int entityId) {
      Iterator var2 = this.flattenedAttachments.iterator();

      Attachment attachment;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         attachment = (Attachment)var2.next();
      } while(!attachment.containsEntityId(entityId));

      return true;
   }

   public void storeSeatHint(Player player) {
      this.seatHints.put(player, new AttachmentControllerMember.SeatHint(this.getSeatsClosestToHitTest(Util.getRealEyeLocation(player))));
   }

   public void storeSeatHint(Entity entity, CartAttachmentSeat seat) {
      if (seat == null) {
         this.seatHints.remove(entity);
      } else {
         this.seatHints.put(entity, new AttachmentControllerMember.SeatHint(Collections.singletonList(seat)));
      }

   }

   public void syncUnloaded() {
      this.syncMovement(false);
   }

   public synchronized void syncPrePositionUpdate(AttachmentUpdateTransformHelper updater) {
      if (this.isAttached()) {
         this.syncPrePositionUpdate();
         updater.start(this.getRootAttachment(), this.getLiveTransform());
      }

   }

   public synchronized void syncRespawn() {
      List<Player> oldViewers = new ArrayList(this.getViewers());
      this.makeHiddenForAll();
      this.plugin.getTrainUpdateController().syncPositions(this.member);
      Iterator var2 = oldViewers.iterator();

      while(var2.hasNext()) {
         Player viewer = (Player)var2.next();
         this.makeVisible(viewer);
      }

   }

   public synchronized void makeHiddenForAll() {
      Iterator iter = this.viewers.values().iterator();

      while(iter.hasNext()) {
         AttachmentViewer attachmentViewer = (AttachmentViewer)iter.next();
         iter.remove();
         HelperMethods.makeHiddenRecursive(this.rootAttachment, true, attachmentViewer);
      }

   }

   public boolean isUnloadedOrDead() {
      return this.member.isUnloaded() || ((CommonMinecart)this.member.getEntity()).isRemoved();
   }

   public void syncPrePositionUpdate() {
      if (!this.seatHints.isEmpty()) {
         Iterator iter = this.seatHints.values().iterator();

         while(iter.hasNext()) {
            if (((AttachmentControllerMember.SeatHint)iter.next()).isExpired()) {
               iter.remove();
            }
         }
      }

      if (!this.isUnloadedOrDead()) {
         this.getRootAttachment();
         if (TCConfig.animationsUseTickTime) {
            this.animationDeltaTime = 0.05D;
         } else {
            long time_now = System.currentTimeMillis();
            this.animationDeltaTime = 0.001D * (double)(time_now - this.animationCurrentTime);
            this.animationCurrentTime = time_now;
         }

      }
   }

   public void syncPostPositionUpdate() {
      if (this.rootAttachment != null && !this.isUnloadedOrDead()) {
         this.flattenedAttachments.forEach(Attachment::onTick);
      }
   }

   public void syncMovement(boolean absolute) {
      if (!this.isUnloadedOrDead()) {
         if (absolute && !(((CommonMinecart)this.member.getEntity()).getNetworkController() instanceof MinecartMemberNetwork)) {
            this.networkInvalid.set();
         }

         ((CommonMinecart)this.member.getEntity()).setPositionChanged(false);
         ((CommonMinecart)this.member.getEntity()).setVelocityChanged(false);
         if (this.rootAttachment != null) {
            this.flattenedAttachments.forEach((a) -> {
               a.onMove(absolute);
            });
         }

      }
   }

   public Matrix4x4 getLiveTransform() {
      Matrix4x4 transform = new Matrix4x4();
      transform.translate(this.member.getWheels().getPosition());
      transform.rotate(this.member.getOrientation());
      transform.rotateZ(this.member.getRoll());
      return transform;
   }

   public int getAvailableSeatCount(Entity passenger) {
      int count = 0;
      Iterator var3 = this.seatAttachments.iterator();

      while(var3.hasNext()) {
         CartAttachmentSeat seat = (CartAttachmentSeat)var3.next();
         if (seat.canEnter(passenger)) {
            ++count;
         }
      }

      return count;
   }

   public World getWorld() {
      return this.member.getWorld();
   }

   public AttachmentWorldFeatures getWorldFeatures() {
      return this.worldFeaturesTracker.get(this.getWorld());
   }

   private void createRootAttachmentAndStartTracking() {
      if (this.attached && !this.hidden) {
         this.model = this.member.getProperties().getModel();
         this.modelTracker = new AttachmentConfigModelTracker(this.model.getConfigTracker(), this.plugin) {
            public AttachmentConfigTracker findModelConfig(String name) {
               return AttachmentControllerMember.this.plugin.getSavedAttachmentModels().getModelOrNone(name).getConfigTracker();
            }
         };
         AttachmentConfig rootConfig = this.modelTracker.startTracking(this);
         this.destroyRootAttachment();
         this.onAttachmentAdded(rootConfig);
         this.onSynchronized(rootConfig);
      }
   }

   private void destroyRootAttachmentAndStopTracking() {
      try {
         this.destroyRootAttachment();
      } finally {
         if (this.modelTracker != null) {
            this.modelTracker.stopTracking(this);
            this.modelTracker = null;
         }

      }

   }

   private void destroyRootAttachment() {
      if (this.rootAttachment != null) {
         try {
            Iterator var1 = this.seatAttachments.iterator();

            while(var1.hasNext()) {
               CartAttachmentSeat seat = (CartAttachmentSeat)var1.next();
               Entity oldEntity = seat.getEntity();
               if (oldEntity != null) {
                  this.previousSeatPositions.put(oldEntity, seat.getTransform().toVector());
               }
            }

            var1 = this.viewers.values().iterator();

            while(var1.hasNext()) {
               AttachmentViewer viewer = (AttachmentViewer)var1.next();
               HelperMethods.makeHiddenRecursive(this.rootAttachment, true, viewer);
            }

            detachAttachments(this.flattenedAttachments);
         } finally {
            this.rootAttachment = null;
            this.changeListenerSeatsAddedOrRemoved = false;
            this.flattenedAttachments = Collections.emptyList();
            this.seatAttachments = Collections.emptyList();
            this.cachedSeatAttachmentsByPassenger.clear();
            this.invalidateCachedNameLookups();
         }
      }
   }

   private void invalidateCachedNameLookups() {
      this.cachedNameLookups.values().forEach(AttachmentNameLookup::invalidate);
      this.cachedNameLookups.clear();
   }

   private static void detachAttachments(List<Attachment> flattenedAttachments) {
      ListIterator iter = flattenedAttachments.listIterator(flattenedAttachments.size());

      while(iter.hasPrevious()) {
         HelperMethods.perform_onDetached_single((Attachment)iter.previous());
      }

   }

   private void updateFlattenedLists() {
      this.flattenedAttachments = HelperMethods.listAllAttachments(this.rootAttachment);
      this.seatAttachments = (List)this.flattenedAttachments.stream().filter((attachment) -> {
         return attachment instanceof CartAttachmentSeat;
      }).map((attachment) -> {
         return (CartAttachmentSeat)attachment;
      }).collect(StreamUtil.toUnmodifiableList());
      this.cachedSeatAttachmentsByPassenger.clear();
      this.invalidateCachedNameLookups();
   }

   public synchronized void onAttachmentRemoved(AttachmentConfig attachmentConfig) {
      if (attachmentConfig.isRoot()) {
         this.destroyRootAttachment();
      } else if (this.rootAttachment != null) {
         Attachment curr = this.rootAttachment;
         int[] var3 = attachmentConfig.childPath();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int p = var3[var5];
            curr = (Attachment)curr.getChildren().get(p);
         }

         Attachment curr_parent = curr.getParent();
         List<Attachment> removedAttachments = HelperMethods.listAllAttachments(curr);
         Iterator var11 = removedAttachments.iterator();

         while(var11.hasNext()) {
            Attachment removedAttachment = (Attachment)var11.next();
            if (removedAttachment instanceof CartAttachmentSeat) {
               CartAttachmentSeat seat = (CartAttachmentSeat)removedAttachment;
               Entity oldEntity = seat.getEntity();
               if (oldEntity != null) {
                  this.previousSeatPositions.put(oldEntity, seat.getTransform().toVector());
               }

               this.changeListenerSeatsAddedOrRemoved = true;
            }
         }

         boolean active = !HelperMethods.hasInactiveParent(curr);
         Iterator var14 = this.viewers.values().iterator();

         while(var14.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var14.next();
            HelperMethods.makeHiddenRecursive(curr, active, viewer);
         }

         detachAttachments(removedAttachments);
         curr_parent.removeChild(curr);
         this.updateFlattenedLists();
      }

   }

   public synchronized void onAttachmentAdded(AttachmentConfig attachmentConfig) {
      if (attachmentConfig.isRoot()) {
         this.destroyRootAttachment();
         this.rootAttachment = this.createAttachment(attachmentConfig);
         this.updateFlattenedLists();
         this.changeListenerSeatsAddedOrRemoved |= !this.seatAttachments.isEmpty();
         this.flattenedAttachments.forEach(HelperMethods::perform_onAttached_single);
         this.plugin.getTrainUpdateController().computeAttachmentTransform(this.rootAttachment, this.getLiveTransform());
         Iterator var2 = this.viewers.values().iterator();

         while(var2.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var2.next();
            HelperMethods.makeVisibleRecursive(this.rootAttachment, true, viewer);
         }
      } else {
         Attachment curr_parent = this.rootAttachment;
         int[] path = attachmentConfig.childPath();
         int limit = path.length - 1;

         int prevSeatCount;
         for(prevSeatCount = 0; prevSeatCount < limit; ++prevSeatCount) {
            curr_parent = (Attachment)curr_parent.getChildren().get(path[prevSeatCount]);
         }

         Attachment curr = this.createAttachment(attachmentConfig);
         curr_parent.addChild(attachmentConfig.childIndex(), curr);
         List<Attachment> addedAttachments = HelperMethods.listAllAttachments(curr);
         prevSeatCount = this.seatAttachments.size();
         this.updateFlattenedLists();
         if (this.seatAttachments.size() > prevSeatCount) {
            this.changeListenerSeatsAddedOrRemoved = true;
         }

         addedAttachments.forEach(HelperMethods::perform_onAttached_single);
         this.plugin.getTrainUpdateController().computeAttachmentTransform(this.rootAttachment, this.getLiveTransform());
         boolean active = !HelperMethods.hasInactiveParent(curr);
         Iterator var7 = this.viewers.values().iterator();

         while(var7.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var7.next();
            HelperMethods.makeVisibleRecursive(curr, active, viewer);
         }
      }

   }

   public synchronized void onAttachmentChanged(AttachmentConfig attachmentConfig) {
      Attachment curr = this.rootAttachment;
      if (curr != null) {
         curr = curr.findChild(attachmentConfig.childPath());
         AttachmentType type = this.getTypeRegistry().findOrEmpty(attachmentConfig.typeId());
         ConfigurationNode config = attachmentConfig.config();

         try {
            type.migrateConfiguration(config);
         } catch (Throwable var6) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to migrate attachment configuration of " + type.getName(), var6);
         }

         if (!curr.checkCanReload(config)) {
            this.onAttachmentRemoved(attachmentConfig);
            this.onAttachmentAdded(attachmentConfig);
            return;
         }

         Collection<String> oldNames = curr.getNames();
         curr.getInternalState().onLoad(this.getClass(), type, config);
         if (!oldNames.equals(curr.getNames())) {
            this.invalidateCachedNameLookups();
         }

         curr.onLoad(config);
      }

   }

   public void onAttachmentAction(AttachmentConfig attachmentConfig, Consumer<Attachment> action) {
      Attachment curr = this.rootAttachment;
      if (curr != null) {
         curr = curr.findChild(attachmentConfig.childPath());
         if (curr != null) {
            action.accept(curr);
         }
      }

   }

   public void onSynchronized(AttachmentConfig rootAttachmentConfig) {
      if (!this.member.onModelChanged(this.model)) {
         if (this.modelTracker != null) {
            this.modelTracker.stopTracking(this);
            this.modelTracker = null;
         }

      } else {
         this.putPassengersIntoSeats();
      }
   }

   private void putPassengersIntoSeats() {
      if (this.changeListenerSeatsAddedOrRemoved) {
         this.changeListenerSeatsAddedOrRemoved = false;
         List<Entity> allPassengers = ((CommonMinecart)this.member.getEntity()).getPassengers();
         List<Entity> remainingPassengers = new ArrayList(allPassengers.size());
         Iterator var3 = allPassengers.iterator();

         Entity entity;
         while(var3.hasNext()) {
            entity = (Entity)var3.next();
            if (this.findSeatOfExistingPassenger(entity) == null) {
               remainingPassengers.add(entity);
            }
         }

         while(!remainingPassengers.isEmpty()) {
            Entity entity = (Entity)remainingPassengers.get(0);
            Vector position = (Vector)this.previousSeatPositions.get(entity);
            if (position == null) {
               position = entity.getLocation().toVector();
            }

            boolean foundSeat = false;
            List<CartAttachmentSeat> seats = this.getSeatsClosestToPosition(position);
            Iterator var7 = seats.iterator();

            while(var7.hasNext()) {
               CartAttachmentSeat seat = (CartAttachmentSeat)var7.next();
               if (seat.getEntity() == null) {
                  seat.setEntity(entity);
                  remainingPassengers.remove(0);
                  foundSeat = true;
                  break;
               }
            }

            if (!foundSeat) {
               break;
            }
         }

         var3 = remainingPassengers.iterator();

         while(var3.hasNext()) {
            entity = (Entity)var3.next();
            ((CommonMinecart)this.member.getEntity()).removePassenger(entity);
         }
      }

      this.previousSeatPositions.clear();
      this.cachedSeatAttachmentsByPassenger.clear();
   }

   public void getUsedModels(SetCallbackCollector<SavedAttachmentModel> collector) {
      if (this.model != null) {
         this.model.getUsedModels(collector);
      }

   }

   private static class SeatHint {
      public final List<CartAttachmentSeat> seats;
      public final int expire;

      public SeatHint(List<CartAttachmentSeat> seats) {
         this.seats = seats;
         this.expire = CommonUtil.getServerTicks() + 2;
      }

      public boolean isExpired() {
         return CommonUtil.getServerTicks() >= this.expire;
      }
   }
}
