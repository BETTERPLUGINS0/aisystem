package ac.grim.grimac.platform.api.world;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.UUID;

public interface PlatformWorld {
   boolean isChunkLoaded(int var1, int var2);

   WrappedBlockState getBlockAt(int var1, int var2, int var3);

   String getName();

   @Nullable
   UUID getUID();

   PlatformChunk getChunkAt(int var1, int var2);

   boolean isLoaded();
}
