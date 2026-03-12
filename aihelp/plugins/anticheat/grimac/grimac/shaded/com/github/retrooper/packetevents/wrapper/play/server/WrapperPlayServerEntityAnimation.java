package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityAnimation extends PacketWrapper<WrapperPlayServerEntityAnimation> {
   private int entityID;
   private WrapperPlayServerEntityAnimation.EntityAnimationType type;

   public WrapperPlayServerEntityAnimation(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityAnimation(int entityID, WrapperPlayServerEntityAnimation.EntityAnimationType type) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_ANIMATION);
      this.entityID = entityID;
      this.type = type;
   }

   public void read() {
      this.entityID = this.readVarInt();
      this.type = WrapperPlayServerEntityAnimation.EntityAnimationType.getById(this.readUnsignedByte());
   }

   public void write() {
      this.writeVarInt(this.entityID);
      this.writeByte(this.type.ordinal());
   }

   public void copy(WrapperPlayServerEntityAnimation wrapper) {
      this.entityID = wrapper.entityID;
      this.type = wrapper.type;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public WrapperPlayServerEntityAnimation.EntityAnimationType getType() {
      return this.type;
   }

   public void setType(WrapperPlayServerEntityAnimation.EntityAnimationType type) {
      this.type = type;
   }

   public static enum EntityAnimationType {
      SWING_MAIN_ARM,
      HURT,
      WAKE_UP,
      SWING_OFF_HAND,
      CRITICAL_HIT,
      MAGIC_CRITICAL_HIT;

      private static final WrapperPlayServerEntityAnimation.EntityAnimationType[] VALUES = values();

      public static WrapperPlayServerEntityAnimation.EntityAnimationType getById(int id) {
         return VALUES[id];
      }

      // $FF: synthetic method
      private static WrapperPlayServerEntityAnimation.EntityAnimationType[] $values() {
         return new WrapperPlayServerEntityAnimation.EntityAnimationType[]{SWING_MAIN_ARM, HURT, WAKE_UP, SWING_OFF_HAND, CRITICAL_HIT, MAGIC_CRITICAL_HIT};
      }
   }
}
