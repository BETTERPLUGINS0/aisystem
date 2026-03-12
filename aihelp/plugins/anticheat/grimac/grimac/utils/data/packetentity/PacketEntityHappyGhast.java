package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import java.util.UUID;

public class PacketEntityHappyGhast extends PacketEntityTrackXRot {
   public PacketEntityHappyGhast(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, float xRot) {
      super(player, uuid, type, x, y, z, xRot);
      this.trackEntityEquipment = true;
      this.trackAttribute(ValuedAttribute.ranged(Attributes.FLYING_SPEED, 0.05D, 0.0D, 1024.0D));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.05D, 0.0D, 1024.0D));
   }

   public boolean isControllingPassenger() {
      return this.isWearingBodyArmor() && this.getFirstPassenger() instanceof PacketEntitySelf;
   }

   public boolean isWearingBodyArmor() {
      return this.hasItemInSlot(EquipmentSlot.BODY);
   }

   public PacketEntity getFirstPassenger() {
      return this.passengers.isEmpty() ? null : (PacketEntity)this.passengers.get(0);
   }
}
