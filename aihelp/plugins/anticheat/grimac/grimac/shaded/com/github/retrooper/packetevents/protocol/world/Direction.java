package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;

public enum Direction {
   DOWN(-1, Axis.Y, new Vector3i(0, -1, 0)),
   UP(-1, Axis.Y, new Vector3i(0, 1, 0)),
   NORTH(0, Axis.Z, new Vector3i(0, 0, -1)),
   SOUTH(1, Axis.Z, new Vector3i(0, 0, 1)),
   WEST(2, Axis.X, new Vector3i(-1, 0, 0)),
   EAST(3, Axis.X, new Vector3i(1, 0, 0));

   private final int horizontalIndex;
   private final Axis axis;
   private final Vector3i vec3i;
   private static final Direction[] HORIZONTAL_VALUES = new Direction[]{NORTH, SOUTH, WEST, EAST};
   private static final Direction[] VALUES = values();

   private Direction(int horizontalIndex, Axis axis, Vector3i vec3i) {
      this.horizontalIndex = horizontalIndex;
      this.axis = axis;
      this.vec3i = vec3i;
   }

   public int getHorizontalIndex() {
      return this.horizontalIndex;
   }

   public static Direction getByHorizontalIndex(int index) {
      return HORIZONTAL_VALUES[index % HORIZONTAL_VALUES.length];
   }

   public static Direction getByIndex(int enumOrdinal) {
      return VALUES[enumOrdinal];
   }

   public Vector3i getVector() {
      return this.vec3i;
   }

   public Axis getAxis() {
      return this.axis;
   }

   // $FF: synthetic method
   private static Direction[] $values() {
      return new Direction[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   }
}
