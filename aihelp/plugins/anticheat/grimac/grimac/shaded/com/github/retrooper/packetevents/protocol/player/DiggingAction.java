package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player;

public enum DiggingAction {
   START_DIGGING,
   CANCELLED_DIGGING,
   FINISHED_DIGGING,
   DROP_ITEM_STACK,
   DROP_ITEM,
   RELEASE_USE_ITEM,
   SWAP_ITEM_WITH_OFFHAND,
   STAB;

   private static final DiggingAction[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static DiggingAction getById(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static DiggingAction[] $values() {
      return new DiggingAction[]{START_DIGGING, CANCELLED_DIGGING, FINISHED_DIGGING, DROP_ITEM_STACK, DROP_ITEM, RELEASE_USE_ITEM, SWAP_ITEM_WITH_OFFHAND, STAB};
   }
}
