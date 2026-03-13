package com.nisovin.shopkeepers.api.shopkeeper;

import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.api.ui.UISession;
import com.nisovin.shopkeepers.api.ui.UIType;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Shopkeeper {
   void save();

   void saveDelayed();

   int updateItems();

   boolean isValid();

   void delete();

   void delete(@Nullable Player var1);

   int getId();

   UUID getUniqueId();

   String getIdString();

   String getLogPrefix();

   String getUniqueIdLogPrefix();

   String getLocatedLogPrefix();

   String getDisplayName();

   ShopType<?> getType();

   boolean isVirtual();

   @Nullable
   String getWorldName();

   int getX();

   int getY();

   int getZ();

   float getYaw();

   String getPositionString();

   @Nullable
   Location getLocation();

   @Nullable
   ChunkCoords getChunkCoords();

   boolean isOpen();

   void setOpen(boolean var1);

   String getName();

   void setName(@Nullable String var1);

   ShopObject getShopObject();

   List<? extends ShopkeeperSnapshot> getSnapshots();

   ShopkeeperSnapshot getSnapshot(int var1);

   int getSnapshotIndex(String var1);

   @Nullable
   ShopkeeperSnapshot getSnapshot(String var1);

   ShopkeeperSnapshot createSnapshot(String var1);

   void addSnapshot(ShopkeeperSnapshot var1);

   ShopkeeperSnapshot removeSnapshot(int var1);

   void removeAllSnapshots();

   void applySnapshot(ShopkeeperSnapshot var1) throws ShopkeeperLoadException;

   boolean hasTradingRecipes(@Nullable Player var1);

   List<? extends TradingRecipe> getTradingRecipes(@Nullable Player var1);

   Collection<? extends UISession> getUISessions();

   Collection<? extends UISession> getUISessions(UIType var1);

   void abortUISessionsDelayed();

   boolean openWindow(UIType var1, Player var2);

   boolean openEditorWindow(Player var1);

   boolean openTradingWindow(Player var1);
}
