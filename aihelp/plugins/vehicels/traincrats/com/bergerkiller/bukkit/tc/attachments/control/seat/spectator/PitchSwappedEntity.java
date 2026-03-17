package com.bergerkiller.bukkit.tc.attachments.control.seat.spectator;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.seat.FirstPersonView;
import com.bergerkiller.generated.net.minecraft.server.level.EntityTrackerEntryStateHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.util.Vector;

class PitchSwappedEntity<E extends VirtualEntity> {
   private static final float MIN_PITCH = EntityTrackerEntryStateHandle.getRotationFromProtocol(-128);
   private static final float MAX_PITCH = EntityTrackerEntryStateHandle.getRotationFromProtocol(127);
   private final AttachmentViewer viewer;
   private final VehicleMountController vmc;
   private Consumer<E> beforeSwap = (e) -> {
   };
   private Runnable afterSwap = () -> {
   };
   public E entity;
   public E entityAlt;
   public E entityAltFlip;
   private boolean spectating = false;

   private PitchSwappedEntity(AttachmentViewer viewer, E entity, E entityAlt, E entityAltFlip) {
      this.viewer = viewer;
      this.vmc = viewer.getVehicleMountController();
      this.entity = entity;
      this.entityAlt = entityAlt;
      this.entityAltFlip = entityAltFlip;
   }

   public int getEntityId() {
      return this.entity.getEntityId();
   }

   public void beforeSwap(Consumer<E> action) {
      this.beforeSwap = action;
   }

   public void afterSwap(Runnable action) {
      this.afterSwap = action;
   }

   public void spawn(Matrix4x4 eyeTransform, Vector motion) {
      FirstPersonView.HeadRotation headRot = FirstPersonView.HeadRotation.compute(eyeTransform);
      this.entity.updatePosition(eyeTransform, headRot.pyr);
      this.entity.syncPosition(true);
      this.entityAlt.updatePosition(eyeTransform, new Vector((double)computeAltPitch(headRot.pitch, MAX_PITCH), (double)headRot.yaw, 0.0D));
      this.entityAlt.syncPosition(true);
      this.entityAltFlip.updatePosition(eyeTransform, headRot.flipVertical().pyr);
      this.entityAltFlip.syncPosition(true);
      this.entity.spawn(this.viewer, motion);
      this.entity.forceSyncRotation();
      this.entityAlt.spawn(this.viewer, motion);
      this.entityAlt.forceSyncRotation();
      this.entityAltFlip.spawn(this.viewer, motion);
      this.entityAltFlip.forceSyncRotation();
   }

   public void destroy() {
      if (this.spectating) {
         this.spectating = false;
         this.vmc.stopSpectating(this.entity.getEntityId());
      }

      this.entity.destroy(this.viewer);
      this.entityAlt.destroy(this.viewer);
      this.entityAltFlip.destroy(this.viewer);
   }

   public void spectate() {
      this.vmc.startSpectating(this.entity.getEntityId());
      this.spectating = true;
   }

   public void spectateFrom(int previousEntityId) {
      this.vmc.swapSpectating(previousEntityId, this.entity.getEntityId());
      this.spectating = true;
   }

   public void swapVisibility(E swapped) {
      this.entity.getMetaData().setFlag(EntityHandle.DATA_FLAGS, 32, true);
      this.entity.syncMetadata();
      swapped.getMetaData().setFlag(EntityHandle.DATA_FLAGS, 32, false);
      swapped.syncMetadata();
   }

   public void updatePosition(Matrix4x4 eyeTransform) {
      Vector position = eyeTransform.toVector();
      FirstPersonView.HeadRotation headRot = FirstPersonView.HeadRotation.compute(eyeTransform);
      FirstPersonView.HeadRotation headRotFlipped = headRot.flipVertical();
      VirtualEntity tmp;
      if (Util.isProtocolRotationGlitched(this.entity.getSyncPitch(), headRot.pitch)) {
         if (this.spectating) {
            this.vmc.swapSpectating(this.entity.getEntityId(), this.entityAlt.getEntityId());
         }

         this.beforeSwap.accept(this.entityAlt);
         tmp = this.entity;
         this.entity = this.entityAlt;
         this.entityAlt = tmp;
         this.entity.updatePosition(position, headRot.pyr);
         this.entity.syncPosition(true);
         this.entity.syncMetadata();
         this.entityAlt.syncMetadata();
         this.afterSwap.run();
      } else if (this.isCameraFlipped(headRot, headRotFlipped)) {
         if (this.spectating) {
            this.vmc.swapSpectating(this.entity.getEntityId(), this.entityAltFlip.getEntityId());
         }

         this.beforeSwap.accept(this.entityAltFlip);
         tmp = this.entity;
         this.entity = this.entityAltFlip;
         this.entityAltFlip = tmp;
         this.entity.updatePosition(position, headRot.pyr);
         this.entity.syncPosition(true);
         this.entity.syncMetadata();
         this.entityAltFlip.syncMetadata();
         this.afterSwap.run();
      } else {
         this.entity.updatePosition(position, headRot.pyr);
      }

      boolean requiresRespawning = Util.isProtocolRotationGlitched(headRotFlipped.pitch, this.entityAltFlip.getLivePitch());
      this.entityAltFlip.updatePosition(position, headRotFlipped.pyr);
      if (requiresRespawning) {
         this.entityAltFlip.respawnForAll(new Vector());
         this.entityAltFlip.forceSyncRotation();
      }

      float newAltPitch = computeAltPitch(headRot.pitch, this.entityAlt.getLivePitch());
      boolean requiresRespawning = Util.isProtocolRotationGlitched(newAltPitch, this.entityAlt.getLivePitch());
      this.entityAlt.updatePosition(position, new Vector(newAltPitch, headRot.yaw, headRot.roll));
      if (requiresRespawning) {
         this.entityAlt.respawnForAll(new Vector());
         this.entityAlt.forceSyncRotation();
      }

   }

   private boolean isCameraFlipped(FirstPersonView.HeadRotation newRot, FirstPersonView.HeadRotation newRotFlipped) {
      return MathUtil.getAngleDifference(this.entity.getLiveYaw(), newRot.yaw) > 90.0F && MathUtil.getAngleDifference(this.entity.getLivePitch(), newRotFlipped.pitch) < MathUtil.getAngleDifference(this.entity.getLivePitch(), this.entity.getLiveYaw());
   }

   public void syncPosition(boolean absolute) {
      this.entity.syncPosition(absolute);
      this.entityAlt.syncPosition(absolute);
      this.entityAltFlip.syncPosition(absolute);
   }

   public void onBeforeSwap() {
   }

   public static <E extends VirtualEntity> PitchSwappedEntity<E> create(AttachmentViewer viewer, E entity, E entityAlt, E entityAltFlip) {
      return new PitchSwappedEntity(viewer, entity, entityAlt, entityAltFlip);
   }

   public static <E extends VirtualEntity> PitchSwappedEntity<E> create(AttachmentViewer viewer, Supplier<E> entityFactory) {
      return new PitchSwappedEntity(viewer, (VirtualEntity)entityFactory.get(), (VirtualEntity)entityFactory.get(), (VirtualEntity)entityFactory.get());
   }

   static float computeAltPitch(float currPitch, float currAltPitch) {
      int protRot = EntityTrackerEntryStateHandle.getProtocolRotation(currPitch);
      if (protRot == -128) {
         return MAX_PITCH;
      } else if (protRot == 127) {
         return MIN_PITCH;
      } else {
         currPitch = MathUtil.wrapAngle(currPitch);
         if ((double)currPitch > 90.0D) {
            return MIN_PITCH;
         } else {
            return (double)currPitch < -90.0D ? MAX_PITCH : currAltPitch;
         }
      }
   }
}
