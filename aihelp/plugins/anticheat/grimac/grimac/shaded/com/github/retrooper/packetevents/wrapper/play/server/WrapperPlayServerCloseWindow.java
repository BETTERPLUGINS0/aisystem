package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerCloseWindow extends PacketWrapper<WrapperPlayServerCloseWindow> {
   private int windowId;

   public WrapperPlayServerCloseWindow(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCloseWindow() {
      this(0);
   }

   public WrapperPlayServerCloseWindow(int id) {
      super((PacketTypeCommon)PacketType.Play.Server.CLOSE_WINDOW);
      this.windowId = id;
   }

   public void read() {
      this.windowId = this.readContainerId();
   }

   public void write() {
      this.writeContainerId(this.windowId);
   }

   public void copy(WrapperPlayServerCloseWindow wrapper) {
      this.windowId = wrapper.windowId;
   }

   public int getWindowId() {
      return this.windowId;
   }

   public void setWindowId(int windowId) {
      this.windowId = windowId;
   }
}
