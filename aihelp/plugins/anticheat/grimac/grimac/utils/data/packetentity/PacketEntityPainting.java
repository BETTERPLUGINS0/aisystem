package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import java.util.UUID;
import lombok.Generated;

public class PacketEntityPainting extends PacketEntity {
   private final Direction direction;

   public PacketEntityPainting(GrimPlayer player, UUID uuid, double x, double y, double z, Direction direction) {
      super(player, uuid, EntityTypes.PAINTING, x, y, z);
      this.direction = direction;
   }

   @Generated
   public Direction getDirection() {
      return this.direction;
   }
}
