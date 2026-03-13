package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.shopobjects.living.types.villager.WanderingTraderSounds;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import com.nisovin.shopkeepers.ui.trading.TradingViewProvider;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import java.util.List;
import org.bukkit.entity.WanderingTrader;
import org.checkerframework.checker.nullness.qual.Nullable;

public class WanderingTraderShop extends BabyableShop<WanderingTrader> {
   private final WanderingTraderSounds wanderingTraderSounds = new WanderingTraderSounds((SKLivingShopObject)Unsafe.initialized(this));

   public WanderingTraderShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<WanderingTraderShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
   }

   public void setup() {
      super.setup();
      if (Settings.simulateWanderingTraderTradingSounds) {
         ViewProvider viewProvider = this.shopkeeper.getViewProvider(DefaultUITypes.TRADING());
         if (viewProvider instanceof TradingViewProvider) {
            TradingViewProvider tradingViewProvider = (TradingViewProvider)viewProvider;
            tradingViewProvider.addListener(this.wanderingTraderSounds);
         }
      }

   }

   protected void onSpawn() {
      super.onSpawn();
      WanderingTrader entity = (WanderingTrader)Unsafe.assertNonNull((WanderingTrader)this.getEntity());
      if (Settings.simulateWanderingTraderTradingSounds || Settings.simulateWanderingTraderAmbientSounds) {
         entity.setSilent(true);
      }

      entity.setDespawnDelay(0);
   }

   public void onTick() {
      super.onTick();
      if (Settings.simulateWanderingTraderAmbientSounds) {
         this.wanderingTraderSounds.tick();
      }

   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      return editorButtons;
   }
}
