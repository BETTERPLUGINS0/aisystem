package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

public class EntityTypeTags {
   public static final EntityTypeTags.EntityTag CAN_FLOAT_WHILE_RIDDEN;

   static {
      CAN_FLOAT_WHILE_RIDDEN = new EntityTypeTags.EntityTag(new EntityType[]{EntityTypes.HORSE, EntityTypes.ZOMBIE_HORSE, EntityTypes.MULE, EntityTypes.DONKEY, EntityTypes.CAMEL, EntityTypes.CAMEL_HUSK});
   }

   public static record EntityTag(EntityType... tags) {
      public EntityTag(EntityType... tags) {
         this.tags = tags;
      }

      public boolean anyOf(EntityType tested) {
         EntityType[] var2 = this.tags;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EntityType type = var2[var4];
            if (tested.isInstanceOf(type)) {
               return true;
            }
         }

         return false;
      }

      public EntityType[] tags() {
         return this.tags;
      }
   }
}
