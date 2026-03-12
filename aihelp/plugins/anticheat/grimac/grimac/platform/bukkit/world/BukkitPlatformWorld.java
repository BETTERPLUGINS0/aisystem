package ac.grim.grimac.platform.bukkit.world;

import ac.grim.grimac.platform.api.world.PlatformChunk;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotConversionUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BukkitPlatformWorld implements PlatformWorld {
   private static final boolean LEGACY_SERVER_VERSION;
   @NotNull
   private final World bukkitWorld;

   public boolean isChunkLoaded(int chunkX, int chunkZ) {
      return this.bukkitWorld.isChunkLoaded(chunkX, chunkZ);
   }

   public WrappedBlockState getBlockAt(int x, int y, int z) {
      if (LEGACY_SERVER_VERSION) {
         Block block = this.bukkitWorld.getBlockAt(x, y, z);
         int blockId = block.getType().getId() << 4 | block.getData();
         return WrappedBlockState.getByGlobalId(blockId);
      } else {
         return SpigotConversionUtil.fromBukkitBlockData(this.bukkitWorld.getBlockAt(x, y, z).getBlockData());
      }
   }

   public String getName() {
      return this.bukkitWorld.getName();
   }

   @Nullable
   public UUID getUID() {
      return this.bukkitWorld.getUID();
   }

   public PlatformChunk getChunkAt(int currChunkX, int currChunkZ) {
      return new BukkitPlatformChunk(this.bukkitWorld.getChunkAt(currChunkX, currChunkZ));
   }

   public boolean isLoaded() {
      return Bukkit.getWorld(this.bukkitWorld.getUID()) != null;
   }

   @NotNull
   @Generated
   public World getBukkitWorld() {
      return this.bukkitWorld;
   }

   @Generated
   public BukkitPlatformWorld(@NotNull World bukkitWorld) {
      if (bukkitWorld == null) {
         throw new NullPointerException("bukkitWorld is marked non-null but is null");
      } else {
         this.bukkitWorld = bukkitWorld;
      }
   }

   static {
      LEGACY_SERVER_VERSION = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2);
   }
}
