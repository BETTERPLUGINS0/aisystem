package com.nisovin.shopkeepers.api.shopobjects;

import com.nisovin.shopkeepers.api.types.SelectableType;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ShopObjectType<T extends ShopObject> extends SelectableType {
   String getDisplayName();

   boolean isValidSpawnLocation(@Nullable Location var1, @Nullable BlockFace var2);
}
