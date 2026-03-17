package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import java.util.LinkedHashSet;
import org.bukkit.block.BlockFace;

public enum Direction {
   NORTH(true, new String[]{"n", "north"}),
   EAST(true, new String[]{"e", "east"}),
   SOUTH(true, new String[]{"s", "south"}),
   WEST(true, new String[]{"w", "west"}),
   LEFT(false, new String[]{"l", "left"}),
   RIGHT(false, new String[]{"r", "right"}),
   IMPLICIT_LEFT(false, new String[]{"implicit_left"}),
   IMPLICIT_RIGHT(false, new String[]{"implicit_right"}),
   FORWARD(false, new String[]{"f", "front", "forward", "forwards"}),
   BACKWARD(false, new String[]{"b", "back", "backward", "backwards"}),
   UP(true, new String[]{"u", "up", "upwards", "above"}),
   DOWN(true, new String[]{"d", "down", "downwards", "below"}),
   CONTINUE(false, new String[]{"continue"}),
   REVERSE(false, new String[]{"reverse"}),
   NONE(true, new String[]{"", "n", "none"});

   private final boolean absolute;
   private final String[] aliases;

   private Direction(boolean absolute, String... aliases) {
      this.absolute = absolute;
      this.aliases = aliases;
   }

   public boolean isAbsolute() {
      return this.absolute;
   }

   public String[] aliases() {
      return this.aliases;
   }

   public BlockFace getDirection(BlockFace signfacing) {
      return this.getDirectionLegacy(signfacing, signfacing.getOppositeFace());
   }

   public BlockFace getDirection(BlockFace signfacing, BlockFace cartdirection) {
      switch(this) {
      case NORTH:
         return BlockFace.NORTH;
      case EAST:
         return BlockFace.EAST;
      case SOUTH:
         return BlockFace.SOUTH;
      case WEST:
         return BlockFace.WEST;
      case DOWN:
         return BlockFace.DOWN;
      case UP:
         return BlockFace.UP;
      case LEFT:
      case IMPLICIT_LEFT:
         return FaceUtil.rotate(signfacing, 2);
      case RIGHT:
      case IMPLICIT_RIGHT:
         return FaceUtil.rotate(signfacing, -2);
      case FORWARD:
         return signfacing.getOppositeFace();
      case BACKWARD:
         return signfacing;
      case CONTINUE:
         return cartdirection;
      case REVERSE:
         return cartdirection.getOppositeFace();
      default:
         return cartdirection;
      }
   }

   public BlockFace getDirectionLegacy(BlockFace signfacing, BlockFace cartdirection) {
      switch(this) {
      case NORTH:
         return BlockFace.NORTH;
      case EAST:
         return BlockFace.EAST;
      case SOUTH:
         return BlockFace.SOUTH;
      case WEST:
         return BlockFace.WEST;
      case DOWN:
         return BlockFace.DOWN;
      case UP:
         return BlockFace.UP;
      case LEFT:
      case IMPLICIT_LEFT:
         return FaceUtil.rotate(signfacing, 2);
      case RIGHT:
      case IMPLICIT_RIGHT:
         return FaceUtil.rotate(signfacing, -2);
      case FORWARD:
      case CONTINUE:
         return cartdirection;
      case BACKWARD:
      case REVERSE:
         return cartdirection.getOppositeFace();
      default:
         return cartdirection;
      }
   }

   public boolean match(char character) {
      String[] var2 = this.aliases;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String alias = var2[var4];
         if (alias.length() == 1 && alias.charAt(0) == character) {
            return true;
         }
      }

      return false;
   }

   public boolean match(String text) {
      String[] var2 = this.aliases;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String alias = var2[var4];
         if (alias.equalsIgnoreCase(text)) {
            return true;
         }
      }

      return false;
   }

   public static Direction parse(char character) {
      Direction[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Direction dir = var1[var3];
         if (dir.match(character)) {
            return dir;
         }
      }

      return NONE;
   }

   public static Direction parse(String text) {
      Direction[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Direction dir = var1[var3];
         if (dir.match(text)) {
            return dir;
         }
      }

      return NONE;
   }

   public static Direction fromFace(BlockFace face) {
      switch(face) {
      case NORTH:
         return NORTH;
      case EAST:
         return EAST;
      case SOUTH:
         return SOUTH;
      case WEST:
         return WEST;
      case UP:
         return UP;
      case DOWN:
         return DOWN;
      case SELF:
         return CONTINUE;
      default:
         return NONE;
      }
   }

   public static Direction[] parseAll(String text) {
      if (!text.equalsIgnoreCase("all") && !text.equals("*")) {
         LinkedHashSet<Direction> faces = new LinkedHashSet();
         Direction dir = parse(text);
         if (dir == NONE) {
            char[] var3 = text.toCharArray();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               char c = var3[var5];
               dir = parse(c);
               if (dir == NONE) {
                  return new Direction[0];
               }

               faces.add(dir);
            }
         } else {
            faces.add(dir);
         }

         return (Direction[])faces.toArray(new Direction[0]);
      } else {
         Direction[] dirs = new Direction[FaceUtil.BLOCK_SIDES.length];

         for(int i = 0; i < dirs.length; ++i) {
            dirs[i] = fromFace(FaceUtil.BLOCK_SIDES[i]);
         }

         return dirs;
      }
   }

   public static BlockFace[] parseAll(String text, BlockFace absoluteDirection) {
      Direction[] dirs = parseAll(text);
      BlockFace[] faces = new BlockFace[dirs.length];

      for(int i = 0; i < faces.length; ++i) {
         faces[i] = dirs[i].getDirection(absoluteDirection);
      }

      return faces;
   }

   // $FF: synthetic method
   private static Direction[] $values() {
      return new Direction[]{NORTH, EAST, SOUTH, WEST, LEFT, RIGHT, IMPLICIT_LEFT, IMPLICIT_RIGHT, FORWARD, BACKWARD, UP, DOWN, CONTINUE, REVERSE, NONE};
   }
}
