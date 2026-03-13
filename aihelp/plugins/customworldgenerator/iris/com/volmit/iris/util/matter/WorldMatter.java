package com.volmit.iris.util.matter;

import com.volmit.iris.Iris;
import com.volmit.iris.util.data.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public class WorldMatter {
   public static void placeMatter(Matter matter, Location at) {
      if (var0.hasSlice(BlockData.class)) {
         var0.slice(BlockData.class).writeInto(var1);
      }

      if (var0.hasSlice(MatterEntityGroup.class)) {
         var0.slice(MatterEntityGroup.class).writeInto(var1);
      }

      if (var0.hasSlice(TileWrapper.class)) {
         var0.slice(TileWrapper.class).writeInto(var1);
      }

   }

   public static Matter createMatter(String author, Location a, Location b) {
      Cuboid var3 = new Cuboid(var1, var2);
      IrisMatter var4 = new IrisMatter(var3.getSizeX(), var3.getSizeY(), var3.getSizeZ());
      int var10000 = var4.getWidth();
      Iris.info(var10000 + " " + var4.getHeight() + " " + var4.getDepth());
      var4.getHeader().setAuthor(var0);
      var4.slice(BlockData.class).readFrom(var3.getLowerNE());
      var4.slice(MatterEntityGroup.class).readFrom(var3.getLowerNE());
      var4.slice(TileWrapper.class).readFrom(var3.getLowerNE());
      var4.trimSlices();
      return var4;
   }
}
