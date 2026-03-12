package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerRemoveEntityEffect extends PacketWrapper<WrapperPlayServerRemoveEntityEffect> {
   private int entityId;
   private PotionType potionType;

   public WrapperPlayServerRemoveEntityEffect(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerRemoveEntityEffect(int entityId, PotionType potionType) {
      super((PacketTypeCommon)PacketType.Play.Server.REMOVE_ENTITY_EFFECT);
      this.entityId = entityId;
      this.potionType = potionType;
   }

   public void read() {
      this.entityId = this.readVarInt();
      int effectId;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
         effectId = this.readVarInt();
      } else {
         effectId = this.readByte();
      }

      this.potionType = PotionTypes.getById(effectId, this.serverVersion);
   }

   public void write() {
      this.writeVarInt(this.entityId);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
         this.writeVarInt(this.potionType.getId(this.serverVersion.toClientVersion()));
      } else {
         this.writeByte(this.potionType.getId(this.serverVersion.toClientVersion()));
      }

   }

   public void copy(WrapperPlayServerRemoveEntityEffect wrapper) {
      this.entityId = wrapper.entityId;
      this.potionType = wrapper.potionType;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public PotionType getPotionType() {
      return this.potionType;
   }

   public void setPotionType(PotionType potionType) {
      this.potionType = potionType;
   }
}
