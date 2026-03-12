package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pose;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public enum EntityPose {
   STANDING,
   FALL_FLYING,
   SLEEPING,
   SWIMMING,
   SPIN_ATTACK,
   CROUCHING,
   LONG_JUMPING,
   DYING,
   CROAKING,
   USING_TONGUE,
   SITTING,
   ROARING,
   SNIFFING,
   EMERGING,
   DIGGING,
   SLIDING,
   SHOOTING,
   INHALING;

   public int getId(ClientVersion version) {
      if (this == DYING && version.isOlderThan(ClientVersion.V_1_17)) {
         return 6;
      } else {
         return this.ordinal() >= 11 && version.isOlderThan(ClientVersion.V_1_19_3) ? this.ordinal() - 1 : this.ordinal();
      }
   }

   public static EntityPose getById(ClientVersion version, int id) {
      if (id == 6 && version.isOlderThan(ClientVersion.V_1_17)) {
         return DYING;
      } else {
         if (id >= 10 && version.isOlderThan(ClientVersion.V_1_19_3)) {
            ++id;
         }

         return values()[id];
      }
   }

   // $FF: synthetic method
   private static EntityPose[] $values() {
      return new EntityPose[]{STANDING, FALL_FLYING, SLEEPING, SWIMMING, SPIN_ATTACK, CROUCHING, LONG_JUMPING, DYING, CROAKING, USING_TONGUE, SITTING, ROARING, SNIFFING, EMERGING, DIGGING, SLIDING, SHOOTING, INHALING};
   }
}
