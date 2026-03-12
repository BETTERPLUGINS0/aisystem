package com.nisovin.shopkeepers.shopkeeper.player.sell;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopType;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Arrays;
import java.util.List;

public final class SellingPlayerShopType extends AbstractPlayerShopType<SKSellingPlayerShopkeeper> {
   public SellingPlayerShopType() {
      super("sell", Arrays.asList("selling", "normal", "player"), "shopkeeper.player.sell", SKSellingPlayerShopkeeper.class);
   }

   public String getDisplayName() {
      return Messages.shopTypeSelling;
   }

   public String getDescription() {
      return Messages.shopTypeDescSelling;
   }

   public String getSetupDescription() {
      return Messages.shopSetupDescSelling;
   }

   public List<? extends String> getTradeSetupDescription() {
      return Messages.tradeSetupDescSelling;
   }

   protected SKSellingPlayerShopkeeper createNewShopkeeper() {
      return new SKSellingPlayerShopkeeper();
   }

   static {
      ShopkeeperDataMigrator.registerMigration(new Migration("shop-type-player-to-sell", MigrationPhase.EARLY) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            String shopTypeId = (String)shopkeeperData.getOrNullIfMissing(AbstractShopkeeper.SHOP_TYPE_ID);
            if (shopTypeId == null) {
               return false;
            } else {
               boolean migrated = false;
               if (shopTypeId.equalsIgnoreCase("player")) {
                  Log.info(logPrefix + "Migrating shop type from 'player' to 'sell'.");
                  shopkeeperData.set(AbstractShopkeeper.SHOP_TYPE_ID, "sell");
                  migrated = true;
               }

               return migrated;
            }
         }
      });
   }
}
