package com.bergerkiller.bukkit.tc.rails.direction;

import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import java.util.ArrayList;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public interface RailEnterDirection {
   RailEnterDirection[] ALL = RailEnterDirectionImpl.ALL;

   String name();

   double motionDot(Vector var1);

   boolean match(RailState var1);

   static RailEnterDirection toFace(BlockFace face) {
      return RailEnterDirectionToFace.fromFace(face);
   }

   static RailEnterDirection intoDirection(Direction direction, BlockFace forwardDirection) {
      return toFace(direction.getDirection(forwardDirection));
   }

   static RailEnterDirection fromJunction(RailJunction junction) {
      return new RailEnterDirectionFromJunction(junction);
   }

   static RailEnterDirection[] parseAll(RailPiece rail, BlockFace forwardDirection, String text) {
      return RailEnterDirectionImpl.parseAll(rail, forwardDirection, text);
   }

   static BlockFace[] toFacesOnly(RailEnterDirection[] directions) {
      if (directions == null) {
         return null;
      } else {
         int len = directions.length;
         if (len == 0) {
            return new BlockFace[0];
         } else if (len == 1) {
            RailEnterDirection dir = directions[0];
            return dir instanceof RailEnterDirectionToFace ? new BlockFace[]{((RailEnterDirectionToFace)dir).getFace()} : new BlockFace[0];
         } else {
            ArrayList<BlockFace> faces = new ArrayList(len);

            for(int i = 0; i < len; ++i) {
               RailEnterDirection dir = directions[i];
               if (dir instanceof RailEnterDirectionToFace) {
                  faces.add(((RailEnterDirectionToFace)dir).getFace());
               }
            }

            return (BlockFace[])faces.toArray(new BlockFace[faces.size()]);
         }
      }
   }
}
