package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotationHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle.PacketPlayOutEntityLookHandle;
import com.bergerkiller.generated.net.minecraft.server.level.EntityTrackerEntryStateHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SeatOrientation {
   private float _entityLastYaw = 0.0F;
   private float _entityLastPitch = 0.0F;
   private float _entityLastHeadYaw = 0.0F;
   private float _mountYaw = 0.0F;
   private int _entityRotationCtr = 0;

   public float getPassengerYaw() {
      return this._entityLastYaw;
   }

   public float getPassengerPitch() {
      return this._entityLastPitch;
   }

   public float getPassengerHeadYaw() {
      return this._entityLastHeadYaw;
   }

   public float getMountYaw() {
      return this._mountYaw;
   }

   public void sendLockedRotations(AttachmentViewer viewer, int entityId) {
      if (entityId != viewer.getEntityId()) {
         PacketPlayOutEntityHeadRotationHandle headPacket = PacketPlayOutEntityHeadRotationHandle.createNew(entityId, this.getPassengerHeadYaw());
         viewer.send((PacketHandle)headPacket);
         PacketPlayOutEntityLookHandle lookPacket = PacketPlayOutEntityLookHandle.createNew(entityId, this.getPassengerYaw(), this.getPassengerPitch(), false);
         viewer.send((PacketHandle)lookPacket);
      }

   }

   protected Vector computeElytraRelativeOffset(Vector pyr) {
      double yaw_sin = Math.sin(Math.toRadians(pyr.getY()));
      double yaw_cos = Math.cos(Math.toRadians(pyr.getY()));
      double pitch_sin = Math.sin(Math.toRadians(pyr.getX()));
      double pitch_cos = Math.cos(Math.toRadians(pyr.getX()));
      double l = 0.6D;
      double m = 0.1D;
      double rx = 0.6D * pitch_sin + (0.1D * pitch_cos - 0.1D);
      double ry = 0.6D * pitch_cos - 0.6D - 0.1D * pitch_sin;
      double off_x = -yaw_sin * rx;
      double off_z = yaw_cos * rx;
      return new Vector(-off_x, -ry, -off_z);
   }

   protected void synchronizeElytra(CartAttachmentSeat seat, Matrix4x4 transform, Vector pyr, SeatedEntityElytra seated) {
      Player viewerToIgnore = seated.isPlayer() && !seated.isMadeVisibleInFirstPerson() ? (Player)seated.getEntity() : null;
      this._mountYaw = (float)pyr.getY();
      float pitch = (float)(pyr.getX() - 90.0D);
      float headRot = seated.isDummyPlayer() ? this._mountYaw : EntityHandle.fromBukkit(seated.getEntity()).getHeadRotation();
      float HEAD_ROT_LIM = 30.0F;
      if (MathUtil.getAngleDifference(headRot, this._mountYaw) > 30.0F) {
         if (MathUtil.getAngleDifference(headRot, this._mountYaw + 30.0F) < MathUtil.getAngleDifference(headRot, this._mountYaw - 30.0F)) {
            headRot = this._mountYaw + 30.0F;
         } else {
            headRot = this._mountYaw - 30.0F;
         }
      }

      if (Util.isProtocolRotationGlitched(pitch, this._entityLastPitch)) {
         seated.flipFakes(seat);
         this._entityRotationCtr = 0;
      }

      int entityId = seated.getFakePlayerId();
      int flippedId = seated.getFlippedFakePlayerId();
      if (EntityTrackerEntryStateHandle.hasProtocolRotationChanged(headRot, this._entityLastHeadYaw)) {
         PacketPlayOutEntityHeadRotationHandle headPacket = PacketPlayOutEntityHeadRotationHandle.createNew(entityId, headRot);
         PacketPlayOutEntityHeadRotationHandle headPacketFlipped = PacketPlayOutEntityHeadRotationHandle.createNew(flippedId, headRot);
         this._entityLastHeadYaw = headPacket.getHeadYaw();
         Iterator var13 = seat.getAttachmentViewers().iterator();

         while(var13.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var13.next();
            if (viewer.getPlayer() != viewerToIgnore) {
               viewer.send((PacketHandle)headPacket);
               viewer.send((PacketHandle)headPacketFlipped);
            }
         }
      }

      if (this._entityRotationCtr != 0 && !EntityTrackerEntryStateHandle.hasProtocolRotationChanged(this._mountYaw, this._entityLastYaw) && !EntityTrackerEntryStateHandle.hasProtocolRotationChanged(pitch, this._entityLastPitch)) {
         --this._entityRotationCtr;
      } else {
         this._entityRotationCtr = 10;
         PacketPlayOutEntityLookHandle lookPacket = PacketPlayOutEntityLookHandle.createNew(entityId, this._mountYaw, pitch, false);
         this._entityLastYaw = lookPacket.getYaw();
         this._entityLastPitch = lookPacket.getPitch();
         Iterator var19 = seat.getAttachmentViewers().iterator();

         while(var19.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var19.next();
            if (viewer.getPlayer() != viewerToIgnore) {
               viewer.send((PacketHandle)lookPacket);
            }
         }

         float k = 180.0F;
         float f = 10.0F;
         float flippedPitch = this._entityLastPitch >= k ? k + f : k - f;
         PacketPlayOutEntityLookHandle flipLookPacket = PacketPlayOutEntityLookHandle.createNew(flippedId, this._entityLastYaw, flippedPitch, false);
         Iterator var16 = seat.getAttachmentViewers().iterator();

         while(var16.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var16.next();
            if (viewer.getPlayer() != viewerToIgnore) {
               viewer.send((PacketHandle)flipLookPacket);
            }
         }
      }

   }

   protected void synchronizeNormal(CartAttachmentSeat seat, Matrix4x4 transform, SeatedEntityNormal seated, int entityId) {
      Player viewerToIgnore = seated.isPlayer() && !seated.isMadeVisibleInFirstPerson() ? (Player)seated.getEntity() : null;
      SeatedEntity.PassengerPose pose = seated.getCurrentHeadRotation(transform);
      this._mountYaw = pose.bodyYaw;
      if (seat.isRotationLocked()) {
         pose = pose.limitHeadYaw(30.0F);
      }

      SeatedEntity.PassengerPose poseFixed = seated.isUpsideDown() ? pose.upsideDownFix_Pre_1_17() : pose;
      PacketPlayOutEntityHeadRotationHandle headPacket = null;
      PacketPlayOutEntityLookHandle lookPacket = null;
      if (EntityTrackerEntryStateHandle.hasProtocolRotationChanged(pose.headYaw, this._entityLastHeadYaw)) {
         headPacket = PacketPlayOutEntityHeadRotationHandle.createNew(entityId, pose.headYaw);
         this._entityLastHeadYaw = headPacket.getHeadYaw();
      }

      if (this._entityRotationCtr != 0 && !EntityTrackerEntryStateHandle.hasProtocolRotationChanged(pose.bodyYaw, this._entityLastYaw) && !EntityTrackerEntryStateHandle.hasProtocolRotationChanged(pose.headPitch, this._entityLastPitch)) {
         if (seat.isRotationLocked()) {
            --this._entityRotationCtr;
         }
      } else {
         this._entityRotationCtr = 10;
         lookPacket = PacketPlayOutEntityLookHandle.createNew(entityId, pose.bodyYaw, pose.headPitch, false);
         this._entityLastYaw = lookPacket.getYaw();
         this._entityLastPitch = lookPacket.getPitch();
      }

      PacketPlayOutEntityHeadRotationHandle headPacket_1_17_fix = null;
      PacketPlayOutEntityLookHandle lookPacket_1_17_fix = null;
      if (headPacket != null || lookPacket != null) {
         Iterator var12 = seat.getAttachmentViewers().iterator();

         while(true) {
            while(true) {
               AttachmentViewer viewer;
               do {
                  if (!var12.hasNext()) {
                     return;
                  }

                  viewer = (AttachmentViewer)var12.next();
               } while(viewer.getPlayer() == viewerToIgnore);

               if (seated.isUpsideDown() && viewer.evaluateGameVersion("<=", "1.17.1")) {
                  if (headPacket != null) {
                     if (headPacket_1_17_fix == null) {
                        headPacket_1_17_fix = PacketPlayOutEntityHeadRotationHandle.createNew(entityId, poseFixed.headYaw);
                     }

                     viewer.send((PacketHandle)headPacket_1_17_fix);
                  }

                  if (lookPacket != null) {
                     if (lookPacket_1_17_fix == null) {
                        lookPacket_1_17_fix = PacketPlayOutEntityLookHandle.createNew(entityId, poseFixed.bodyYaw, poseFixed.headPitch, false);
                     }

                     viewer.send((PacketHandle)lookPacket_1_17_fix);
                  }
               } else {
                  if (headPacket != null) {
                     viewer.send((PacketHandle)headPacket);
                  }

                  if (lookPacket != null) {
                     viewer.send((PacketHandle)lookPacket);
                  }
               }
            }
         }
      }
   }
}
