package com.volmit.iris.util.math;

import com.volmit.iris.util.collection.GBiset;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.Cuboid;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public enum Direction {
   U(0, 1, 0, Cuboid.CuboidDirection.Up),
   D(0, -1, 0, Cuboid.CuboidDirection.Down),
   N(0, 0, -1, Cuboid.CuboidDirection.North),
   S(0, 0, 1, Cuboid.CuboidDirection.South),
   E(1, 0, 0, Cuboid.CuboidDirection.East),
   W(-1, 0, 0, Cuboid.CuboidDirection.West);

   private static KMap<GBiset<Direction, Direction>, DOP> permute = null;
   private final int x;
   private final int y;
   private final int z;
   private final Cuboid.CuboidDirection f;

   private Direction(int x, int y, int z, Cuboid.CuboidDirection f) {
      this.x = var3;
      this.y = var4;
      this.z = var5;
      this.f = var6;
   }

   public static Direction getDirection(BlockFace f) {
      Direction var10000;
      switch(var0) {
      case DOWN:
         var10000 = D;
         break;
      case EAST:
      case EAST_SOUTH_EAST:
      case EAST_NORTH_EAST:
         var10000 = E;
         break;
      case NORTH:
      case NORTH_WEST:
      case NORTH_NORTH_WEST:
      case NORTH_NORTH_EAST:
      case NORTH_EAST:
         var10000 = N;
         break;
      case SELF:
      case UP:
         var10000 = U;
         break;
      case SOUTH:
      case SOUTH_WEST:
      case SOUTH_SOUTH_WEST:
      case SOUTH_SOUTH_EAST:
      case SOUTH_EAST:
         var10000 = S;
         break;
      case WEST:
      case WEST_SOUTH_WEST:
      case WEST_NORTH_WEST:
         var10000 = W;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static Direction closest(Vector v) {
      double var1 = Double.MAX_VALUE;
      Direction var3 = null;
      Direction[] var4 = values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Direction var7 = var4[var6];
         Vector var8 = var7.toVector();
         double var9 = var8.dot(var0);
         if (var9 < var1) {
            var1 = var9;
            var3 = var7;
         }
      }

      return var3;
   }

   public static Direction closest(Vector v, Direction... d) {
      double var2 = Double.MAX_VALUE;
      Direction var4 = null;
      Direction[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Direction var8 = var5[var7];
         Vector var9 = var8.toVector();
         double var10 = var9.distance(var0);
         if (var10 < var2) {
            var2 = var10;
            var4 = var8;
         }
      }

      return var4;
   }

   public static Direction closest(Vector v, KList<Direction> d) {
      double var2 = Double.MAX_VALUE;
      Direction var4 = null;
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         Direction var6 = (Direction)var5.next();
         Vector var7 = var6.toVector();
         double var8 = var7.distance(var0);
         if (var8 < var2) {
            var2 = var8;
            var4 = var6;
         }
      }

      return var4;
   }

   public static KList<Direction> news() {
      return (new KList()).add((Object[])(N, E, W, S));
   }

   public static Direction getDirection(Vector v) {
      Vector var1 = VectorMath.triNormalize(var0.clone().normalize());
      Iterator var2 = udnews().iterator();

      Direction var3;
      do {
         if (!var2.hasNext()) {
            return N;
         }

         var3 = (Direction)var2.next();
      } while(var3.x != var1.getBlockX() || var3.y != var1.getBlockY() || var3.z != var1.getBlockZ());

      return var3;
   }

   public static KList<Direction> udnews() {
      return (new KList()).add((Object[])(U, D, N, E, W, S));
   }

   public static Direction fromByte(byte b) {
      if (var0 <= 5 && var0 >= 0) {
         if (var0 == 0) {
            return D;
         } else if (var0 == 1) {
            return U;
         } else if (var0 == 2) {
            return N;
         } else if (var0 == 3) {
            return S;
         } else {
            return var0 == 4 ? W : E;
         }
      } else {
         return null;
      }
   }

   public static void calculatePermutations() {
      if (permute == null) {
         permute = new KMap();
         Iterator var0 = udnews().iterator();

         while(var0.hasNext()) {
            Direction var1 = (Direction)var0.next();
            Iterator var2 = udnews().iterator();

            while(var2.hasNext()) {
               Direction var3 = (Direction)var2.next();
               GBiset var4 = new GBiset(var1, var3);
               if (var1.equals(var3)) {
                  permute.put(var4, new DOP("DIRECT") {
                     public Vector op(Vector v) {
                        return var1;
                     }
                  });
               } else if (var1.reverse().equals(var3)) {
                  if (var1.isVertical()) {
                     permute.put(var4, new DOP("R180CCZ") {
                        public Vector op(Vector v) {
                           return VectorMath.rotate90CCZ(VectorMath.rotate90CCZ(var1));
                        }
                     });
                  } else {
                     permute.put(var4, new DOP("R180CCY") {
                        public Vector op(Vector v) {
                           return VectorMath.rotate90CCY(VectorMath.rotate90CCY(var1));
                        }
                     });
                  }
               } else if (getDirection(VectorMath.rotate90CX(var1.toVector())).equals(var3)) {
                  permute.put(var4, new DOP("R90CX") {
                     public Vector op(Vector v) {
                        return VectorMath.rotate90CX(var1);
                     }
                  });
               } else if (getDirection(VectorMath.rotate90CCX(var1.toVector())).equals(var3)) {
                  permute.put(var4, new DOP("R90CCX") {
                     public Vector op(Vector v) {
                        return VectorMath.rotate90CCX(var1);
                     }
                  });
               } else if (getDirection(VectorMath.rotate90CY(var1.toVector())).equals(var3)) {
                  permute.put(var4, new DOP("R90CY") {
                     public Vector op(Vector v) {
                        return VectorMath.rotate90CY(var1);
                     }
                  });
               } else if (getDirection(VectorMath.rotate90CCY(var1.toVector())).equals(var3)) {
                  permute.put(var4, new DOP("R90CCY") {
                     public Vector op(Vector v) {
                        return VectorMath.rotate90CCY(var1);
                     }
                  });
               } else if (getDirection(VectorMath.rotate90CZ(var1.toVector())).equals(var3)) {
                  permute.put(var4, new DOP("R90CZ") {
                     public Vector op(Vector v) {
                        return VectorMath.rotate90CZ(var1);
                     }
                  });
               } else if (getDirection(VectorMath.rotate90CCZ(var1.toVector())).equals(var3)) {
                  permute.put(var4, new DOP("R90CCZ") {
                     public Vector op(Vector v) {
                        return VectorMath.rotate90CCZ(var1);
                     }
                  });
               } else {
                  permute.put(var4, new DOP("FAIL") {
                     public Vector op(Vector v) {
                        return var1;
                     }
                  });
               }
            }
         }

      }
   }

   public String toString() {
      String var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = "Up";
         break;
      case 1:
         var10000 = "Down";
         break;
      case 2:
         var10000 = "North";
         break;
      case 3:
         var10000 = "South";
         break;
      case 4:
         var10000 = "East";
         break;
      case 5:
         var10000 = "West";
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public boolean isVertical() {
      return this.equals(D) || this.equals(U);
   }

   public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
   }

   public boolean isCrooked(Direction to) {
      if (this.equals(var1.reverse())) {
         return false;
      } else {
         return !this.equals(var1);
      }
   }

   public Vector angle(Vector initial, Direction d) {
      calculatePermutations();
      Iterator var3 = permute.entrySet().iterator();

      Entry var4;
      GBiset var5;
      do {
         if (!var3.hasNext()) {
            return var1;
         }

         var4 = (Entry)var3.next();
         var5 = (GBiset)var4.getKey();
      } while(!((Direction)var5.getA()).equals(this) || !((Direction)var5.getB()).equals(var2));

      return ((DOP)var4.getValue()).op(var1);
   }

   public Direction reverse() {
      switch(this.ordinal()) {
      case 0:
         return D;
      case 1:
         return U;
      case 2:
         return S;
      case 3:
         return N;
      case 4:
         return W;
      case 5:
         return E;
      default:
         return null;
      }
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public int z() {
      return this.z;
   }

   public Cuboid.CuboidDirection f() {
      return this.f;
   }

   public byte byteValue() {
      switch(this.ordinal()) {
      case 0:
         return 1;
      case 1:
         return 0;
      case 2:
         return 2;
      case 3:
         return 3;
      case 4:
         return 5;
      case 5:
         return 4;
      default:
         return -1;
      }
   }

   public BlockFace getFace() {
      BlockFace var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = BlockFace.UP;
         break;
      case 1:
         var10000 = BlockFace.DOWN;
         break;
      case 2:
         var10000 = BlockFace.NORTH;
         break;
      case 3:
         var10000 = BlockFace.SOUTH;
         break;
      case 4:
         var10000 = BlockFace.EAST;
         break;
      case 5:
         var10000 = BlockFace.WEST;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public Axis getAxis() {
      Axis var10000;
      switch(this.ordinal()) {
      case 0:
      case 1:
         var10000 = Axis.Y;
         break;
      case 2:
      case 3:
         var10000 = Axis.Z;
         break;
      case 4:
      case 5:
         var10000 = Axis.X;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Direction[] $values() {
      return new Direction[]{U, D, N, S, E, W};
   }
}
