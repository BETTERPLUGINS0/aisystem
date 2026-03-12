package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.ui.SKDefaultUITypes;
import com.nisovin.shopkeepers.ui.hiring.HiringViewProvider;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerShopHiringViewProvider extends HiringViewProvider {
   public PlayerShopHiringViewProvider(AbstractPlayerShopkeeper shopkeeper) {
      super(SKDefaultUITypes.HIRING(), shopkeeper);
   }

   public AbstractPlayerShopkeeper getShopkeeper() {
      return (AbstractPlayerShopkeeper)super.getShopkeeper();
   }

   public boolean canOpen(Player player, boolean silent) {
      if (!super.canOpen(player, silent)) {
         return false;
      } else if (!this.getShopkeeper().isForHire()) {
         if (!silent) {
            this.debugNotOpeningUI(player, "The shopkeeper is not for hire.");
         }

         return false;
      } else {
         return true;
      }
   }

   @Nullable
   protected View createView(Player player, UIState uiState) {
      return new PlayerShopHiringView(this, player, uiState);
   }
}
