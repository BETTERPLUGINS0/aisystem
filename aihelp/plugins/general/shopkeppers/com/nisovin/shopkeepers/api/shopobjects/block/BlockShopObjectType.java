package com.nisovin.shopkeepers.api.shopobjects.block;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface BlockShopObjectType<T extends BlockShopObject> extends ShopObjectType<T> {
   @Nullable
   Shopkeeper getShopkeeper(Block var1);

   @Nullable
   Shopkeeper getShopkeeper(String var1, int var2, int var3, int var4);

   boolean isShopkeeper(Block var1);

   boolean isShopkeeper(String var1, int var2, int var3, int var4);
}
