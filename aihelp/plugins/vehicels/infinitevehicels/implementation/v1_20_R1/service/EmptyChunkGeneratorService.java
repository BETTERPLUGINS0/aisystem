package implementation.v1_20_R1.service;

import java.util.Random;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class EmptyChunkGeneratorService implements advancedplugins.pm2.cv.api.service.EmptyChunkGeneratorService {
   public ChunkGenerator getNewEmptyChunkGenerator() {
      return new ChunkGenerator() {
         public int getBaseHeight(@NotNull WorldInfo var1, @NotNull Random var2, int var3, int var4, @NotNull HeightMap var5) {
            return var1.getMinHeight();
         }

         public Location getFixedSpawnLocation(@NotNull World var1, @NotNull Random var2) {
            return new Location(var1, 0.0D, 0.0D, 0.0D);
         }
      };
   }
}
