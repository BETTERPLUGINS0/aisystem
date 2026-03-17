package com.bergerkiller.bukkit.tc.rails.direction;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Locale;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public final class RailEnterDirectionToFace implements RailEnterDirection {
   private static final EnumMap<BlockFace, RailEnterDirection> byFace = new EnumMap(BlockFace.class);
   private static final EnumMap<BlockFace, RailEnterDirection[]> arrByFace = new EnumMap(BlockFace.class);
   private final BlockFace face;
   private final String name;

   static RailEnterDirection fromFace(BlockFace face) {
      return (RailEnterDirection)byFace.computeIfAbsent(face, (f) -> {
         throw new IllegalArgumentException("Invalid block face: " + f);
      });
   }

   static RailEnterDirection[] arrayFromFace(BlockFace face) {
      return (RailEnterDirection[])arrByFace.computeIfAbsent(face, (f) -> {
         throw new IllegalArgumentException("Invalid block face: " + f);
      });
   }

   private RailEnterDirectionToFace(BlockFace face) {
      this.face = face;
      this.name = face.name().toLowerCase(Locale.ENGLISH).substring(0, 1);
   }

   public BlockFace getFace() {
      return this.face;
   }

   public String name() {
      return this.name;
   }

   public double motionDot(Vector motion) {
      return motion.dot(FaceUtil.faceToVector(this.face));
   }

   public boolean match(RailState state) {
      return this.face == state.enterFace();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else {
         return o instanceof RailEnterDirectionToFace ? this.face.equals(((RailEnterDirectionToFace)o).getFace()) : false;
      }
   }

   public String toString() {
      return "EnterFrom{face=" + this.face.name().toLowerCase(Locale.ENGLISH) + "}";
   }

   static {
      BlockFace[] var0 = FaceUtil.BLOCK_SIDES;
      int var1 = var0.length;

      int var2;
      BlockFace face;
      for(var2 = 0; var2 < var1; ++var2) {
         face = var0[var2];
         RailEnterDirectionToFace re = new RailEnterDirectionToFace(face);
         byFace.put(face, re);
         arrByFace.put(face, new RailEnterDirection[]{re});
      }

      var0 = BlockFace.values();
      var1 = var0.length;

      for(var2 = 0; var2 < var1; ++var2) {
         face = var0[var2];
         if (face.getModX() != 0 && face.getModZ() != 0) {
            ArrayList<RailEnterDirection> values = new ArrayList(2);
            BlockFace[] var5 = FaceUtil.getFaces(Util.snapFace(face));
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               BlockFace blockFace = var5[var7];
               values.add(fromFace(blockFace));
            }

            arrByFace.put(face, (RailEnterDirection[])values.toArray(new RailEnterDirection[values.size()]));
         }
      }

   }
}
