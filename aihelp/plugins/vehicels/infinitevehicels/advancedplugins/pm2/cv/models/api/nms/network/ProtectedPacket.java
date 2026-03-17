package advancedplugins.pm2.cv.models.api.nms.network;

public record ProtectedPacket(Object packet) {
   public ProtectedPacket(Object packet) {
      this.packet = var1;
   }

   public Object packet() {
      return this.packet;
   }
}
