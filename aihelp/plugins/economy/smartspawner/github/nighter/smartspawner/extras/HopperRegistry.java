package github.nighter.smartspawner.extras;

import github.nighter.smartspawner.utils.BlockPos;
import github.nighter.smartspawner.utils.ChunkUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import org.bukkit.World;

public final class HopperRegistry {
   private final Map<UUID, Map<Long, Set<BlockPos>>> data = new ConcurrentHashMap();

   public void add(BlockPos pos) {
      UUID worldId = pos.worldId();
      long chunkKey = ChunkUtil.getChunkKey(pos.getChunkX(), pos.getChunkZ());
      ((Set)((Map)this.data.computeIfAbsent(worldId, (w) -> {
         return new ConcurrentHashMap();
      })).computeIfAbsent(chunkKey, (c) -> {
         return ConcurrentHashMap.newKeySet();
      })).add(pos);
   }

   public void remove(BlockPos pos) {
      UUID worldId = pos.worldId();
      Map<Long, Set<BlockPos>> worldMap = (Map)this.data.get(worldId);
      if (worldMap != null) {
         long chunkKey = ChunkUtil.getChunkKey(pos.getChunkX(), pos.getChunkZ());
         Set<BlockPos> set = (Set)worldMap.get(chunkKey);
         if (set != null) {
            set.remove(pos);
            if (set.isEmpty()) {
               worldMap.remove(chunkKey);
            }

            if (worldMap.isEmpty()) {
               this.data.remove(worldId);
            }

         }
      }
   }

   public void removeChunk(UUID worldId, int chunkX, int chunkZ) {
      Map<Long, Set<BlockPos>> worldMap = (Map)this.data.get(worldId);
      if (worldMap != null) {
         long key = ChunkUtil.getChunkKey(chunkX, chunkZ);
         worldMap.remove(key);
         if (worldMap.isEmpty()) {
            this.data.remove(worldId);
         }

      }
   }

   public void removeWorld(World world) {
      this.removeWorld(world.getUID());
   }

   public void removeWorld(UUID worldId) {
      this.data.remove(worldId);
   }

   public void forEachChunk(BiConsumer<UUID, Long> consumer) {
      Iterator var2 = this.data.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<UUID, Map<Long, Set<BlockPos>>> worldEntry = (Entry)var2.next();
         UUID worldId = (UUID)worldEntry.getKey();
         Iterator var5 = ((Map)worldEntry.getValue()).keySet().iterator();

         while(var5.hasNext()) {
            Long chunkKey = (Long)var5.next();
            consumer.accept(worldId, chunkKey);
         }
      }

   }

   public Set<BlockPos> getChunkHoppers(UUID worldId, long chunkKey) {
      Map<Long, Set<BlockPos>> worldMap = (Map)this.data.get(worldId);
      return worldMap == null ? Collections.emptySet() : (Set)worldMap.getOrDefault(chunkKey, Collections.emptySet());
   }
}
