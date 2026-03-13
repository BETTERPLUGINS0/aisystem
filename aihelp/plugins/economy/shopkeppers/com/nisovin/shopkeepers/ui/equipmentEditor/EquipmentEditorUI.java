package com.nisovin.shopkeepers.ui.equipmentEditor;

import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;

public class EquipmentEditorUI {
   public static boolean request(AbstractShopkeeper shopkeeper, Player player, EquipmentEditorUIState config) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(config, (String)"config is null");
      ShopkeeperEquipmentEditorViewProvider viewProvider = new ShopkeeperEquipmentEditorViewProvider(shopkeeper);
      return UISessionManager.getInstance().requestUI(viewProvider, player, config);
   }

   private EquipmentEditorUI() {
   }
}
