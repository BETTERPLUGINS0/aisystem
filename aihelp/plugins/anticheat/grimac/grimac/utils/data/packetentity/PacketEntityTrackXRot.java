package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import java.util.UUID;

public class PacketEntityTrackXRot extends PacketEntity {
   public float packetYaw;
   public float interpYaw;
   public int steps = 0;

   public PacketEntityTrackXRot(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, float xRot) {
      super(player, uuid, type, x, y, z);
      this.packetYaw = xRot;
      this.interpYaw = xRot;
   }

   public void onMovement(boolean highBound) {
      super.onMovement(highBound);
      if (this.steps > 0) {
         this.interpYaw += (this.packetYaw - this.interpYaw) / (float)(this.steps--);
      }

   }
}
