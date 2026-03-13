package com.nisovin.shopkeepers.ui.villager.equipmentEditor;

import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.ui.villager.AbstractVillagerViewProvider;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class VillagerEquipmentEditorViewProvider extends AbstractVillagerViewProvider {
   VillagerEquipmentEditorViewProvider(AbstractVillager villager) {
      super(SKDefaultUITypes.VILLAGER_EQUIPMENT_EDITOR(), villager);
   }

   public boolean canAccess(Player player, boolean silent) {
      return true;
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new VillagerEquipmentEditorView(this, player, uiState);
   }
}
