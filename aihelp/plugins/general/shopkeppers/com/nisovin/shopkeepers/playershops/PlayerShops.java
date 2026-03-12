package com.nisovin.shopkeepers.playershops;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.playershops.inactivity.PlayerInactivity;
import com.nisovin.shopkeepers.util.java.Validate;

public class PlayerShops {
   private final PlayerShopsLimit playerShopsLimit;
   private final PlayerInactivity playerInactivity;
   private final ShopOwnerNameUpdates shopOwnerNameUpdates;

   public PlayerShops(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.playerShopsLimit = new PlayerShopsLimit();
      this.playerInactivity = new PlayerInactivity(plugin);
      this.shopOwnerNameUpdates = new ShopOwnerNameUpdates(plugin);
   }

   public void onEnable() {
      this.playerShopsLimit.onEnable();
      this.playerInactivity.onEnable();
      this.shopOwnerNameUpdates.onEnable();
   }

   public void onDisable() {
      this.playerShopsLimit.onDisable();
      this.playerInactivity.onDisable();
      this.shopOwnerNameUpdates.onDisable();
   }

   public PlayerShopsLimit getPlayerShopsLimit() {
      return this.playerShopsLimit;
   }

   public PlayerInactivity getPlayerInactivity() {
      return this.playerInactivity;
   }

   public ShopOwnerNameUpdates getShopOwnerNameUpdates() {
      return this.shopOwnerNameUpdates;
   }
}
