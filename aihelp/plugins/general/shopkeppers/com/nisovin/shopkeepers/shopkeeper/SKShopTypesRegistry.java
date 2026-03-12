package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.api.shopkeeper.ShopTypesRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopType;
import com.nisovin.shopkeepers.types.AbstractSelectableType;
import com.nisovin.shopkeepers.types.AbstractSelectableTypeRegistry;
import org.bukkit.entity.Player;

public class SKShopTypesRegistry extends AbstractSelectableTypeRegistry<AbstractShopType<?>> implements ShopTypesRegistry<AbstractShopType<?>> {
   protected String getTypeName() {
      return "shop type";
   }

   public boolean canBeSelected(Player player, AbstractShopType<?> type) {
      return super.canBeSelected(player, (AbstractSelectableType)type) && type instanceof PlayerShopType;
   }
}
