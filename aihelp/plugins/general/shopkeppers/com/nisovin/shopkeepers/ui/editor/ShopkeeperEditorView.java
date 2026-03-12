package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.api.events.ShopkeeperEditedEvent;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class ShopkeeperEditorView extends EditorView {
   protected ShopkeeperEditorView(ShopkeeperEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   protected String getTitle() {
      return Messages.editorTitle;
   }

   protected void saveRecipes() {
      Player player = this.getPlayer();
      AbstractShopkeeper shopkeeper = this.getShopkeeperNonNull();

      assert shopkeeper.isValid();

      int changedOffers = this.getTradingRecipesAdapter().updateTradingRecipes(player, this.getRecipes());
      if (changedOffers == 0) {
         Log.debug(() -> {
            return this.getContext().getLogPrefix() + "No offers have changed.";
         });
      } else {
         Log.debug(() -> {
            String var10000 = this.getContext().getLogPrefix();
            return var10000 + changedOffers + " offers have changed.";
         });
         Bukkit.getPluginManager().callEvent(new ShopkeeperEditedEvent(shopkeeper, player));
      }

      if (shopkeeper.isDirty()) {
         shopkeeper.save();
      }

   }
}
