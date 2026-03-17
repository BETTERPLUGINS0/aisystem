package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentAnchor;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachment;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.dep.neznamytabnametaghider.TabNameTagHider;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributesHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import java.util.function.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class SeatedEntity {
   protected Entity entity = null;
   protected int tickEntered = -1;
   protected boolean showDummy = false;
   protected SeatedEntity.DisplayMode displayMode;
   protected final CartAttachmentSeat seat;
   public final SeatOrientation orientation;
   private VirtualEntity fakeMount;
   public int parentMountId;
   private boolean madeVisibleInFirstPerson;
   private TabNameTagHider.TabPlayerNameTagHider tabNameTagHider;

   public SeatedEntity(CartAttachmentSeat seat) {
      this.displayMode = SeatedEntity.DisplayMode.DEFAULT;
      this.orientation = new SeatOrientation();
      this.fakeMount = null;
      this.parentMountId = -1;
      this.madeVisibleInFirstPerson = false;
      this.tabNameTagHider = null;
      this.seat = seat;
   }

   public boolean isEmpty() {
      return this.entity == null;
   }

   public boolean isDisplayed() {
      return this.entity != null || this.showDummy;
   }

   public boolean isPlayer() {
      return this.entity instanceof Player;
   }

   public Entity getEntity() {
      return this.entity;
   }

   public int getTicksInSeat() {
      return this.tickEntered == -1 ? 0 : CommonUtil.getServerTicks() - this.tickEntered;
   }

   public boolean isDummyPlayer() {
      return this.showDummy;
   }

   public boolean isDummyPlayerDisplayed() {
      return this.showDummy && this.entity == null;
   }

   public final void setShowDummyPlayer(boolean show) {
      if (this.showDummy != show) {
         this.showDummy = show;
         if (this.entity == null) {
            this.updateMode(true);
         }
      }

   }

   public final void setEntity(Entity entity) {
      if (this.tabNameTagHider != null) {
         this.tabNameTagHider.show();
         this.tabNameTagHider = null;
      }

      if (this.entity != entity) {
         this.tickEntered = entity == null ? -1 : CommonUtil.getServerTicks();
      }

      this.entity = entity;
      if (entity instanceof Player && this.getDisplayMode() == SeatedEntity.DisplayMode.NO_NAMETAG) {
         this.tabNameTagHider = this.seat.getPlugin().getTabNameHider((Player)entity);
         if (this.tabNameTagHider != null) {
            this.tabNameTagHider.hide();
         }
      }

      this.updateMode(true);
   }

   public SeatedEntity.DisplayMode getDisplayMode() {
      return this.displayMode;
   }

   public void setDisplayMode(SeatedEntity.DisplayMode displayMode) {
      this.displayMode = displayMode;
   }

   protected void hideRealPlayer(AttachmentViewer viewer) {
      if (this.entity == viewer.getPlayer()) {
         FirstPersonView.setPlayerVisible(viewer, false);
      } else {
         viewer.getVehicleMountController().despawn(this.entity.getEntityId());
      }

   }

   protected void showRealPlayer(AttachmentViewer viewer) {
      if (viewer.getPlayer() == this.entity) {
         FirstPersonView.setPlayerVisible(viewer, true);
      } else {
         VehicleMountController vmc = viewer.getVehicleMountController();
         vmc.respawn((Player)this.entity, (theViewer, thePlayer) -> {
            FakePlayerSpawner.NORMAL.spawnPlayer(theViewer, thePlayer, thePlayer.getEntityId(), FakePlayerSpawner.FakePlayerPosition.ofPlayer(thePlayer), (meta) -> {
            });
         });
      }

   }

   public void resetMetadata(AttachmentViewer viewer) {
      DataWatcher metaTmp = EntityHandle.fromBukkit(this.entity).getDataWatcher();
      viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.entity.getEntityId(), metaTmp, true));
   }

   protected SeatedEntity.PassengerPose getCurrentHeadRotation(Matrix4x4 transform) {
      if (this.isEmpty()) {
         return new SeatedEntity.PassengerPose(transform, transform.getRotation());
      } else if (this.seat.firstPerson instanceof FirstPersonViewSpectator && this.seat.firstPerson.player != null) {
         return new SeatedEntity.PassengerPose(transform, ((FirstPersonViewSpectator)this.seat.firstPerson).getCurrentHeadRotation(transform));
      } else if (this.seat.useSmoothCoasters()) {
         return new SeatedEntity.PassengerPose(transform, this.getCurrentHeadRotationQuat(transform));
      } else {
         EntityHandle entityHandle = EntityHandle.fromBukkit(this.entity);
         return this.seat.isRotationLocked() ? new SeatedEntity.PassengerPose(transform, entityHandle.getPitch(), entityHandle.getHeadRotation()) : new SeatedEntity.PassengerPose(entityHandle.getYaw(), entityHandle.getPitch(), entityHandle.getHeadRotation());
      }
   }

   protected Quaternion getCurrentHeadRotationQuat(Matrix4x4 transform) {
      if (this.isEmpty()) {
         return transform.getRotation();
      } else if (this.seat.firstPerson instanceof FirstPersonViewSpectator && this.seat.firstPerson.player != null) {
         return ((FirstPersonViewSpectator)this.seat.firstPerson).getCurrentHeadRotation(transform);
      } else {
         EntityHandle entityHandle;
         if (this.seat.isRotationLocked()) {
            entityHandle = EntityHandle.fromBukkit(this.entity);
            SeatedEntity.PassengerPose pose = new SeatedEntity.PassengerPose(transform, entityHandle.getPitch(), entityHandle.getHeadRotation());
            pose = pose.limitHeadYaw(70.0F);
            Quaternion rotation = new Quaternion();
            rotation.rotateY((double)(-pose.headYaw));
            rotation.rotateX((double)pose.headPitch);
            return rotation;
         } else {
            entityHandle = EntityHandle.fromBukkit(this.entity);
            Quaternion rotation = new Quaternion();
            rotation.rotateY((double)(-entityHandle.getHeadRotation()));
            rotation.rotateX((double)entityHandle.getPitch());
            return rotation;
         }
      }
   }

   public int spawnVehicleMount(AttachmentViewer viewer) {
      if (this.parentMountId == -1) {
         if (this.seat.getConfiguredPosition().anchor == AttachmentAnchor.SEAT_PARENT && this.seat.getConfiguredPosition().isIdentity() && this.seat.getParent() != null && !this.seat.isRotationLocked()) {
            this.parentMountId = ((CartAttachment)this.seat.getParent()).getMountEntityId();
         }

         if (this.parentMountId == -1) {
            if (this.fakeMount == null) {
               this.fakeMount = this.createPassengerVehicle();
               this.fakeMount.updatePosition(this.seat.getTransform(), new Vector(0.0D, (double)this.orientation.getMountYaw(), 0.0D));
               this.fakeMount.syncPosition(true);
            }

            this.parentMountId = this.fakeMount.getEntityId();
         }
      }

      if (this.fakeMount != null) {
         this.fakeMount.spawn(viewer, this.seat.calcMotion());
         if (this.entity == viewer.getPlayer()) {
            viewer.send((PacketHandle)PacketPlayOutUpdateAttributesHandle.createZeroMaxHealth(this.fakeMount.getEntityId()));
         }
      }

      return this.parentMountId;
   }

   public void despawnVehicleMount(AttachmentViewer viewer) {
      if (this.fakeMount != null) {
         this.fakeMount.destroy(viewer);
         if (!this.fakeMount.hasViewers()) {
            this.fakeMount = null;
            this.parentMountId = -1;
         }
      }

   }

   protected void updateVehicleMountPosition(Matrix4x4 transform) {
      if (this.fakeMount != null) {
         this.fakeMount.updatePosition(transform, new Vector(0.0D, (double)this.orientation.getMountYaw(), 0.0D));
      }

   }

   protected void syncVehicleMountPosition(boolean absolute) {
      if (this.fakeMount != null) {
         this.fakeMount.syncPosition(absolute);
      }

   }

   public final void makeVisibleFirstPerson(AttachmentViewer viewer) {
      this.madeVisibleInFirstPerson = true;
      this.makeVisible(viewer);
   }

   public final void makeHiddenFirstPerson(AttachmentViewer viewer) {
      this.makeHidden(viewer);
      this.madeVisibleInFirstPerson = false;
   }

   public final boolean isMadeVisibleInFirstPerson() {
      return this.madeVisibleInFirstPerson;
   }

   public abstract Vector getThirdPersonCameraOffset();

   public abstract Vector getFirstPersonCameraOffset();

   public boolean isFirstPersonCameraFake() {
      return this.getFirstPersonCameraOffset().getY() != 1.0D;
   }

   public abstract void makeVisible(AttachmentViewer var1);

   public abstract void makeHidden(AttachmentViewer var1);

   public void updateMode(boolean silent) {
      FirstPersonViewMode new_firstPersonMode = this.seat.firstPerson.getMode();
      if (new_firstPersonMode == FirstPersonViewMode.DYNAMIC) {
         new_firstPersonMode = FirstPersonViewMode.THIRD_P;
      }

      if (new_firstPersonMode != this.seat.firstPerson.getLiveMode()) {
         AttachmentViewer viewer;
         if (!silent && this.seat.firstPerson.doesViewModeChangeRequireReset(new_firstPersonMode) && this.isPlayer() && this.seat.getAttachmentViewersSynced().contains(viewer = this.seat.getManager().asAttachmentViewer((Player)this.getEntity()))) {
            this.seat.makeHiddenImpl(viewer, true);
            this.seat.firstPerson.setLiveMode(new_firstPersonMode);
            this.seat.makeVisibleImpl(viewer, true);
         } else {
            this.seat.firstPerson.setLiveMode(new_firstPersonMode);
         }
      }
   }

   public abstract void updatePosition(Matrix4x4 var1);

   public abstract void syncPosition(boolean var1);

   public abstract void updateFocus(boolean var1);

   protected VirtualEntity createPassengerVehicle() {
      VirtualEntity mount = new VirtualEntity(this.seat.getManager());
      mount.setEntityType(EntityType.ARMOR_STAND);
      mount.setSyncMode(VirtualEntity.SyncMode.SEAT);
      mount.setUseMinecartInterpolation(this.seat.isMinecartInterpolation());
      mount.setByViewerPositionAdjustment((viewer, pos) -> {
         pos.setY(pos.getY() - viewer.getArmorStandButtOffset());
      });
      mount.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
      mount.getMetaData().set(EntityLivingHandle.DATA_HEALTH, 10.0F);
      mount.getMetaData().set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
      return mount;
   }

   public abstract boolean containsEntityId(int var1);

   public static enum DisplayMode {
      DEFAULT(SeatedEntityNormal::new),
      ELYTRA_SIT(SeatedEntityElytra::new),
      STANDING(SeatedEntityStanding::new),
      HEAD(SeatedEntityHead::new),
      NO_NAMETAG(SeatedEntityNormal::new),
      INVISIBLE(SeatedEntityInvisible::new);

      private final Function<CartAttachmentSeat, SeatedEntity> _constructor;

      private DisplayMode(Function<CartAttachmentSeat, SeatedEntity> constructor) {
         this._constructor = constructor;
      }

      public SeatedEntity create(CartAttachmentSeat seat) {
         SeatedEntity seated = (SeatedEntity)this._constructor.apply(seat);
         seated.setDisplayMode(this);
         return seated;
      }

      // $FF: synthetic method
      private static SeatedEntity.DisplayMode[] $values() {
         return new SeatedEntity.DisplayMode[]{DEFAULT, ELYTRA_SIT, STANDING, HEAD, NO_NAMETAG, INVISIBLE};
      }
   }

   public static class PassengerPose {
      public final float bodyYaw;
      public final float headPitch;
      public final float headYaw;

      public PassengerPose(Matrix4x4 bodyTransform, Quaternion headRotation) {
         this.bodyYaw = getMountYaw(bodyTransform);
         Vector ypr = headRotation.getYawPitchRoll();
         this.headPitch = (float)ypr.getX();
         this.headYaw = (float)ypr.getY();
      }

      public PassengerPose(Matrix4x4 bodyTransform, float headPitch, float headYaw) {
         this.bodyYaw = getMountYaw(bodyTransform);
         this.headPitch = headPitch;
         this.headYaw = headYaw;
      }

      public PassengerPose(float bodyYaw, float headPitch, float headYaw) {
         this.bodyYaw = bodyYaw;
         this.headPitch = headPitch;
         this.headYaw = headYaw;
      }

      public SeatedEntity.PassengerPose upsideDownFix_Pre_1_17() {
         return new SeatedEntity.PassengerPose(this.bodyYaw, -this.headPitch, -this.headYaw + 2.0F * this.bodyYaw);
      }

      public SeatedEntity.PassengerPose limitHeadYaw(float limit) {
         if (MathUtil.getAngleDifference(this.headYaw, this.bodyYaw) > limit) {
            return MathUtil.getAngleDifference(this.headYaw, this.bodyYaw + limit) < MathUtil.getAngleDifference(this.headYaw, this.bodyYaw - limit) ? new SeatedEntity.PassengerPose(this.bodyYaw, this.headPitch, this.bodyYaw + limit) : new SeatedEntity.PassengerPose(this.bodyYaw, this.headPitch, this.bodyYaw - limit);
         } else {
            return this;
         }
      }

      private static float getMountYaw(Matrix4x4 transform) {
         Vector f = transform.getRotation().forwardVector();
         return MathUtil.getLookAtYaw(-f.getZ(), f.getX());
      }
   }
}
