package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.events.PacketReceiveEvent;
import com.bergerkiller.bukkit.common.events.PacketSendEvent;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.RelativeFlags;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.control.seat.spectator.FirstPersonSpectatedEntity;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerClientSynchronizer;
import com.bergerkiller.bukkit.tc.controller.player.network.PlayerPacketListener;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerRotationPacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayInFlyingHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutPositionHandle;
import com.bergerkiller.generated.net.minecraft.server.level.EntityPlayerHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import java.util.Collections;
import java.util.Objects;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class FirstPersonViewSpectator extends FirstPersonView {
   private static final double GHOST_Y_OFFSET = 64.0D;
   private static final float PITCH_ADJ_THRESHOLD = 15.0F;
   private int vehicleEntityId = -1;
   private FirstPersonSpectatedEntity _spectatedEntity = null;
   private VirtualEntity _playerMount = null;
   private final SpectatorInput _input = new SpectatorInput();
   private PlayerPacketListener<?> _spectatorPacketListener = null;

   public FirstPersonViewSpectator(CartAttachmentSeat seat, AttachmentViewer player) {
      super(seat, player);
   }

   public int prepareVehicleEntityId() {
      if (this.vehicleEntityId == -1) {
         this.vehicleEntityId = this.seat.seated.spawnVehicleMount(this.player);
      }

      return this.vehicleEntityId;
   }

   public boolean doesViewModeChangeRequireReset(FirstPersonViewMode newViewMode) {
      return newViewMode == FirstPersonViewMode.THIRD_P || this.getLiveMode() == FirstPersonViewMode.THIRD_P;
   }

   protected Matrix4x4 getEyeTransform() {
      Matrix4x4 base = super.getEyeTransform();
      this._input.applyTo(base);
      return base;
   }

   protected Quaternion getCurrentHeadRotation(Matrix4x4 transform) {
      transform = transform.clone();
      if (!this._eyePosition.isDefault()) {
         transform.multiply(this._eyePosition.transform);
      }

      this._input.applyTo(transform);
      return transform.getRotation();
   }

   public void makeVisible(AttachmentViewer viewer, boolean isReload) {
      setPlayerVisible(viewer, false);
      this.vehicleEntityId = -1;
      if (this.getLockMode() == FirstPersonViewLockMode.SPECTATOR_FREE) {
         this._input.start(viewer, this.seat.isRotationLocked() ? 70.0F : 360.0F);
      } else {
         this._input.startLocked();
      }

      Matrix4x4 eyeTransform = this.getEyeTransform();
      this._spectatedEntity = FirstPersonSpectatedEntity.create(this.seat, this, viewer);
      this._spectatedEntity.start(eyeTransform);
      if (this._spectatorPacketListener != null) {
         this._spectatorPacketListener.terminate();
         this._spectatorPacketListener = null;
      }

      if (viewer.supportRelativeRotationUpdate()) {
         this._spectatorPacketListener = viewer.createPacketListener(new FirstPersonViewSpectator.ViewControlPacketListenerRelativePitch(), PacketType.IN_POSITION_LOOK, PacketType.IN_POSITION, PacketType.IN_LOOK);
      } else {
         this._spectatorPacketListener = viewer.createPacketListener(new FirstPersonViewSpectator.ViewControlPacketListenerAbsoluteRotation(), PacketType.IN_POSITION_LOOK, PacketType.IN_POSITION, PacketType.IN_LOOK);
      }

      if (this._playerMount == null) {
         this._playerMount = new VirtualEntity(this.seat.getManager());
         this._playerMount.setEntityType(EntityType.ARMOR_STAND);
         this._playerMount.setSyncMode(VirtualEntity.SyncMode.SEAT);
         this._playerMount.setRelativeOffset(0.0D, 64.0D, 0.0D);
         this._playerMount.updatePosition(eyeTransform);
         this._playerMount.syncPosition(true);
         this._playerMount.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
         this._playerMount.getMetaData().set(EntityLivingHandle.DATA_HEALTH, 10.0F);
         this._playerMount.getMetaData().set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
         this._playerMount.spawn(viewer, new Vector());
         Vector pos = this._playerMount.getSyncPos();
         viewer.getClientSynchronizer().synchronize((teleportId) -> {
            return PacketPlayOutPositionHandle.createNew(pos.getX(), pos.getY(), pos.getZ(), this._playerMount.getSyncYaw(), this._playerMount.getSyncPitch(), 0.0D, 0.0D, 0.0D, RelativeFlags.ABSOLUTE_POSITION, teleportId);
         }, (p) -> {
            this._spectatorPacketListener.enable();
         });
         viewer.getVehicleMountController().mount(this._playerMount.getEntityId(), viewer.getEntityId());
      }

      if (this.getLiveMode() == FirstPersonViewMode.THIRD_P) {
         this.seat.seated.makeVisibleFirstPerson(viewer);
      }

   }

   public void makeHidden(AttachmentViewer viewer, boolean isReload) {
      if (this.getLiveMode() == FirstPersonViewMode.THIRD_P) {
         this.seat.seated.makeHiddenFirstPerson(viewer);
      }

      if (this._spectatorPacketListener != null) {
         PlayerClientSynchronizer var10000 = viewer.getClientSynchronizer();
         PlayerPacketListener var10001 = this._spectatorPacketListener;
         Objects.requireNonNull(var10001);
         var10000.synchronize(var10001::terminate);
         this._spectatorPacketListener = null;
      }

      if (this._playerMount != null) {
         VehicleMountController vmc = viewer.getVehicleMountController();
         vmc.unmount(this._playerMount.getEntityId(), viewer.getEntityId());
         this._playerMount.destroy(viewer);
         this._playerMount = null;
         if (this._spectatedEntity != null) {
            VirtualEntity entity = this._spectatedEntity.getCurrentEntity();
            Vector pos = entity.getSyncPos();
            EntityPlayerHandle playerHandle = EntityPlayerHandle.fromBukkit(viewer.getPlayer());
            playerHandle.setPositionRotation(pos.getX(), pos.getY(), pos.getZ(), entity.getSyncYaw(), entity.getSyncPitch());
            playerHandle.setFallDistance(0.0F);
            viewer.send((PacketHandle)PacketPlayOutPositionHandle.createAbsolute(pos.getX(), pos.getY(), pos.getZ(), entity.getSyncYaw(), entity.getSyncPitch()));
         }
      }

      if (this._spectatedEntity != null) {
         this._spectatedEntity.stop();
         this._spectatedEntity = null;
      }

      if (this.vehicleEntityId != -1) {
         this.seat.seated.despawnVehicleMount(viewer);
         this.vehicleEntityId = -1;
      }

      if (this.getLiveMode() != FirstPersonViewMode.THIRD_P) {
         setPlayerVisible(viewer, true);
      }

      this._input.stop(this.getEyeTransform());
   }

   public void onTick() {
      if (this._spectatedEntity != null) {
         Matrix4x4 baseTransform = this.getEyeTransform();
         this._playerMount.updatePosition(baseTransform);
         this._spectatedEntity.updatePosition(baseTransform);
      }

      this._input.update();
   }

   public void onMove(boolean absolute) {
      if (this._spectatedEntity != null) {
         this._playerMount.syncPosition(absolute);
         this._spectatedEntity.syncPosition(absolute);
      }

   }

   private class ViewControlPacketListenerRelativePitch extends FirstPersonViewSpectator.ViewControlPacketListener {
      private ViewControlPacketListenerRelativePitch() {
         super(null);
      }

      private void ackPitchAdjustDone(float pitchChange) {
         synchronized(this.stateLock) {
            if (this.isAdjustingPitch) {
               this.inFlightPitchCorrection -= pitchChange;
               if (this.lastYawPitch != null) {
                  this.lastYawPitch = new SpectatorInput.YawPitch(this.lastYawPitch.yaw, this.lastYawPitch.pitch + pitchChange);
               }

               SpectatorInput.YawPitch yawPitchDuringAdjustment = this.yawPitchDuringAdjustment;
               this.yawPitchDuringAdjustment = null;
               this.isAdjustingPitch = false;
               if (yawPitchDuringAdjustment != null) {
                  this.detectLookChanges(yawPitchDuringAdjustment);
               }

            }
         }
      }

      protected void makeAdjustment(SpectatorInput.YawPitch newYawPitch, float pitchAdjustment) {
         FirstPersonViewSpectator.this.player.getClientSynchronizer().synchronizeBundle(Collections.singletonList(ClientboundPlayerRotationPacketHandle.createRelative(0.0F, pitchAdjustment)), this::ackPitchAdjustStart, () -> {
            this.ackPitchAdjustDone(pitchAdjustment);
         });
      }

      // $FF: synthetic method
      ViewControlPacketListenerRelativePitch(Object x1) {
         this();
      }
   }

   private class ViewControlPacketListenerAbsoluteRotation extends FirstPersonViewSpectator.ViewControlPacketListener {
      private ViewControlPacketListenerAbsoluteRotation() {
         super(null);
      }

      private void ackAbsoluteRotationAdjust(float absoluteYaw, float pitchChange) {
         synchronized(this.stateLock) {
            if (this.isAdjustingPitch) {
               this.inFlightPitchCorrection -= pitchChange;
               if (this.lastYawPitch != null) {
                  this.lastYawPitch = new SpectatorInput.YawPitch(absoluteYaw, 0.0F);
               }

               SpectatorInput.YawPitch yawPitchDuringAdjustment = this.yawPitchDuringAdjustment;
               this.yawPitchDuringAdjustment = null;
               this.isAdjustingPitch = false;
               if (yawPitchDuringAdjustment != null) {
                  this.detectLookChanges(yawPitchDuringAdjustment);
               }

            }
         }
      }

      protected void makeAdjustment(SpectatorInput.YawPitch newYawPitch, float pitchAdjustment) {
         FirstPersonViewSpectator.this.player.getClientSynchronizer().synchronizeBundle(Collections.singletonList(ClientboundPlayerRotationPacketHandle.createAbsolute(newYawPitch.yaw, 0.0F)), this::ackPitchAdjustStart, () -> {
            this.ackAbsoluteRotationAdjust(newYawPitch.yaw, pitchAdjustment);
         });
      }

      // $FF: synthetic method
      ViewControlPacketListenerAbsoluteRotation(Object x1) {
         this();
      }
   }

   private abstract class ViewControlPacketListener implements PacketListener {
      protected final Object stateLock;
      protected SpectatorInput.YawPitch lastYawPitch;
      protected boolean isAdjustingPitch;
      protected SpectatorInput.YawPitch yawPitchDuringAdjustment;
      protected float inFlightPitchCorrection;
      protected int inFlightPitchCorrectionsCurrTick;

      private ViewControlPacketListener() {
         this.stateLock = new Object();
         this.lastYawPitch = null;
         this.isAdjustingPitch = false;
         this.yawPitchDuringAdjustment = null;
         this.inFlightPitchCorrection = 0.0F;
         this.inFlightPitchCorrectionsCurrTick = -1;
      }

      protected abstract void makeAdjustment(SpectatorInput.YawPitch var1, float var2);

      protected void detectLookChanges(SpectatorInput.YawPitch newYawPitch) {
         SpectatorInput.YawPitch lookChange = null;
         Float pitchAdjustment = null;
         synchronized(this.stateLock) {
            if (this.isAdjustingPitch) {
               this.yawPitchDuringAdjustment = newYawPitch;
               return;
            }

            if (this.lastYawPitch != null) {
               lookChange = SpectatorInput.YawPitch.subtract(newYawPitch, this.lastYawPitch);
            }

            this.lastYawPitch = newYawPitch;
            float pitchErrorFromZero = MathUtil.wrapAngle(-newYawPitch.pitch - this.inFlightPitchCorrection);
            if (Math.abs(pitchErrorFromZero) > 15.0F) {
               int currTick = CommonUtil.getServerTicks();
               if (currTick != this.inFlightPitchCorrectionsCurrTick) {
                  this.inFlightPitchCorrectionsCurrTick = currTick;
                  this.inFlightPitchCorrection += pitchErrorFromZero;
                  pitchAdjustment = pitchErrorFromZero;
               }
            }
         }

         if (lookChange != null) {
            FirstPersonViewSpectator.this._input.addInputRotation(lookChange);
         }

         if (pitchAdjustment != null) {
            this.makeAdjustment(newYawPitch, pitchAdjustment);
         }

      }

      protected void ackPitchAdjustStart() {
         synchronized(this.stateLock) {
            this.isAdjustingPitch = true;
            this.yawPitchDuringAdjustment = null;
         }
      }

      public void onPacketReceive(PacketReceiveEvent event) {
         PacketPlayInFlyingHandle p = PacketPlayInFlyingHandle.createHandle(event.getPacket().getHandle());
         if (event.getType() != PacketType.IN_LOOK) {
            p.setY(p.getY() - 64.0D);
         }

         if (event.getType() != PacketType.IN_POSITION) {
            this.detectLookChanges(new SpectatorInput.YawPitch(p.getYaw(), p.getPitch()));
         }

      }

      public void onPacketSend(PacketSendEvent event) {
      }

      // $FF: synthetic method
      ViewControlPacketListener(Object x1) {
         this();
      }
   }
}
