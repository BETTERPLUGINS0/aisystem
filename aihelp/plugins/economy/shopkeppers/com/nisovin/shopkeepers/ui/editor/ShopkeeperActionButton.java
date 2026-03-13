package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.api.events.ShopkeeperEditedEvent;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ShopkeeperActionButton extends ActionButton {
   public ShopkeeperActionButton() {
      this(false);
   }

   public ShopkeeperActionButton(boolean placeAtEnd) {
      super(placeAtEnd);
   }

   protected boolean isApplicable(EditorLayout editorLayout) {
      return super.isApplicable(editorLayout) && editorLayout instanceof ShopkeeperEditorLayout;
   }

   protected Shopkeeper getShopkeeper() {
      EditorLayout layout = this.getEditorLayout();
      Validate.State.notNull(layout, (String)"Button was not yet added to any editor layout!");

      assert layout instanceof ShopkeeperEditorLayout;

      return ((ShopkeeperEditorLayout)layout).getShopkeeper();
   }

   protected void onActionSuccess(EditorView editorView, InventoryClickEvent clickEvent) {
      Shopkeeper shopkeeper = this.getShopkeeper();
      Player player = editorView.getPlayer();
      Bukkit.getPluginManager().callEvent(new ShopkeeperEditedEvent(shopkeeper, player));
      shopkeeper.save();
   }
}
