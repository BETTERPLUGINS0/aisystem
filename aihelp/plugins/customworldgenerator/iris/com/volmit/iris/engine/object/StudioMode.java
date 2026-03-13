package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.platform.BukkitChunkGenerator;
import com.volmit.iris.engine.platform.studio.StudioGenerator;
import com.volmit.iris.engine.platform.studio.generators.BiomeBuffetGenerator;
import java.util.function.Consumer;

@Desc("Represents a studio mode")
public enum StudioMode {
   NORMAL((var0) -> {
      var0.setStudioGenerator((StudioGenerator)null);
   }),
   BIOME_BUFFET_1x1((var0) -> {
      var0.setStudioGenerator(new BiomeBuffetGenerator(var0.getEngine(), 1));
   }),
   BIOME_BUFFET_3x3((var0) -> {
      var0.setStudioGenerator(new BiomeBuffetGenerator(var0.getEngine(), 3));
   }),
   BIOME_BUFFET_5x5((var0) -> {
      var0.setStudioGenerator(new BiomeBuffetGenerator(var0.getEngine(), 5));
   }),
   BIOME_BUFFET_9x9((var0) -> {
      var0.setStudioGenerator(new BiomeBuffetGenerator(var0.getEngine(), 9));
   }),
   BIOME_BUFFET_18x18((var0) -> {
      var0.setStudioGenerator(new BiomeBuffetGenerator(var0.getEngine(), 18));
   }),
   BIOME_BUFFET_36x36((var0) -> {
      var0.setStudioGenerator(new BiomeBuffetGenerator(var0.getEngine(), 36));
   }),
   REGION_BUFFET((var0) -> {
      var0.setStudioGenerator((StudioGenerator)null);
   }),
   OBJECT_BUFFET((var0) -> {
      var0.setStudioGenerator((StudioGenerator)null);
   });

   private final Consumer<BukkitChunkGenerator> injector;

   private StudioMode(Consumer<BukkitChunkGenerator> injector) {
      this.injector = var3;
   }

   public void inject(BukkitChunkGenerator c) {
      this.injector.accept(var1);
   }

   // $FF: synthetic method
   private static StudioMode[] $values() {
      return new StudioMode[]{NORMAL, BIOME_BUFFET_1x1, BIOME_BUFFET_3x3, BIOME_BUFFET_5x5, BIOME_BUFFET_9x9, BIOME_BUFFET_18x18, BIOME_BUFFET_36x36, REGION_BUFFET, OBJECT_BUFFET};
   }
}
