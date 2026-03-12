package com.nisovin.shopkeepers.shopcreation;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;

public class ShopkeeperCreation {
   private final ContainerSelection containerSelection;
   private final ShopkeeperPlacement shopkeeperPlacement;
   private final CreateListener createListener;

   public ShopkeeperCreation(SKShopkeepersPlugin plugin, ShopkeeperRegistry shopkeeperRegistry, ProtectedContainers protectedContainers) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.containerSelection = new ContainerSelection(plugin, protectedContainers);
      this.shopkeeperPlacement = new ShopkeeperPlacement(shopkeeperRegistry);
      this.createListener = new CreateListener(plugin, this.containerSelection, this.shopkeeperPlacement);
   }

   public void onEnable() {
      this.containerSelection.onEnable();
      this.createListener.onEnable();
   }

   public void onDisable() {
      this.createListener.onDisable();
      this.containerSelection.onDisable();
   }

   public void onPlayerQuit(Player player) {
      assert player != null;

      this.containerSelection.onPlayerQuit(player);
   }

   public ContainerSelection getContainerSelection() {
      return this.containerSelection;
   }

   public ShopkeeperPlacement getShopkeeperPlacement() {
      return this.shopkeeperPlacement;
   }
}
