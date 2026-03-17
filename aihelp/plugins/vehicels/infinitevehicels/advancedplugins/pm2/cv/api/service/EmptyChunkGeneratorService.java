package advancedplugins.pm2.cv.api.service;

import org.bukkit.generator.ChunkGenerator;

public interface EmptyChunkGeneratorService extends Service {
   ChunkGenerator getNewEmptyChunkGenerator();
}
