package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

public abstract class TypedPacketEntity {
   public final EntityType type;
   public final boolean isLivingEntity;
   public final boolean isMinecart;
   public final boolean isHorse;
   public final boolean isAgeable;
   public final boolean isAnimal;
   public final boolean isBoat;
   public final boolean isHappyGhast;

   public TypedPacketEntity(EntityType type) {
      this.type = type;
      this.isLivingEntity = EntityTypes.isTypeInstanceOf(type, EntityTypes.LIVINGENTITY);
      this.isMinecart = EntityTypes.isTypeInstanceOf(type, EntityTypes.MINECART_ABSTRACT);
      this.isHorse = EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_HORSE);
      this.isAgeable = EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_AGEABLE) && !EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_PARROT) && type != EntityTypes.FROG || EntityTypes.isTypeInstanceOf(type, EntityTypes.ZOMBIE) || EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_PIGLIN) || type == EntityTypes.ZOGLIN;
      this.isAnimal = EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_ANIMAL);
      this.isBoat = EntityTypes.isTypeInstanceOf(type, EntityTypes.BOAT);
      this.isHappyGhast = EntityTypes.HAPPY_GHAST.equals(type);
   }

   public boolean isPushable() {
      if (this.type != EntityTypes.ARMOR_STAND && this.type != EntityTypes.BAT && this.type != EntityTypes.PARROT) {
         return this.isLivingEntity || this.isBoat || this.isMinecart;
      } else {
         return false;
      }
   }
}
