package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype;

import ac.grim.grimac.shaded.kyori.adventure.util.Index;

public enum DamageEffects {
   HURT("hurt"),
   THORNS("thorns"),
   DROWNING("drowning"),
   BURNING("burning"),
   POKING("poking"),
   FREEZING("freezing");

   public static final Index<String, DamageEffects> ID_INDEX = Index.create(DamageEffects.class, DamageEffects::getId);
   private final String id;

   private DamageEffects(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   // $FF: synthetic method
   private static DamageEffects[] $values() {
      return new DamageEffects[]{HURT, THORNS, DROWNING, BURNING, POKING, FREEZING};
   }
}
