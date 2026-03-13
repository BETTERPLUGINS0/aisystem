package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.ui.editor.ActionButton;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperEditorLayout;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerShopEditorLayout extends ShopkeeperEditorLayout {
   public PlayerShopEditorLayout(AbstractPlayerShopkeeper shopkeeper) {
      super(shopkeeper);
   }

   protected AbstractPlayerShopkeeper getShopkeeper() {
      return (AbstractPlayerShopkeeper)super.getShopkeeper();
   }

   protected void setupShopkeeperButtons() {
      super.setupShopkeeperButtons();
      this.addButtonOrIgnore(this.createContainerButton());
      this.addButtonOrIgnore(this.createTradeNotificationsButton());
   }

   @Nullable
   protected Button createContainerButton() {
      return !Settings.enableContainerOptionOnPlayerShop ? null : new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.containerButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayedAndRunTask(() -> {
               Player player = editorView.getPlayer();
               PlayerShopkeeper shopkeeper = PlayerShopEditorLayout.this.getShopkeeper();
               if (player.isValid() && shopkeeper.isValid()) {
                  shopkeeper.openContainerWindow(player);
               }
            });
            return true;
         }
      };
   }

   @Nullable
   protected Button createTradeNotificationsButton() {
      return !Settings.notifyShopOwnersAboutTrades ? null : new ShopkeeperActionButton(this) {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            AbstractPlayerShopkeeper shopkeeper = (AbstractPlayerShopkeeper)this.getShopkeeper();
            ItemStack iconItem = Settings.tradeNotificationsItem.createItemStack();
            String state = shopkeeper.isNotifyOnTrades() ? Messages.stateEnabled : Messages.stateDisabled;
            String displayName = StringUtils.replaceArguments(Messages.buttonTradeNotifications, "state", state);
            List<? extends String> lore = StringUtils.replaceArguments((Collection)Messages.buttonTradeNotificationsLore, (Object[])("state", state));
            ItemUtils.setDisplayNameAndLore(iconItem, displayName, lore);
            return iconItem;
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            AbstractPlayerShopkeeper shopkeeper = (AbstractPlayerShopkeeper)this.getShopkeeper();
            shopkeeper.setNotifyOnTrades(!shopkeeper.isNotifyOnTrades());
            return true;
         }
      };
   }
}
