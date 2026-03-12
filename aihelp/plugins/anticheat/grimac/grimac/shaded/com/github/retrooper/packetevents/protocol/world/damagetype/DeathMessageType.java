package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype;

import ac.grim.grimac.shaded.kyori.adventure.util.Index;

public enum DeathMessageType {
   DEFAULT("default"),
   FALL_VARIANTS("fall_variants"),
   INTENTIONAL_GAME_DESIGN("intentional_game_design");

   public static final Index<String, DeathMessageType> ID_INDEX = Index.create(DeathMessageType.class, DeathMessageType::getId);
   private final String id;

   private DeathMessageType(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   // $FF: synthetic method
   private static DeathMessageType[] $values() {
      return new DeathMessageType[]{DEFAULT, FALL_VARIANTS, INTENTIONAL_GAME_DESIGN};
   }
}
