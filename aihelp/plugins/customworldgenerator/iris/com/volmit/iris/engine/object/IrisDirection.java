package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.collection.GBiset;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.Cuboid;
import com.volmit.iris.util.math.DOP;
import com.volmit.iris.util.math.VectorMath;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Jigsaw.Orientation;
import org.bukkit.util.Vector;

@Desc("A direction object")
public enum IrisDirection {
   @Desc("0, 1, 0")
   UP_POSITIVE_Y(0, 1, 0, Cuboid.CuboidDirection.Up),
   @Desc("0, -1, 0")
   DOWN_NEGATIVE_Y(0, -1, 0, Cuboid.CuboidDirection.Down),
   @Desc("0, 0, -1")
   NORTH_NEGATIVE_Z(0, 0, -1, Cuboid.CuboidDirection.North),
   @Desc("0, 0, 1")
   SOUTH_POSITIVE_Z(0, 0, 1, Cuboid.CuboidDirection.South),
   @Desc("1, 0, 0")
   EAST_POSITIVE_X(1, 0, 0, Cuboid.CuboidDirection.East),
   @Desc("-1, 0, 0")
   WEST_NEGATIVE_X(-1, 0, 0, Cuboid.CuboidDirection.West);

   private static KMap<GBiset<IrisDirection, IrisDirection>, DOP> permute = null;
   private final int x;
   private final int y;
   private final int z;
   private final Cuboid.CuboidDirection f;

   private IrisDirection(int x, int y, int z, Cuboid.CuboidDirection f) {
      this.x = var3;
      this.y = var4;
      this.z = var5;
      this.f = var6;
   }

   public static IrisDirection getDirection(BlockFace f) {
      IrisDirection var10000;
      switch(var0) {
      case DOWN:
         var10000 = DOWN_NEGATIVE_Y;
         break;
      case EAST:
      case EAST_NORTH_EAST:
      case EAST_SOUTH_EAST:
         var10000 = EAST_POSITIVE_X;
         break;
      case NORTH:
      case NORTH_NORTH_WEST:
      case NORTH_EAST:
      case NORTH_NORTH_EAST:
      case NORTH_WEST:
         var10000 = NORTH_NEGATIVE_Z;
         break;
      case SELF:
      case UP:
         var10000 = UP_POSITIVE_Y;
         break;
      case SOUTH:
      case SOUTH_EAST:
      case SOUTH_SOUTH_EAST:
      case SOUTH_SOUTH_WEST:
      case SOUTH_WEST:
         var10000 = SOUTH_POSITIVE_Z;
         break;
      case WEST:
      case WEST_NORTH_WEST:
      case WEST_SOUTH_WEST:
         var10000 = WEST_NEGATIVE_X;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static IrisDirection fromJigsawBlock(String direction) {
      IrisDirection[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         IrisDirection var4 = var1[var3];
         if (var4.name().toLowerCase().split("\\Q_\\E")[0].equals(var0.split("\\Q_\\E")[0])) {
            return var4;
         }
      }

      return null;
   }

   public static IrisDirection getDirection(Orientation orientation) {
      IrisDirection var10000;
      switch(var0) {
      case DOWN_EAST:
      case UP_EAST:
      case EAST_UP:
         var10000 = EAST_POSITIVE_X;
         break;
      case DOWN_NORTH:
      case UP_NORTH:
      case NORTH_UP:
         var10000 = NORTH_NEGATIVE_Z;
         break;
      case DOWN_SOUTH:
      case UP_SOUTH:
      case SOUTH_UP:
         var10000 = SOUTH_POSITIVE_Z;
         break;
      case DOWN_WEST:
      case UP_WEST:
      case WEST_UP:
         var10000 = WEST_NEGATIVE_X;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static IrisDirection closest(Vector v) {
      double var1 = Double.MAX_VALUE;
      IrisDirection var3 = null;
      IrisDirection[] var4 = values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         IrisDirection var7 = var4[var6];
         Vector var8 = var7.toVector();
         double var9 = var8.distance(var0);
         if (var9 < var1) {
            var1 = var9;
            var3 = var7;
         }
      }

      return var3;
   }

   public static IrisDirection closest(Vector v, IrisDirection... d) {
      double var2 = Double.MAX_VALUE;
      IrisDirection var4 = null;
      IrisDirection[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         IrisDirection var8 = var5[var7];
         Vector var9 = var8.toVector();
         double var10 = var9.distance(var0);
         if (var10 < var2) {
            var2 = var10;
            var4 = var8;
         }
      }

      return var4;
   }

   public static IrisDirection closest(Vector v, KList<IrisDirection> d) {
      double var2 = Double.MAX_VALUE;
      IrisDirection var4 = null;
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         IrisDirection var6 = (IrisDirection)var5.next();
         Vector var7 = var6.toVector();
         double var8 = var7.distance(var0);
         if (var8 < var2) {
            var2 = var8;
            var4 = var6;
         }
      }

      return var4;
   }

   public static KList<IrisDirection> news() {
      return (new KList()).add((Object[])(NORTH_NEGATIVE_Z, EAST_POSITIVE_X, WEST_NEGATIVE_X, SOUTH_POSITIVE_Z));
   }

   public static IrisDirection getDirection(Vector v) {
      Vector var1 = VectorMath.triNormalize(var0.clone().normalize());
      Iterator var2 = udnews().iterator();

      IrisDirection var3;
      do {
         if (!var2.hasNext()) {
            return NORTH_NEGATIVE_Z;
         }

         var3 = (IrisDirection)var2.next();
      } while(var3.x != var1.getBlockX() || var3.y != var1.getBlockY() || var3.z != var1.getBlockZ());

      return var3;
   }

   public static KList<IrisDirection> udnews() {
      return (new KList()).add((Object[])(UP_POSITIVE_Y, DOWN_NEGATIVE_Y, NORTH_NEGATIVE_Z, EAST_POSITIVE_X, WEST_NEGATIVE_X, SOUTH_POSITIVE_Z));
   }

   public static IrisDirection fromByte(byte b) {
      if (var0 <= 5 && var0 >= 0) {
         if (var0 == 0) {
            return DOWN_NEGATIVE_Y;
         } else if (var0 == 1) {
            return UP_POSITIVE_Y;
         } else if (var0 == 2) {
            return NORTH_NEGATIVE_Z;
         } else if (var0 == 3) {
            return SOUTH_POSITIVE_Z;
         } else {
            return var0 == 4 ? WEST_NEGATIVE_X : EAST_POSITIVE_X;
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
            IrisDirection var1 = (IrisDirection)var0.next();
            Iterator var2 = udnews().iterator();

            while(var2.hasNext()) {
               IrisDirection var3 = (IrisDirection)var2.next();
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
      return this.equals(DOWN_NEGATIVE_Y) || this.equals(UP_POSITIVE_Y);
   }

   public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
   }

   public boolean isCrooked(IrisDirection to) {
      if (this.equals(var1.reverse())) {
         return false;
      } else {
         return !this.equals(var1);
      }
   }

   public Vector angle(Vector initial, IrisDirection d) {
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
      } while(!((IrisDirection)var5.getA()).equals(this) || !((IrisDirection)var5.getB()).equals(var2));

      return ((DOP)var4.getValue()).op(var1);
   }

   public IrisDirection reverse() {
      switch(this.ordinal()) {
      case 0:
         return DOWN_NEGATIVE_Y;
      case 1:
         return UP_POSITIVE_Y;
      case 2:
         return SOUTH_POSITIVE_Z;
      case 3:
         return NORTH_NEGATIVE_Z;
      case 4:
         return WEST_NEGATIVE_X;
      case 5:
         return EAST_POSITIVE_X;
      default:
         return EAST_POSITIVE_X;
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
   private static IrisDirection[] $values() {
      return new IrisDirection[]{UP_POSITIVE_Y, DOWN_NEGATIVE_Y, NORTH_NEGATIVE_Z, SOUTH_POSITIVE_Z, EAST_POSITIVE_X, WEST_NEGATIVE_X};
   }
}
