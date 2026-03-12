package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperPlayServerFacePlayer extends PacketWrapper<WrapperPlayServerFacePlayer> {
   private WrapperPlayServerFacePlayer.EntitySection aimUnit;
   private Vector3d targetPosition;
   @Nullable
   private WrapperPlayServerFacePlayer.TargetEntity targetEntity;

   public WrapperPlayServerFacePlayer(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerFacePlayer(WrapperPlayServerFacePlayer.EntitySection aimUnit, Vector3d targetPosition, @Nullable WrapperPlayServerFacePlayer.TargetEntity targetEntity) {
      super((PacketTypeCommon)PacketType.Play.Server.FACE_PLAYER);
      this.aimUnit = aimUnit;
      this.targetPosition = targetPosition;
      this.targetEntity = targetEntity;
   }

   public void read() {
      this.aimUnit = WrapperPlayServerFacePlayer.EntitySection.getById(this.readVarInt());
      this.targetPosition = new Vector3d(this.readDouble(), this.readDouble(), this.readDouble());
      this.targetEntity = (WrapperPlayServerFacePlayer.TargetEntity)this.readOptional((reader) -> {
         return new WrapperPlayServerFacePlayer.TargetEntity(reader.readVarInt(), WrapperPlayServerFacePlayer.EntitySection.getById(reader.readVarInt()));
      });
   }

   public void write() {
      this.writeVarInt(this.aimUnit.getId());
      this.writeDouble(this.targetPosition.getX());
      this.writeDouble(this.targetPosition.getY());
      this.writeDouble(this.targetPosition.getZ());
      this.writeOptional(this.targetEntity, (wrapper, innerEntity) -> {
         wrapper.writeVarInt(innerEntity.getEntityId());
         wrapper.writeVarInt(innerEntity.getEntitySection().getId());
      });
   }

   public void copy(WrapperPlayServerFacePlayer wrapper) {
      this.aimUnit = wrapper.aimUnit;
      this.targetPosition = wrapper.targetPosition;
      this.targetEntity = wrapper.targetEntity;
   }

   public WrapperPlayServerFacePlayer.EntitySection getAimUnit() {
      return this.aimUnit;
   }

   public void setAimUnit(WrapperPlayServerFacePlayer.EntitySection aimUnit) {
      this.aimUnit = aimUnit;
   }

   public Vector3d getTargetPosition() {
      return this.targetPosition;
   }

   public void setTargetPosition(Vector3d targetPosition) {
      this.targetPosition = targetPosition;
   }

   @Nullable
   public WrapperPlayServerFacePlayer.TargetEntity getTargetEntity() {
      return this.targetEntity;
   }

   public void setTargetEntity(@Nullable WrapperPlayServerFacePlayer.TargetEntity targetEntity) {
      this.targetEntity = targetEntity;
   }

   public static enum EntitySection {
      EYES,
      FEET;

      public int getId() {
         return this.ordinal();
      }

      public static WrapperPlayServerFacePlayer.EntitySection getById(int id) {
         return values()[id];
      }

      // $FF: synthetic method
      private static WrapperPlayServerFacePlayer.EntitySection[] $values() {
         return new WrapperPlayServerFacePlayer.EntitySection[]{EYES, FEET};
      }
   }

   public static class TargetEntity {
      private int entityId;
      private WrapperPlayServerFacePlayer.EntitySection entitySection;

      public TargetEntity(int entityId, WrapperPlayServerFacePlayer.EntitySection entitySection) {
         this.entityId = entityId;
         this.entitySection = entitySection;
      }

      public int getEntityId() {
         return this.entityId;
      }

      public void setEntityId(int entityId) {
         this.entityId = entityId;
      }

      public WrapperPlayServerFacePlayer.EntitySection getEntitySection() {
         return this.entitySection;
      }

      public void setEntitySection(WrapperPlayServerFacePlayer.EntitySection entitySection) {
         this.entitySection = entitySection;
      }
   }
}
