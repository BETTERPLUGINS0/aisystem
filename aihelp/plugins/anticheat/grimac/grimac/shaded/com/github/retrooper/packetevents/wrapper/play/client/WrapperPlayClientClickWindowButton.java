package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientClickWindowButton extends PacketWrapper<WrapperPlayClientClickWindowButton> {
   private int windowID;
   private int buttonID;

   public WrapperPlayClientClickWindowButton(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientClickWindowButton(int windowID, int buttonID) {
      super((PacketTypeCommon)PacketType.Play.Client.CLICK_WINDOW_BUTTON);
      this.windowID = windowID;
      this.buttonID = buttonID;
   }

   public void read() {
      this.windowID = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readContainerId() : this.readVarInt();
      this.buttonID = this.readByte();
   }

   public void write() {
      this.writeContainerId(this.windowID);
      this.writeByte(this.buttonID);
   }

   public void copy(WrapperPlayClientClickWindowButton wrapper) {
      this.windowID = wrapper.windowID;
      this.buttonID = wrapper.buttonID;
   }

   public int getWindowId() {
      return this.windowID;
   }

   public void setWindowId(int windowID) {
      this.windowID = windowID;
   }

   public int getButtonId() {
      return this.buttonID;
   }

   public void setButtonId(int buttonID) {
      this.buttonID = buttonID;
   }
}
