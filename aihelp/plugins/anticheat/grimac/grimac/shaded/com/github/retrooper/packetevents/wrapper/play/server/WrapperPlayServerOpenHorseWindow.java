package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerOpenHorseWindow extends PacketWrapper<WrapperPlayServerOpenHorseWindow> {
   private int windowId;
   private int slotCount;
   private int entityId;

   public WrapperPlayServerOpenHorseWindow(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerOpenHorseWindow(int windowId, int slotCount, int entityId) {
      super((PacketTypeCommon)PacketType.Play.Server.OPEN_HORSE_WINDOW);
      this.windowId = windowId;
      this.slotCount = slotCount;
      this.entityId = entityId;
   }

   public void read() {
      this.windowId = this.readContainerId();
      this.slotCount = this.readVarInt();
      this.entityId = this.readInt();
   }

   public void write() {
      this.writeContainerId(this.windowId);
      this.writeVarInt(this.slotCount);
      this.writeInt(this.entityId);
   }

   public void copy(WrapperPlayServerOpenHorseWindow other) {
      this.windowId = other.windowId;
      this.slotCount = other.slotCount;
      this.entityId = other.entityId;
   }

   public int getWindowId() {
      return this.windowId;
   }

   public void setWindowId(int windowId) {
      this.windowId = windowId;
   }

   public int getSlotCount() {
      return this.slotCount;
   }

   public void setSlotCount(int slotCount) {
      this.slotCount = slotCount;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }
}
