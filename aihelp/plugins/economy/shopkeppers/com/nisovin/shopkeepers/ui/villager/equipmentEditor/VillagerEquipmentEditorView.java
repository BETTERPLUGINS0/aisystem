package com.nisovin.shopkeepers.ui.villager.equipmentEditor;

import com.nisovin.shopkeepers.ui.equipmentEditor.EquipmentEditorView;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import org.bukkit.entity.Player;

public class VillagerEquipmentEditorView extends EquipmentEditorView {
   VillagerEquipmentEditorView(ViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }
}
