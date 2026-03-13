package com.nisovin.shopkeepers.ui.editor;

import com.nisovin.shopkeepers.util.bukkit.SoundEffect;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class Button {
   protected static final SoundEffect DEFAULT_BUTTON_CLICK_SOUND;
   static final int NO_SLOT = -1;
   private final boolean placeAtEnd;
   @Nullable
   private EditorLayout editorLayout;
   private int slot;

   public Button() {
      this(false);
   }

   public Button(boolean placeAtEnd) {
      this.slot = -1;
      this.placeAtEnd = placeAtEnd;
   }

   void setEditorLayout(EditorLayout editorLayout) {
      if (this.editorLayout != null) {
         throw new IllegalStateException("Button was already added to some editor layout!");
      } else {
         this.editorLayout = editorLayout;
      }
   }

   boolean isPlaceAtEnd() {
      return this.placeAtEnd;
   }

   int getSlot() {
      return this.slot;
   }

   void setSlot(int slot) {
      this.slot = slot;
   }

   protected boolean isApplicable(EditorLayout editorLayout) {
      return true;
   }

   @Nullable
   protected EditorLayout getEditorLayout() {
      return this.editorLayout;
   }

   @Nullable
   public abstract ItemStack getIcon(EditorView var1);

   protected final void updateIcon(EditorView editorView) {
      if (this.slot != -1 && this.editorLayout != null) {
         editorView.updateSlotInAllViews(this.slot);
      }

   }

   protected final void updateAllIcons(EditorView editorView) {
      if (this.editorLayout != null) {
         editorView.updateButtonsInAllViews();
      }

   }

   protected abstract void onClick(EditorView var1, InventoryClickEvent var2);

   static {
      DEFAULT_BUTTON_CLICK_SOUND = (new SoundEffect(Sound.UI_BUTTON_CLICK)).withVolume(0.25F);
   }
}
