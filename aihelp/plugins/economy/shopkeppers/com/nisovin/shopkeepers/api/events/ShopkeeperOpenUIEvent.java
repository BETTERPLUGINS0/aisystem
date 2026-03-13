package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.ui.UIType;
import org.bukkit.entity.Player;

public class ShopkeeperOpenUIEvent extends PlayerOpenUIEvent {
   private final Shopkeeper shopkeeper;

   public ShopkeeperOpenUIEvent(Shopkeeper shopkeeper, UIType uiType, Player player, boolean silentRequest) {
      super(uiType, player, silentRequest);
      Preconditions.checkNotNull(shopkeeper, "shopkeeper is null");
      this.shopkeeper = shopkeeper;
   }

   public Shopkeeper getShopkeeper() {
      return this.shopkeeper;
   }
}
