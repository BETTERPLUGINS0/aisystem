package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import java.util.UUID;

public class PacketEntityRideable extends PacketEntity {
   public boolean hasSaddle = false;
   public int boostTimeMax = 0;
   public int currentBoostTime = 0;

   public PacketEntityRideable(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
      super(player, uuid, type, x, y, z);
      this.setAttribute(Attributes.STEP_HEIGHT, 1.0D);
      this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.10000000149011612D, 0.0D, 1024.0D));
   }
}
