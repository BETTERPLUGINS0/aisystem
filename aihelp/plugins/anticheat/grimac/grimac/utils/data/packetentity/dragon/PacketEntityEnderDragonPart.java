package ac.grim.grimac.utils.data.packetentity.dragon;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import java.util.UUID;

public final class PacketEntityEnderDragonPart extends PacketEntity {
   public final DragonPart part;
   public final float width;
   public final float height;

   public PacketEntityEnderDragonPart(GrimPlayer player, DragonPart part, double x, double y, double z, float width, float height) {
      super(player, (UUID)null, EntityTypes.ENDER_DRAGON, x, y, z);
      this.part = part;
      this.width = width;
      this.height = height;
   }
}
