package com.bergerkiller.bukkit.tc.attachments.control.seat.spectator;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import org.bukkit.entity.EntityType;

public class FirstPersonEyePreview {
   public final CartAttachmentSeat seat;
   public final AttachmentViewer player;
   private int remaining = 0;
   private PitchSwappedEntity<VirtualEntity> entity;

   public FirstPersonEyePreview(CartAttachmentSeat seat, AttachmentViewer player) {
      this.seat = seat;
      this.player = player;
   }

   public boolean updateRemaining() {
      if (this.remaining == 1) {
         this.remaining = 0;
         this.handleStop();
         return false;
      } else if (this.remaining > 1) {
         --this.remaining;
         return true;
      } else {
         return true;
      }
   }

   public boolean start(int numTicks, Matrix4x4 eyeTransform) {
      if (this.remaining == 0 && numTicks > 0) {
         this.handleStart(eyeTransform);
         this.remaining = numTicks;
         return true;
      } else if (this.remaining > 0 && numTicks == 0) {
         this.remaining = 0;
         this.handleStop();
         return false;
      } else {
         this.remaining = numTicks;
         return false;
      }
   }

   public void stop() {
      if (this.remaining > 0) {
         this.remaining = 0;
         this.handleStop();
      }

   }

   private void handleStart(Matrix4x4 eyeTransform) {
      this.entity = PitchSwappedEntity.create(this.player, () -> {
         VirtualEntity entity = new VirtualEntity(this.seat.getManager());
         entity.setEntityType(EntityType.ARMOR_STAND);
         entity.setSyncMode(VirtualEntity.SyncMode.NORMAL);
         entity.setUseMinecartInterpolation(this.seat.isMinecartInterpolation());
         entity.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
         entity.getMetaData().set(EntityLivingHandle.DATA_HEALTH, 10.0F);
         entity.getMetaData().set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
         return entity;
      });
      this.entity.spawn(eyeTransform, this.seat.calcMotion());
      this.entity.spectate();
   }

   private void handleStop() {
      this.entity.destroy();
   }

   public void updatePosition(Matrix4x4 eyeTransform) {
      this.entity.updatePosition(eyeTransform);
   }

   public void syncPosition(boolean absolute) {
      this.entity.syncPosition(absolute);
   }
}
