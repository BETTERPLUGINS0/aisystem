package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.ui.lib.ViewContext;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractEditorViewProvider extends ViewProvider {
   @Nullable
   private EditorLayout layout;
   protected final TradingRecipesAdapter tradingRecipesAdapter;

   protected AbstractEditorViewProvider(AbstractUIType uiType, ViewContext viewContext, TradingRecipesAdapter tradingRecipesAdapter) {
      super(uiType, viewContext);
      Validate.notNull(tradingRecipesAdapter, (String)"tradingRecipesAdapter is null");
      this.tradingRecipesAdapter = tradingRecipesAdapter;
   }

   public boolean canAccess(Player player, boolean silent) {
      Validate.notNull(player, (String)"player is null");
      return true;
   }

   protected final EditorLayout getLayout() {
      if (this.layout == null) {
         this.layout = this.createLayout();
         this.setupButtons();
      }

      assert this.layout != null;

      return this.layout;
   }

   protected abstract EditorLayout createLayout();

   protected void setupButtons() {
      EditorLayout layout = this.getLayout();
      layout.setupTradesPageBarButtons();
   }
}
