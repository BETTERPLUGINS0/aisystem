package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientWindowConfirmation extends PacketWrapper<WrapperPlayClientWindowConfirmation> {
   private int windowId;
   private short actionId;
   private boolean accepted;

   public WrapperPlayClientWindowConfirmation(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientWindowConfirmation(int windowId, short actionId, boolean accepted) {
      super((PacketTypeCommon)PacketType.Play.Client.WINDOW_CONFIRMATION);
      this.windowId = windowId;
      this.actionId = actionId;
      this.accepted = accepted;
   }

   public void read() {
      this.windowId = this.readUnsignedByte();
      this.actionId = this.readShort();
      this.accepted = this.readBoolean();
   }

   public void write() {
      this.writeByte(this.windowId);
      this.writeShort(this.actionId);
      this.writeBoolean(this.accepted);
   }

   public void copy(WrapperPlayClientWindowConfirmation wrapper) {
      this.windowId = wrapper.windowId;
      this.actionId = wrapper.actionId;
      this.accepted = wrapper.accepted;
   }

   public int getWindowId() {
      return this.windowId;
   }

   public void setWindowId(int windowID) {
      this.windowId = windowID;
   }

   public short getActionId() {
      return this.actionId;
   }

   public void setActionId(short actionId) {
      this.actionId = actionId;
   }

   public boolean isAccepted() {
      return this.accepted;
   }

   public void setAccepted(boolean accepted) {
      this.accepted = accepted;
   }
}
