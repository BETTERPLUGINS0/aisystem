package com.nisovin.shopkeepers.ui.confirmations;

import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ConfirmationUI {
   @Nullable
   private static ConfirmationViewProvider VIEW_PROVIDER;

   private static ConfirmationViewProvider getViewProvider() {
      if (VIEW_PROVIDER == null) {
         VIEW_PROVIDER = new ConfirmationViewProvider();
      }

      assert VIEW_PROVIDER != null;

      return VIEW_PROVIDER;
   }

   public static void requestConfirmation(Player player, ConfirmationUIState config) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(config, (String)"config is null");
      UISessionManager.getInstance().requestUI(getViewProvider(), player, config);
   }

   private ConfirmationUI() {
   }
}
