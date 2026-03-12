package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientCloseWindow extends PacketWrapper<WrapperPlayClientCloseWindow> {
   private int windowID;

   public WrapperPlayClientCloseWindow(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientCloseWindow(int windowID) {
      super((PacketTypeCommon)PacketType.Play.Client.CLOSE_WINDOW);
      this.windowID = windowID;
   }

   public void read() {
      this.windowID = this.readContainerId();
   }

   public void write() {
      this.writeContainerId(this.windowID);
   }

   public void copy(WrapperPlayClientCloseWindow wrapper) {
      this.windowID = wrapper.windowID;
   }

   public int getWindowId() {
      return this.windowID;
   }

   public void setWindowId(int windowID) {
      this.windowID = windowID;
   }
}
