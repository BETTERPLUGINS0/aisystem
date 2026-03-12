package com.nisovin.shopkeepers.shopobjects.living.types.villager;

import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.Sound;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class WanderingTraderSounds extends VillagerSounds {
   public WanderingTraderSounds(SKLivingShopObject<? extends WanderingTrader> shopObject) {
      super(shopObject);
   }

   protected Sound getAmbientSound() {
      return this.isShopkeeperTrading() ? Sound.ENTITY_WANDERING_TRADER_TRADE : Sound.ENTITY_WANDERING_TRADER_AMBIENT;
   }

   protected Sound getTradeSound() {
      return Sound.ENTITY_WANDERING_TRADER_YES;
   }

   protected Sound getTradeInteractionSound(@Nullable ItemStack resultItem) {
      return ItemUtils.isEmpty(resultItem) ? Sound.ENTITY_WANDERING_TRADER_NO : Sound.ENTITY_WANDERING_TRADER_YES;
   }
}
