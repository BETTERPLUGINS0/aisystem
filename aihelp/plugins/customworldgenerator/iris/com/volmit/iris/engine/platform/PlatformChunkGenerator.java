package com.volmit.iris.engine.platform;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineTarget;
import com.volmit.iris.engine.framework.Hotloadable;
import com.volmit.iris.util.data.DataProvider;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlatformChunkGenerator extends Hotloadable, DataProvider {
   @Nullable
   Engine getEngine();

   default IrisData getData() {
      return this.getTarget().getData();
   }

   @NotNull
   EngineTarget getTarget();

   void injectChunkReplacement(World world, int x, int z, Executor syncExecutor);

   void close();

   boolean isStudio();

   void touch(World world);

   CompletableFuture<Integer> getSpawnChunks();
}
