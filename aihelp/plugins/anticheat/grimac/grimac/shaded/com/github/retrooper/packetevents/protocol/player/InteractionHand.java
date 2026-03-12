package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player;

public enum InteractionHand {
   MAIN_HAND,
   OFF_HAND;

   private static final InteractionHand[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static InteractionHand getById(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static InteractionHand[] $values() {
      return new InteractionHand[]{MAIN_HAND, OFF_HAND};
   }
}
