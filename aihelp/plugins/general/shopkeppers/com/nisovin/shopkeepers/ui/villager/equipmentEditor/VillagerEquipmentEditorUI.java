package com.nisovin.shopkeepers.ui.villager.equipmentEditor;

import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;

public class VillagerEquipmentEditorUI {
   public static boolean request(AbstractVillager entity, Player player) {
      Validate.notNull(entity, (String)"entity is null");
      Validate.notNull(player, (String)"player is null");
      VillagerEquipmentEditorViewProvider viewProvider = new VillagerEquipmentEditorViewProvider(entity);
      VillagerEquipmentEditorUIState config = new VillagerEquipmentEditorUIState(entity);
      return UISessionManager.getInstance().requestUI(viewProvider, player, config);
   }

   private VillagerEquipmentEditorUI() {
   }
}
