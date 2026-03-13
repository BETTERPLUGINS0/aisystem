package com.nisovin.shopkeepers.ui.hiring;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.AbstractShopkeeperViewProvider;
import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;

public abstract class HiringViewProvider extends AbstractShopkeeperViewProvider {
   protected HiringViewProvider(AbstractUIType uiType, AbstractShopkeeper shopkeeper) {
      super(uiType, shopkeeper);
   }

   public boolean canAccess(Player player, boolean silent) {
      Validate.notNull(player, (String)"player is null");
      if (!PermissionUtils.hasPermission(player, "shopkeeper.hire")) {
         if (!silent) {
            this.debugNotOpeningUI(player, "Player is missing the hire permission.");
            TextUtils.sendMessage(player, (Text)Messages.missingHirePerm);
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean canOpen(Player player, boolean silent) {
      Validate.notNull(player, (String)"player is null");
      if (!super.canOpen(player, silent)) {
         return false;
      } else {
         AbstractShopkeeper shopkeeper = this.getShopkeeper();
         if (!shopkeeper.isOpen()) {
            if (!silent) {
               this.debugNotOpeningUI(player, "Shopkeeper is closed.");
               TextUtils.sendMessage(player, (Text)Messages.shopCurrentlyClosed);
            }

            return false;
         } else {
            return true;
         }
      }
   }
}
