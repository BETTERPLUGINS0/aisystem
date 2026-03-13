package com.nisovin.shopkeepers.ui.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ActionButton extends Button {
   public ActionButton() {
   }

   public ActionButton(boolean placeAtEnd) {
      super(placeAtEnd);
   }

   protected void playButtonClickSound(Player player, boolean actionSuccess) {
      DEFAULT_BUTTON_CLICK_SOUND.play(player);
   }

   protected final void onClick(EditorView editorView, InventoryClickEvent clickEvent) {
      if (clickEvent.getClick() != ClickType.DOUBLE_CLICK) {
         boolean success = this.runAction(editorView, clickEvent);
         if (success) {
            this.onActionSuccess(editorView, clickEvent);
            this.playButtonClickSound(editorView.getPlayer(), success);
            this.updateIcon(editorView);
         }
      }
   }

   protected abstract boolean runAction(EditorView var1, InventoryClickEvent var2);

   protected void onActionSuccess(EditorView editorView, InventoryClickEvent clickEvent) {
   }
}
