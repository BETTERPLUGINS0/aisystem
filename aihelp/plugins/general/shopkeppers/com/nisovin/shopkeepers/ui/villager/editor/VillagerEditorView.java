package com.nisovin.shopkeepers.ui.villager.editor;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public final class VillagerEditorView extends EditorView {
   protected VillagerEditorView(VillagerEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   public AbstractVillager getVillager() {
      return (AbstractVillager)this.getContext().getObject();
   }

   protected String getTitle() {
      AbstractVillager villager = this.getVillager();
      String villagerName = villager.getName();
      return StringUtils.replaceArguments(Messages.villagerEditorTitle, "villagerName", villagerName);
   }

   protected void saveRecipes() {
      Player player = this.getPlayer();
      AbstractVillager villager = this.getVillager();
      if (!this.abortIfContextInvalid()) {
         int changedTrades = this.getTradingRecipesAdapter().updateTradingRecipes(player, this.getRecipes());
         if (changedTrades == 0) {
            TextUtils.sendMessage(player, (String)Messages.noVillagerTradesChanged);
         } else {
            TextUtils.sendMessage(player, (String)Messages.villagerTradesChanged, (Object[])("changedTrades", changedTrades));
            if (villager instanceof Villager) {
               Villager regularVillager = (Villager)villager;
               if (regularVillager.getVillagerExperience() == 0) {
                  regularVillager.setVillagerExperience(1);
                  TextUtils.sendMessage(player, (String)Messages.setVillagerXp, (Object[])("xp", 1));
               }
            }

         }
      }
   }
}
