package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.matter.MatterBiomeInject;
import com.volmit.iris.util.matter.Sliced;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.bukkit.block.Biome;

@Sliced
public class BiomeInjectMatter extends RawMatter<MatterBiomeInject> {
   public BiomeInjectMatter() {
      this(1, 1, 1);
   }

   public BiomeInjectMatter(int width, int height, int depth) {
      super(var1, var2, var3, MatterBiomeInject.class);
   }

   public static MatterBiomeInject get(Biome biome) {
      return get(false, 0, var0);
   }

   public static MatterBiomeInject get(int customBiome) {
      return get(true, var0, (Biome)null);
   }

   public static MatterBiomeInject get(boolean custom, int customBiome, Biome biome) {
      return new MatterBiomeInject(var0, var1, var2);
   }

   public Palette<MatterBiomeInject> getGlobalPalette() {
      return null;
   }

   public void writeNode(MatterBiomeInject b, DataOutputStream dos) {
      var2.writeBoolean(var1.isCustom());
      if (var1.isCustom()) {
         var2.writeShort(var1.getBiomeId());
      } else {
         var2.writeByte(var1.getBiome().ordinal());
      }

   }

   public MatterBiomeInject readNode(DataInputStream din) {
      boolean var2 = var1.readBoolean();
      short var3 = var2 ? var1.readShort() : 0;
      Biome var4 = !var2 ? Biome.values()[var1.readByte()] : Biome.PLAINS;
      return new MatterBiomeInject(var2, Integer.valueOf(var3), var4);
   }
}
