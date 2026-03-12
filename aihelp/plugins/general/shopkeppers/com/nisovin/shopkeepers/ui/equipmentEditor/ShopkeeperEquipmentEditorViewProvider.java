package com.nisovin.shopkeepers.ui.equipmentEditor;

import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.ui.AbstractShopkeeperViewProvider;
import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperEquipmentEditorViewProvider extends AbstractShopkeeperViewProvider {
   ShopkeeperEquipmentEditorViewProvider(AbstractShopkeeper shopkeeper) {
      super(SKDefaultUITypes.EQUIPMENT_EDITOR(), shopkeeper);
   }

   public boolean canAccess(Player player, boolean silent) {
      return true;
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new EquipmentEditorView(this, player, uiState);
   }
}
