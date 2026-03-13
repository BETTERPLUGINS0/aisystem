package com.nisovin.shopkeepers.api.shopkeeper;

import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShopkeeperRegistry {
   Shopkeeper createShopkeeper(ShopCreationData var1) throws ShopkeeperCreateException;

   Collection<? extends Shopkeeper> getAllShopkeepers();

   Collection<? extends Shopkeeper> getVirtualShopkeepers();

   @Nullable
   Shopkeeper getShopkeeperByUniqueId(UUID var1);

   @Nullable
   Shopkeeper getShopkeeperById(int var1);

   Collection<? extends PlayerShopkeeper> getAllPlayerShopkeepers();

   Collection<? extends PlayerShopkeeper> getPlayerShopkeepersByOwner(UUID var1);

   Stream<? extends Shopkeeper> getShopkeepersByName(String var1);

   Stream<? extends Shopkeeper> getShopkeepersByNamePrefix(String var1);

   Collection<? extends String> getWorldsWithShopkeepers();

   Collection<? extends Shopkeeper> getShopkeepersInWorld(String var1);

   Map<? extends ChunkCoords, ? extends Collection<? extends Shopkeeper>> getShopkeepersByChunks(String var1);

   Collection<? extends ChunkCoords> getActiveChunks(String var1);

   boolean isChunkActive(ChunkCoords var1);

   Collection<? extends Shopkeeper> getActiveShopkeepers();

   Collection<? extends Shopkeeper> getActiveShopkeepers(String var1);

   Collection<? extends Shopkeeper> getShopkeepersInChunk(ChunkCoords var1);

   Collection<? extends Shopkeeper> getShopkeepersAtLocation(Location var1);

   @Nullable
   Shopkeeper getShopkeeperByEntity(Entity var1);

   boolean isShopkeeper(Entity var1);

   @Nullable
   Shopkeeper getShopkeeperByBlock(Block var1);

   @Nullable
   Shopkeeper getShopkeeperByBlock(String var1, int var2, int var3, int var4);

   boolean isShopkeeper(Block var1);
}
