package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.PlayerDeleteShopkeeperEvent;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopType;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.event.ShopkeeperEventHelper;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.moving.ShopkeeperMoving;
import com.nisovin.shopkeepers.naming.ShopkeeperNaming;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.confirmations.ConfirmationUI;
import com.nisovin.shopkeepers.ui.confirmations.ConfirmationUIState;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperEditorLayout extends EditorLayout {
   private final AbstractShopkeeper shopkeeper;

   public ShopkeeperEditorLayout(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.shopkeeper = shopkeeper;
   }

   protected AbstractShopkeeper getShopkeeper() {
      return this.shopkeeper;
   }

   protected ItemStack createShopInformationIcon() {
      AbstractShopkeeper shopkeeper = this.getShopkeeper();
      String itemName = Messages.shopInformationHeader;
      List<String> itemLore = shopkeeper.getInformation();
      TextUtils.wrap(itemLore, 32);
      return ItemUtils.setDisplayNameAndLore(Settings.shopInformationItem.createItemStack(), itemName, itemLore);
   }

   protected ItemStack createTradeSetupIcon() {
      ShopType<?> shopType = this.getShopkeeper().getType();
      String itemName = StringUtils.replaceArguments(Messages.tradeSetupDescHeader, "shopType", shopType.getDisplayName());
      List<? extends String> itemLore = shopType.getTradeSetupDescription();
      return ItemUtils.setDisplayNameAndLore(Settings.tradeSetupItem.createItemStack(), itemName, itemLore);
   }

   protected void setupShopkeeperButtons() {
      this.addButtonOrIgnore(this.createDeleteButton());
      this.addButtonOrIgnore(this.createOpenButton());
      this.addButtonOrIgnore(this.createNamingButton());
      this.addButtonOrIgnore(this.createMoveButton());
   }

   protected void setupShopObjectButtons() {
      this.addButtons(this.shopkeeper.getShopObject().createEditorButtons());
   }

   protected Button createDeleteButton() {
      return new ActionButton(true) {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.deleteButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            UIState capturedUIState = editorView.captureState();
            editorView.closeDelayedAndRunTask(() -> {
               ShopkeeperEditorLayout.this.requestConfirmationDeleteShop(editorView.getPlayer(), capturedUIState);
            });
            return true;
         }
      };
   }

   private void requestConfirmationDeleteShop(Player player, UIState previousUIState) {
      ConfirmationUIState config = new ConfirmationUIState(Messages.confirmationUiDeleteShopTitle, Messages.confirmationUiDeleteShopConfirmLore, () -> {
         if (player.isValid()) {
            if (!this.shopkeeper.isValid()) {
               TextUtils.sendMessage(player, (Text)Messages.shopAlreadyRemoved);
            } else {
               PlayerDeleteShopkeeperEvent deleteEvent = ShopkeeperEventHelper.callPlayerDeleteShopkeeperEvent(this.shopkeeper, player);
               Bukkit.getPluginManager().callEvent(deleteEvent);
               if (!deleteEvent.isCancelled()) {
                  this.shopkeeper.delete(player);
                  this.shopkeeper.save();
                  TextUtils.sendMessage(player, (Text)Messages.shopRemoved);
               }

            }
         }
      }, () -> {
         if (player.isValid()) {
            if (this.shopkeeper.isValid()) {
               this.shopkeeper.openWindow(DefaultUITypes.EDITOR(), player, previousUIState);
            }
         }
      });
      ConfirmationUI.requestConfirmation(player, config);
   }

   protected Button createOpenButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ShopkeeperEditorLayout.this.shopkeeper.isOpen() ? Settings.DerivedSettings.shopOpenButtonItem.createItemStack() : Settings.DerivedSettings.shopClosedButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean newState = !ShopkeeperEditorLayout.this.shopkeeper.isOpen();
            ShopkeeperEditorLayout.this.shopkeeper.setOpen(newState);
            return true;
         }
      };
   }

   @Nullable
   protected Button createNamingButton() {
      boolean useNamingButton = true;
      if (this.shopkeeper.getType() instanceof PlayerShopType) {
         if (Settings.namingOfPlayerShopsViaItem) {
            useNamingButton = false;
         } else if (!Settings.allowRenamingOfPlayerNpcShops && this.shopkeeper.getShopObject().getType() == DefaultShopObjectTypes.CITIZEN()) {
            useNamingButton = false;
         }
      }

      return !useNamingButton ? null : new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.nameButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayed();
            Player player = editorView.getPlayer();
            ShopkeeperNaming shopkeeperNaming = SKShopkeepersPlugin.getInstance().getShopkeeperNaming();
            shopkeeperNaming.startNaming(player, ShopkeeperEditorLayout.this.shopkeeper);
            TextUtils.sendMessage(player, (Text)Messages.typeNewName);
            return true;
         }
      };
   }

   @Nullable
   protected Button createMoveButton() {
      return this.shopkeeper.getType() instanceof PlayerShopType && !Settings.enableMovingOfPlayerShops ? null : new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.moveButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayed();
            Player player = editorView.getPlayer();
            ShopkeeperMoving shopkeeperMoving = SKShopkeepersPlugin.getInstance().getShopkeeperMoving();
            shopkeeperMoving.startMoving(player, ShopkeeperEditorLayout.this.shopkeeper);
            TextUtils.sendMessage(player, (Text)Messages.clickNewShopLocation);
            return true;
         }
      };
   }
}
