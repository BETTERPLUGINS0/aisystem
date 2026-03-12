package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player;

public enum Combat {
   ENTER_COMBAT,
   END_COMBAT,
   ENTITY_DEAD;

   public static final Combat[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static Combat getById(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static Combat[] $values() {
      return new Combat[]{ENTER_COMBAT, END_COMBAT, ENTITY_DEAD};
   }
}
