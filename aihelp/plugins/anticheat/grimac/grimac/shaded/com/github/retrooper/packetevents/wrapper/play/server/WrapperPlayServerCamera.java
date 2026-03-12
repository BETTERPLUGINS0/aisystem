package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerCamera extends PacketWrapper<WrapperPlayServerCamera> {
   private int cameraId;

   public WrapperPlayServerCamera(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCamera(int cameraId) {
      super((PacketTypeCommon)PacketType.Play.Server.CAMERA);
      this.cameraId = cameraId;
   }

   public void read() {
      this.cameraId = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.cameraId);
   }

   public void copy(WrapperPlayServerCamera wrapper) {
      this.cameraId = wrapper.cameraId;
   }

   public int getCameraId() {
      return this.cameraId;
   }

   public void setCameraId(int cameraId) {
      this.cameraId = cameraId;
   }
}
