package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public enum AxisSelect {
   EAST {
      public SimpleCollisionBox modify(SimpleCollisionBox box) {
         box.maxX = 1.0D;
         return box;
      }
   },
   WEST {
      public SimpleCollisionBox modify(SimpleCollisionBox box) {
         box.minX = 0.0D;
         return box;
      }
   },
   NORTH {
      public SimpleCollisionBox modify(SimpleCollisionBox box) {
         box.minZ = 0.0D;
         return box;
      }
   },
   SOUTH {
      public SimpleCollisionBox modify(SimpleCollisionBox box) {
         box.maxZ = 1.0D;
         return box;
      }
   },
   UP {
      public SimpleCollisionBox modify(SimpleCollisionBox box) {
         box.minY = 0.0D;
         return box;
      }
   },
   DOWN {
      public SimpleCollisionBox modify(SimpleCollisionBox box) {
         box.maxY = 1.0D;
         return box;
      }
   };

   public abstract SimpleCollisionBox modify(SimpleCollisionBox var1);

   @Contract(
      pure = true
   )
   public static AxisSelect byFace(@NotNull BlockFace face) {
      AxisSelect var10000;
      switch(face) {
      case EAST:
         var10000 = EAST;
         break;
      case WEST:
         var10000 = WEST;
         break;
      case NORTH:
         var10000 = NORTH;
         break;
      case SOUTH:
         var10000 = SOUTH;
         break;
      case UP:
         var10000 = UP;
         break;
      default:
         var10000 = DOWN;
      }

      return var10000;
   }

   // $FF: synthetic method
   private static AxisSelect[] $values() {
      return new AxisSelect[]{EAST, WEST, NORTH, SOUTH, UP, DOWN};
   }
}
