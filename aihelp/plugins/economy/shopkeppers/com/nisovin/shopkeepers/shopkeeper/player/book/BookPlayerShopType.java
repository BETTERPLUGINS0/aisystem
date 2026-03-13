package com.nisovin.shopkeepers.shopkeeper.player.book;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopType;
import java.util.Collections;
import java.util.List;

public final class BookPlayerShopType extends AbstractPlayerShopType<SKBookPlayerShopkeeper> {
   public BookPlayerShopType() {
      super("book", Collections.emptyList(), "shopkeeper.player.book", SKBookPlayerShopkeeper.class);
   }

   public String getDisplayName() {
      return Messages.shopTypeBook;
   }

   public String getDescription() {
      return Messages.shopTypeDescBook;
   }

   public String getSetupDescription() {
      return Messages.shopSetupDescBook;
   }

   public List<? extends String> getTradeSetupDescription() {
      return Messages.tradeSetupDescBook;
   }

   protected SKBookPlayerShopkeeper createNewShopkeeper() {
      return new SKBookPlayerShopkeeper();
   }
}
