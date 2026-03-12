package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ViewProvider {
   private final AbstractUIType uiType;
   private final ViewContext viewContext;

   protected ViewProvider(AbstractUIType uiType, ViewContext viewContext) {
      Validate.notNull(uiType, (String)"uiType is null");
      Validate.notNull(viewContext, (String)"viewContext is null");
      this.uiType = uiType;
      this.viewContext = viewContext;
   }

   public final AbstractUIType getUIType() {
      return this.uiType;
   }

   public ViewContext getContext() {
      return this.viewContext;
   }

   protected void debugNotOpeningUI(Player player, String reason) {
      Validate.notNull(player, (String)"player is null");
      Validate.notEmpty(reason, "reason is null or empty");
      Log.debug(() -> {
         String var10000 = this.getContext().getLogPrefix();
         return var10000 + "Not opening UI '" + this.getUIType().getIdentifier() + "' for player " + player.getName() + ": " + reason;
      });
   }

   public abstract boolean canAccess(Player var1, boolean var2);

   public boolean canOpen(Player player, boolean silent) {
      return this.canAccess(player, silent);
   }

   @Nullable
   protected abstract View createView(Player var1, UIState var2);
}
