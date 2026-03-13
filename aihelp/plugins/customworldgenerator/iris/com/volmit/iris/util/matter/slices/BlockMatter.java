package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

@Sliced
public class BlockMatter extends RawMatter<BlockData> {
   public static final BlockData AIR;

   public BlockMatter() {
      this(1, 1, 1);
   }

   public BlockMatter(int width, int height, int depth) {
      super(var1, var2, var3, BlockData.class);
      this.registerWriter(World.class, (var0, var1x, var2x, var3x, var4) -> {
         if (var1x instanceof IrisCustomData) {
            IrisCustomData var5 = (IrisCustomData)var1x;
            var0.getBlockAt(var2x, var3x, var4).setBlockData(var5.getBase());
         } else {
            var0.getBlockAt(var2x, var3x, var4).setBlockData(var1x);
         }

      });
      this.registerReader(World.class, (var0, var1x, var2x, var3x) -> {
         BlockData var4 = var0.getBlockAt(var1x, var2x, var3x).getBlockData();
         return var4.getMaterial().isAir() ? null : var4;
      });
   }

   public Palette<BlockData> getGlobalPalette() {
      return null;
   }

   public void writeNode(BlockData b, DataOutputStream dos) {
      var2.writeUTF(var1.getAsString(true));
   }

   public BlockData readNode(DataInputStream din) {
      return B.get(var1.readUTF());
   }

   static {
      AIR = Material.AIR.createBlockData();
   }
}
