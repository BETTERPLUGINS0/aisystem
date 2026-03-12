package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import java.util.UUID;

public class PacketEntityUnHittable extends PacketEntity {
   public PacketEntityUnHittable(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
      super(player, uuid, type, x, y, z);
   }

   public boolean canHit() {
      return false;
   }
}
