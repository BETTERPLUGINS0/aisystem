package com.nisovin.shopkeepers.api.shopkeeper;

import com.nisovin.shopkeepers.api.types.SelectableType;
import java.util.List;

public interface ShopType<T extends Shopkeeper> extends SelectableType {
   String getDisplayName();

   String getDescription();

   String getSetupDescription();

   List<? extends String> getTradeSetupDescription();
}
