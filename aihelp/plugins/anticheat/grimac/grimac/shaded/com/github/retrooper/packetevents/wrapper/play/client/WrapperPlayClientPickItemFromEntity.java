package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPickItemFromEntity extends PacketWrapper<WrapperPlayClientPickItemFromEntity> {
   private int entityId;
   private boolean includeData;

   public WrapperPlayClientPickItemFromEntity(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPickItemFromEntity(int entityId, boolean includeData) {
      super((PacketTypeCommon)PacketType.Play.Client.PICK_ITEM_FROM_ENTITY);
      this.entityId = entityId;
      this.includeData = includeData;
   }

   public void read() {
      this.entityId = this.readVarInt();
      this.includeData = this.readBoolean();
   }

   public void write() {
      this.writeVarInt(this.entityId);
      this.writeBoolean(this.includeData);
   }

   public void copy(WrapperPlayClientPickItemFromEntity wrapper) {
      this.entityId = wrapper.entityId;
      this.includeData = wrapper.includeData;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public boolean isIncludeData() {
      return this.includeData;
   }

   public void setIncludeData(boolean includeData) {
      this.includeData = includeData;
   }
}
