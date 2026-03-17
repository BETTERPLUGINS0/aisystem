package com.bergerkiller.bukkit.tc.attachments.control.seat.spectator;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.control.seat.FirstPersonViewSpectator;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import org.bukkit.entity.EntityType;

class FirstPersonSpectatedEntityInvisible extends FirstPersonSpectatedEntity {
   private PitchSwappedEntity<VirtualEntity> entity;

   public FirstPersonSpectatedEntityInvisible(CartAttachmentSeat seat, FirstPersonViewSpectator view, AttachmentViewer player) {
      super(seat, view, player);
   }

   public void start(Matrix4x4 eyeTransform) {
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

   public void stop() {
      this.entity.destroy();
   }

   public void updatePosition(Matrix4x4 eyeTransform) {
      this.entity.updatePosition(eyeTransform);
   }

   public void syncPosition(boolean absolute) {
      this.entity.syncPosition(absolute);
   }

   public VirtualEntity getCurrentEntity() {
      return this.entity.entity;
   }
}
