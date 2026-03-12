package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

public enum BlockFace {
   DOWN(0, -1, 0),
   UP(0, 1, 0),
   NORTH(0, 0, -1),
   SOUTH(0, 0, 1),
   WEST(-1, 0, 0),
   EAST(1, 0, 0),
   OTHER((short)255, -1, -1, -1);

   private static final BlockFace[] VALUES = values();
   private static final BlockFace[] CARTESIAN_VALUES = new BlockFace[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   final short faceValue;
   final int modX;
   final int modY;
   final int modZ;

   private BlockFace(short faceValue, int modX, int modY, int modZ) {
      this.faceValue = faceValue;
      this.modX = modX;
      this.modY = modY;
      this.modZ = modZ;
   }

   private BlockFace(int modX, int modY, int modZ) {
      this.faceValue = (short)this.ordinal();
      this.modX = modX;
      this.modY = modY;
      this.modZ = modZ;
   }

   public static BlockFace getLegacyBlockFaceByValue(int face) {
      return face == 255 ? OTHER : CARTESIAN_VALUES[face % CARTESIAN_VALUES.length];
   }

   public static BlockFace getBlockFaceByValue(int face) {
      return CARTESIAN_VALUES[face % CARTESIAN_VALUES.length];
   }

   public int getModX() {
      return this.modX;
   }

   public int getModY() {
      return this.modY;
   }

   public int getModZ() {
      return this.modZ;
   }

   public BlockFace getOppositeFace() {
      switch(this.ordinal()) {
      case 0:
         return UP;
      case 1:
         return DOWN;
      case 2:
         return SOUTH;
      case 3:
         return NORTH;
      case 4:
         return EAST;
      case 5:
         return WEST;
      default:
         return OTHER;
      }
   }

   public BlockFace getCCW() {
      switch(this.ordinal()) {
      case 2:
         return WEST;
      case 3:
         return EAST;
      case 4:
         return SOUTH;
      case 5:
         return NORTH;
      default:
         return OTHER;
      }
   }

   public BlockFace getCW() {
      switch(this.ordinal()) {
      case 2:
         return EAST;
      case 3:
         return WEST;
      case 4:
         return NORTH;
      case 5:
         return SOUTH;
      default:
         return OTHER;
      }
   }

   public int getHorizontalId() {
      switch(this.ordinal()) {
      case 0:
      case 1:
         return -1;
      case 2:
         return 2;
      case 3:
         return 0;
      case 4:
         return 1;
      case 5:
         return 3;
      default:
         throw new IllegalArgumentException("Invalid block face input for getHorizontalId");
      }
   }

   public short getFaceValue() {
      return this.faceValue;
   }

   // $FF: synthetic method
   private static BlockFace[] $values() {
      return new BlockFace[]{DOWN, UP, NORTH, SOUTH, WEST, EAST, OTHER};
   }
}
