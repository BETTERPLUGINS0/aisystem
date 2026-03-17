package com.bergerkiller.bukkit.tc.rails.direction;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import org.bukkit.block.BlockFace;

class RailEnterDirectionImpl {
   public static final RailEnterDirection[] NONE = new RailEnterDirection[0];
   public static final RailEnterDirection[] ALL;
   private static final Map<String, RailEnterDirectionImpl.DirectionEnterDirection> DIRECTION_BY_NAME;
   private static final Map<Character, Direction> DIRECTION_BY_CHAR;

   public static RailEnterDirection[] parseAll(RailPiece rail, BlockFace forwardDirection, String text) {
      forwardDirection = Util.snapFace(forwardDirection);
      RailEnterDirectionImpl.DirectionEnterDirection dir = (RailEnterDirectionImpl.DirectionEnterDirection)DIRECTION_BY_NAME.get(text);
      if (dir != null) {
         return dir.get(forwardDirection);
      } else {
         dir = (RailEnterDirectionImpl.DirectionEnterDirection)DIRECTION_BY_NAME.get(text.toLowerCase(Locale.ENGLISH));
         if (dir != null) {
            return dir.get(forwardDirection);
         } else {
            RailEnterDirectionImpl.DirectionList result = new RailEnterDirectionImpl.DirectionList(text);
            Iterator var4 = rail.getJunctions().iterator();

            while(true) {
               RailJunction junction;
               String name;
               int nameLen;
               do {
                  do {
                     if (!var4.hasNext()) {
                        result.finish(forwardDirection);
                        return result.toArray();
                     }

                     junction = (RailJunction)var4.next();
                     name = junction.name();
                     nameLen = name.length();
                  } while(nameLen == 0);
               } while(nameLen == 1 && DIRECTION_BY_CHAR.containsKey(name.charAt(0)));

               result.matchJunction(name, junction);
            }
         }
      }
   }

   static {
      ALL = new RailEnterDirection[FaceUtil.BLOCK_SIDES.length];

      for(int i = 0; i < ALL.length; ++i) {
         ALL[i] = RailEnterDirection.toFace(FaceUtil.BLOCK_SIDES[i]);
      }

      DIRECTION_BY_NAME = new HashMap();
      DIRECTION_BY_CHAR = new HashMap();
      Direction[] var11 = Direction.values();
      int var1 = var11.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Direction direction = var11[var2];
         if (direction != Direction.NONE) {
            RailEnterDirectionImpl.DirectionEnterDirection enterDirection;
            if (direction.isAbsolute()) {
               RailEnterDirection[] constant = RailEnterDirectionToFace.arrayFromFace(direction.getDirection(BlockFace.DOWN));
               enterDirection = (a) -> {
                  return constant;
               };
            } else {
               enterDirection = (a) -> {
                  return RailEnterDirectionToFace.arrayFromFace(direction.getDirection(a));
               };
            }

            String[] var12 = direction.aliases();
            int var6 = var12.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String name = var12[var7];
               String name_lower = name.toLowerCase(Locale.ENGLISH);
               String name_upper = name.toUpperCase(Locale.ENGLISH);
               DIRECTION_BY_NAME.put(name_lower, enterDirection);
               DIRECTION_BY_NAME.put(name_upper, enterDirection);
               if (name.length() == 1) {
                  DIRECTION_BY_CHAR.put(name_lower.charAt(0), direction);
                  DIRECTION_BY_CHAR.put(name_upper.charAt(0), direction);
               }
            }
         }
      }

      DIRECTION_BY_NAME.put("*", (a) -> {
         return ALL;
      });
      DIRECTION_BY_NAME.put("all", (a) -> {
         return ALL;
      });
      DIRECTION_BY_NAME.put("ALL", (a) -> {
         return ALL;
      });
      DIRECTION_BY_NAME.put("", (a) -> {
         return NONE;
      });
   }

   @FunctionalInterface
   private interface DirectionEnterDirection {
      RailEnterDirection[] get(BlockFace var1);
   }

   private static class DirectionList {
      final LinkedList<RailEnterDirectionImpl.DirectionToken> list = new LinkedList();

      public DirectionList(String text) {
         this.list.add(new RailEnterDirectionImpl.DirectionToken(text));
      }

      public void matchJunction(String name, RailJunction junction) {
         ListIterator iter = this.list.listIterator();

         while(iter.hasNext()) {
            RailEnterDirectionImpl.DirectionToken token = (RailEnterDirectionImpl.DirectionToken)iter.next();
            int index = token.text.indexOf(name);
            if (index != -1) {
               int len = name.length();
               if (index == 0) {
                  if (token.text.length() == len) {
                     token.text = "";
                     token.direction = RailEnterDirection.fromJunction(junction);
                  } else {
                     token.text = token.text.substring(index + len);
                     iter.previous();
                     iter.add(new RailEnterDirectionImpl.DirectionToken(RailEnterDirection.fromJunction(junction)));
                  }
               } else {
                  iter.add(new RailEnterDirectionImpl.DirectionToken(RailEnterDirection.fromJunction(junction)));
                  if (index + len < token.text.length()) {
                     iter.add(new RailEnterDirectionImpl.DirectionToken(token.text.substring(index + len)));
                     iter.previous();
                  }

                  token.text = token.text.substring(0, index);
                  iter.previous();
                  iter.previous();
               }
            }
         }

      }

      public void finish(BlockFace forwardDirection) {
         ListIterator iter = this.list.listIterator();

         while(true) {
            RailEnterDirectionImpl.DirectionToken token;
            do {
               if (!iter.hasNext()) {
                  return;
               }

               token = (RailEnterDirectionImpl.DirectionToken)iter.next();
            } while(token.text.isEmpty());

            int len = token.text.length();
            boolean first = true;

            for(int ch_idx = 0; ch_idx < len; ++ch_idx) {
               Direction ch_dir = (Direction)RailEnterDirectionImpl.DIRECTION_BY_CHAR.get(token.text.charAt(ch_idx));
               if (ch_dir != null) {
                  RailEnterDirection[] var8 = RailEnterDirectionToFace.arrayFromFace(ch_dir.getDirection(forwardDirection));
                  int var9 = var8.length;

                  for(int var10 = 0; var10 < var9; ++var10) {
                     RailEnterDirection enterDir = var8[var10];
                     if (first) {
                        first = false;
                        token.direction = enterDir;
                     } else {
                        iter.add(new RailEnterDirectionImpl.DirectionToken(enterDir));
                     }
                  }
               }
            }

            token.text = "";
            if (first) {
               iter.remove();
            }
         }
      }

      public RailEnterDirection[] toArray() {
         RailEnterDirection[] result = new RailEnterDirection[this.list.size()];
         int i = -1;

         RailEnterDirectionImpl.DirectionToken token;
         for(Iterator var3 = this.list.iterator(); var3.hasNext(); result[i] = token.direction) {
            token = (RailEnterDirectionImpl.DirectionToken)var3.next();
            ++i;
         }

         return result;
      }
   }

   private static class DirectionToken {
      String text;
      RailEnterDirection direction;

      public DirectionToken(String text) {
         this.text = text;
         this.direction = null;
      }

      public DirectionToken(RailEnterDirection direction) {
         this.text = "";
         this.direction = direction;
      }
   }
}
