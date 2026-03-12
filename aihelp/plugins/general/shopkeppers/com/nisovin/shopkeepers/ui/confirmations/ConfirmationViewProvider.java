package com.nisovin.shopkeepers.ui.confirmations;

import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.lib.SimpleViewContext;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.ui.lib.ViewContext;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfirmationViewProvider extends ViewProvider {
   private static final ViewContext VIEW_CONTEXT = new SimpleViewContext("confirmation");

   ConfirmationViewProvider() {
      super(SKDefaultUITypes.CONFIRMATION(), VIEW_CONTEXT);
   }

   public boolean canAccess(Player player, boolean silent) {
      return true;
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new ConfirmationView(this, player, uiState);
   }
}
