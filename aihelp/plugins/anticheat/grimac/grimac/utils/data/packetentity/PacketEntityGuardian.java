package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import java.util.UUID;

public class PacketEntityGuardian extends PacketEntity {
   public boolean isElder;

   public PacketEntityGuardian(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, boolean isElder) {
      super(player, uuid, type, x, y, z);
      this.isElder = isElder;
   }
}
