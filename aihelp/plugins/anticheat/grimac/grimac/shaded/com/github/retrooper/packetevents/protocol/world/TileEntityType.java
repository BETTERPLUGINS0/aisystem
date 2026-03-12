package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

/** @deprecated */
@Deprecated
public enum TileEntityType {
   MOB_SPAWNER,
   COMMAND_BLOCK,
   BEACON,
   SKULL,
   CONDUIT,
   BANNER,
   STRUCTURE_BLOCK,
   END_GATEWAY,
   SIGN,
   BED,
   JIGSAW,
   CAMPFIRE,
   BEEHIVE,
   UNKNOWN;

   public static final TileEntityType[] VALUES = values();

   public int getId() {
      return this.ordinal() + 1;
   }

   public static TileEntityType getById(int id) {
      return id >= 0 && id < VALUES.length ? VALUES[id - 1] : UNKNOWN;
   }

   // $FF: synthetic method
   private static TileEntityType[] $values() {
      return new TileEntityType[]{MOB_SPAWNER, COMMAND_BLOCK, BEACON, SKULL, CONDUIT, BANNER, STRUCTURE_BLOCK, END_GATEWAY, SIGN, BED, JIGSAW, CAMPFIRE, BEEHIVE, UNKNOWN};
   }
}
