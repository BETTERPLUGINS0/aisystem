package com.nisovin.shopkeepers.api.shopobjects.block;

import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface BlockShopObject extends ShopObject {
   @Nullable
   Block getBlock();
}
