package com.nisovin.shopkeepers.ui;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.ui.UIRegistry;
import com.nisovin.shopkeepers.api.ui.UIType;
import com.nisovin.shopkeepers.types.AbstractTypeRegistry;
import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKUIRegistry extends AbstractTypeRegistry<AbstractUIType> implements UIRegistry<AbstractUIType> {
   protected String getTypeName() {
      return "UI type";
   }

   public Collection<? extends View> getUISessions() {
      return UISessionManager.getInstance().getUISessions();
   }

   public Collection<? extends View> getUISessions(Shopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      return UISessionManager.getInstance().getUISessionsForContext(shopkeeper);
   }

   public Collection<? extends View> getUISessions(Shopkeeper shopkeeper, UIType uiType) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      return UISessionManager.getInstance().getUISessionsForContext(shopkeeper, uiType);
   }

   public Collection<? extends View> getUISessions(UIType uiType) {
      return UISessionManager.getInstance().getUISessions(uiType);
   }

   @Nullable
   public View getUISession(Player player) {
      return UISessionManager.getInstance().getUISession(player);
   }

   public void abortUISessions() {
      UISessionManager.getInstance().abortUISessions();
   }

   public void abortUISessions(Shopkeeper shopkeeper) {
      UISessionManager.getInstance().abortUISessionsForContext(shopkeeper);
   }

   public void abortUISessionsDelayed(Shopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      UISessionManager.getInstance().abortUISessionsForContextDelayed(shopkeeper);
   }
}
