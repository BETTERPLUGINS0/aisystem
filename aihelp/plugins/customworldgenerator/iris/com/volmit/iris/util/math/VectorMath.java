package com.volmit.iris.util.math;

import com.volmit.iris.util.collection.GListAdapter;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.Form;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class VectorMath {
   public static Vector scaleStatic(Axis x, Vector v, double amt) {
      Vector var10000;
      switch(var0) {
      case X:
         var10000 = scaleX(var1, var2);
         break;
      case Y:
         var10000 = scaleY(var1, var2);
         break;
      case Z:
         var10000 = scaleZ(var1, var2);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public static Vector scaleX(Vector v, double amt) {
      double var3 = var0.getX();
      double var5 = var0.getY();
      double var7 = var0.getZ();
      double var9 = var3 == 0.0D ? 1.0D : var1 / var3;
      return new Vector(var3 * var9, var5 * var9, var7 * var9);
   }

   public static Vector scaleY(Vector v, double amt) {
      double var3 = var0.getX();
      double var5 = var0.getY();
      double var7 = var0.getZ();
      double var9 = var5 == 0.0D ? 1.0D : var1 / var5;
      return new Vector(var3 * var9, var5 * var9, var7 * var9);
   }

   public static Vector scaleZ(Vector v, double amt) {
      double var3 = var0.getX();
      double var5 = var0.getY();
      double var7 = var0.getZ();
      double var9 = var7 == 0.0D ? 1.0D : var1 / var7;
      return new Vector(var3 * var9, var5 * var9, var7 * var9);
   }

   public static Vector reverseXZ(Vector v) {
      var0.setX(-var0.getX());
      var0.setZ(-var0.getZ());
      return var0;
   }

   public static boolean isLookingNear(Location a, Location b, double maxOff) {
      Vector var4 = direction(var0, var1);
      Vector var5 = var0.getDirection();
      return var4.distance(var5) <= var2;
   }

   public static Vector rotate(Direction current, Direction to, Vector v) {
      if (var0.equals(var1)) {
         return var2;
      } else if (var0.equals(var1.reverse())) {
         return var0.isVertical() ? new Vector(var2.getX(), -var2.getY(), var2.getZ()) : new Vector(-var2.getX(), var2.getY(), -var2.getZ());
      } else {
         Vector var3 = var0.toVector().clone().add(var1.toVector());
         if (var3.getX() == 0.0D) {
            return var3.getY() != var3.getZ() ? rotate90CX(var2) : rotate90CCX(var2);
         } else if (var3.getY() == 0.0D) {
            return var3.getX() != var3.getZ() ? rotate90CY(var2) : rotate90CCY(var2);
         } else if (var3.getZ() == 0.0D) {
            return var3.getX() != var3.getY() ? rotate90CZ(var2) : rotate90CCZ(var2);
         } else {
            return var2;
         }
      }
   }

   public static Vector rotate(Direction current, Direction to, Vector v, int w, int h, int d) {
      if (var0.equals(var1)) {
         return var2;
      } else if (var0.equals(var1.reverse())) {
         return var0.isVertical() ? new Vector(var2.getX(), -var2.getY() + (double)var4, var2.getZ()) : new Vector(-var2.getX() + (double)var3, var2.getY(), -var2.getZ() + (double)var5);
      } else {
         Vector var6 = var0.toVector().clone().add(var1.toVector());
         if (var6.getX() == 0.0D) {
            return var6.getY() != var6.getZ() ? rotate90CX(var2, var5) : rotate90CCX(var2, var4);
         } else if (var6.getY() == 0.0D) {
            return var6.getX() != var6.getZ() ? rotate90CY(var2, var5) : rotate90CCY(var2, var3);
         } else if (var6.getZ() == 0.0D) {
            return var6.getX() != var6.getY() ? rotate90CZ(var2, var3) : rotate90CCZ(var2, var4);
         } else {
            return var2;
         }
      }
   }

   public static Vector rotate90CX(Vector v) {
      return new Vector(var0.getX(), -var0.getZ(), var0.getY());
   }

   public static Vector rotate90CCX(Vector v) {
      return new Vector(var0.getX(), var0.getZ(), -var0.getY());
   }

   public static Vector rotate90CY(Vector v) {
      return new Vector(-var0.getZ(), var0.getY(), var0.getX());
   }

   public static Vector rotate90CCY(Vector v) {
      return new Vector(var0.getZ(), var0.getY(), -var0.getX());
   }

   public static Vector rotate90CZ(Vector v) {
      return new Vector(var0.getY(), -var0.getX(), var0.getZ());
   }

   public static Vector rotate90CCZ(Vector v) {
      return new Vector(-var0.getY(), var0.getX(), var0.getZ());
   }

   public static Vector rotate90CX(Vector v, int s) {
      return new Vector(var0.getX(), -var0.getZ() + (double)var1, var0.getY());
   }

   public static Vector rotate90CCX(Vector v, int s) {
      return new Vector(var0.getX(), var0.getZ(), -var0.getY() + (double)var1);
   }

   public static Vector rotate90CY(Vector v, int s) {
      return new Vector(-var0.getZ() + (double)var1, var0.getY(), var0.getX());
   }

   public static Vector rotate90CCY(Vector v, int s) {
      return new Vector(var0.getZ(), var0.getY(), -var0.getX() + (double)var1);
   }

   public static Vector rotate90CZ(Vector v, int s) {
      return new Vector(var0.getY(), -var0.getX() + (double)var1, var0.getZ());
   }

   public static Vector rotate90CCZ(Vector v, int s) {
      return new Vector(-var0.getY() + (double)var1, var0.getX(), var0.getZ());
   }

   public static Vector getAxis(Direction current, Direction to) {
      if (!var0.equals(Direction.U) && !var0.equals(Direction.D)) {
         return new Vector(0, 1, 0);
      } else if (!var1.equals(Direction.U) && !var1.equals(Direction.D)) {
         return !var0.equals(Direction.N) && !var0.equals(Direction.S) ? Direction.S.toVector() : Direction.E.toVector();
      } else {
         return new Vector(1, 0, 0);
      }
   }

   private static double round(double value, int precision) {
      return Double.parseDouble(Form.f(var0, var2));
   }

   public static Vector clip(Vector v, int decimals) {
      var0.setX(round(var0.getX(), var1));
      var0.setY(round(var0.getY(), var1));
      var0.setZ(round(var0.getZ(), var1));
      return var0;
   }

   public static Vector rotateVectorCC(Vector vec, Vector axis, double deg) {
      double var4 = Math.toRadians(var2);
      double var6 = var0.getX();
      double var8 = var0.getY();
      double var10 = var0.getZ();
      double var12 = var1.getX();
      double var14 = var1.getY();
      double var16 = var1.getZ();
      double var18 = var12 * var6 + var14 * var8 + var16 * var10;
      double var20 = var12 * var18 * (1.0D - Math.cos(var4)) + var6 * Math.cos(var4) + (-var16 * var8 + var14 * var10) * Math.sin(var4);
      double var22 = var14 * var18 * (1.0D - Math.cos(var4)) + var8 * Math.cos(var4) + (var16 * var6 - var12 * var10) * Math.sin(var4);
      double var24 = var16 * var18 * (1.0D - Math.cos(var4)) + var10 * Math.cos(var4) + (-var14 * var6 + var12 * var8) * Math.sin(var4);
      return clip(new Vector(var20, var22, var24), 4);
   }

   public static KList<BlockFace> split(BlockFace f) {
      KList var1 = new KList();
      switch(var0) {
      case DOWN:
         var1.add((Object)BlockFace.DOWN);
         break;
      case EAST:
         var1.add((Object)BlockFace.EAST);
         break;
      case EAST_NORTH_EAST:
         var1.add((Object)BlockFace.EAST);
         var1.add((Object)BlockFace.EAST);
         var1.add((Object)BlockFace.NORTH);
         break;
      case EAST_SOUTH_EAST:
         var1.add((Object)BlockFace.EAST);
         var1.add((Object)BlockFace.EAST);
         var1.add((Object)BlockFace.SOUTH);
         break;
      case NORTH:
         var1.add((Object)BlockFace.NORTH);
         break;
      case NORTH_EAST:
         var1.add((Object)BlockFace.NORTH);
         var1.add((Object)BlockFace.EAST);
         break;
      case NORTH_NORTH_EAST:
         var1.add((Object)BlockFace.NORTH);
         var1.add((Object)BlockFace.NORTH);
         var1.add((Object)BlockFace.EAST);
         break;
      case NORTH_NORTH_WEST:
         var1.add((Object)BlockFace.NORTH);
         var1.add((Object)BlockFace.NORTH);
         var1.add((Object)BlockFace.WEST);
         break;
      case NORTH_WEST:
         var1.add((Object)BlockFace.NORTH);
         var1.add((Object)BlockFace.WEST);
         break;
      case SELF:
         var1.add((Object)BlockFace.SELF);
         break;
      case SOUTH:
         var1.add((Object)BlockFace.SOUTH);
         break;
      case SOUTH_EAST:
         var1.add((Object)BlockFace.SOUTH);
         var1.add((Object)BlockFace.EAST);
         break;
      case SOUTH_SOUTH_EAST:
         var1.add((Object)BlockFace.SOUTH);
         var1.add((Object)BlockFace.SOUTH);
         var1.add((Object)BlockFace.EAST);
         break;
      case SOUTH_SOUTH_WEST:
         var1.add((Object)BlockFace.SOUTH);
         var1.add((Object)BlockFace.SOUTH);
         var1.add((Object)BlockFace.WEST);
         break;
      case SOUTH_WEST:
         var1.add((Object)BlockFace.SOUTH);
         var1.add((Object)BlockFace.WEST);
         break;
      case UP:
         var1.add((Object)BlockFace.UP);
         break;
      case WEST:
         var1.add((Object)BlockFace.WEST);
         break;
      case WEST_NORTH_WEST:
         var1.add((Object)BlockFace.WEST);
         var1.add((Object)BlockFace.WEST);
         var1.add((Object)BlockFace.NORTH);
         break;
      case WEST_SOUTH_WEST:
         var1.add((Object)BlockFace.WEST);
         var1.add((Object)BlockFace.WEST);
         var1.add((Object)BlockFace.SOUTH);
      }

      return var1;
   }

   public static Vector direction(Location from, Location to) {
      return var1.clone().subtract(var0.clone()).toVector().normalize();
   }

   public static Vector directionNoNormal(Location from, Location to) {
      return var1.clone().subtract(var0.clone()).toVector();
   }

   public static Vector toVector(float yaw, float pitch) {
      return new Vector(Math.cos((double)var1) * Math.cos((double)var0), Math.sin((double)var1), Math.cos((double)var1) * Math.sin((double)(-var0)));
   }

   public static void impulse(Entity e, Vector v) {
      impulse(var0, var1, 1.0D);
   }

   public static void impulse(Entity e, Vector v, double effectiveness) {
      Vector var4 = var0.getVelocity();
      var4.add(var1.clone().multiply(var2));
      var0.setVelocity(var4);
   }

   public static Vector reverse(Vector v) {
      if (var0.getX() != 0.0D) {
         var0.setX(-var0.getX());
      }

      if (var0.getY() != 0.0D) {
         var0.setY(-var0.getY());
      }

      if (var0.getZ() != 0.0D) {
         var0.setZ(-var0.getZ());
      }

      return var0;
   }

   public static double getSpeed(Vector v) {
      Vector var1 = new Vector(0, 0, 0);
      Vector var2 = (new Vector(0, 0, 0)).add(var0);
      return var1.distance(var2);
   }

   public static KList<Vector> shift(Vector vector, KList<Vector> vectors) {
      return new KList((new GListAdapter<Vector, Vector>() {
         public Vector onAdapt(Vector from) {
            return var1.add(var0);
         }
      }).adapt(var1));
   }

   public static BlockFace getBlockFace(Vector v) {
      Vector var1 = triNormalize(var0);
      BlockFace[] var2 = BlockFace.values();
      int var3 = var2.length;

      int var4;
      BlockFace var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if (var1.getX() == (double)var5.getModX() && var1.getY() == (double)var5.getModY() && var1.getZ() == (double)var5.getModZ()) {
            return var5;
         }
      }

      var2 = BlockFace.values();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if (var1.getX() == (double)var5.getModX() && var1.getZ() == (double)var5.getModZ()) {
            return var5;
         }
      }

      var2 = BlockFace.values();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if (var1.getY() == (double)var5.getModY() && var1.getZ() == (double)var5.getModZ()) {
            return var5;
         }
      }

      var2 = BlockFace.values();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if (var1.getX() == (double)var5.getModX() || var1.getY() == (double)var5.getModY()) {
            return var5;
         }
      }

      var2 = BlockFace.values();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if (var1.getX() == (double)var5.getModX() || var1.getY() == (double)var5.getModY() || var1.getZ() == (double)var5.getModZ()) {
            return var5;
         }
      }

      return null;
   }

   public static Vector angleLeft(Vector v, float amt) {
      Location var2 = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
      var2.setDirection(var0);
      float var3 = var2.getYaw();
      float var4 = var2.getPitch();
      CDou var5 = new CDou(360.0D);
      CDou var6 = new CDou(180.0D);
      var5.set((double)var3);
      var6.set((double)var4);
      var5.sub((double)var1);
      var2.setYaw((float)var5.get());
      var2.setPitch((float)var6.get());
      return var2.getDirection();
   }

   public static Vector angleRight(Vector v, float amt) {
      Location var2 = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
      var2.setDirection(var0);
      float var3 = var2.getYaw();
      float var4 = var2.getPitch();
      CDou var5 = new CDou(360.0D);
      CDou var6 = new CDou(180.0D);
      var5.set((double)var3);
      var6.set((double)var4);
      var5.add((double)var1);
      var2.setYaw((float)var5.get());
      var2.setPitch((float)var6.get());
      return var2.getDirection();
   }

   public static Vector angleUp(Vector v, float amt) {
      Location var2 = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
      var2.setDirection(var0);
      float var3 = var2.getYaw();
      float var4 = var2.getPitch();
      CDou var5 = new CDou(360.0D);
      var5.set((double)var3);
      var2.setYaw((float)var5.get());
      var2.setPitch(Math.max(-90.0F, var4 - var1));
      return var2.getDirection();
   }

   public static Vector angleDown(Vector v, float amt) {
      Location var2 = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
      var2.setDirection(var0);
      float var3 = var2.getYaw();
      float var4 = var2.getPitch();
      CDou var5 = new CDou(360.0D);
      var5.set((double)var3);
      var2.setYaw((float)var5.get());
      var2.setPitch(Math.min(90.0F, var4 + var1));
      return var2.getDirection();
   }

   public static Vector triNormalize(Vector direction) {
      Vector var1 = var0.clone();
      var1.normalize();
      if (var1.getX() > 0.333D) {
         var1.setX(1);
      } else if (var1.getX() < -0.333D) {
         var1.setX(-1);
      } else {
         var1.setX(0);
      }

      if (var1.getY() > 0.333D) {
         var1.setY(1);
      } else if (var1.getY() < -0.333D) {
         var1.setY(-1);
      } else {
         var1.setY(0);
      }

      if (var1.getZ() > 0.333D) {
         var1.setZ(1);
      } else if (var1.getZ() < -0.333D) {
         var1.setZ(-1);
      } else {
         var1.setZ(0);
      }

      return var1;
   }
}
