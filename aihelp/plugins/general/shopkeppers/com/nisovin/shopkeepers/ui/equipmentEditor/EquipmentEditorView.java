package com.nisovin.shopkeepers.ui.equipmentEditor;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.player.PlaceholderItems;
import com.nisovin.shopkeepers.ui.UIHelpers;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.lib.View;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.inventory.ChestLayout;
import com.nisovin.shopkeepers.util.inventory.InventoryViewUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EquipmentEditorView extends View {
   protected EquipmentEditorView(ViewProvider provider, Player player, UIState uiState) {
      super(provider, player, uiState);
   }

   public boolean isAcceptedState(UIState uiState) {
      return uiState instanceof EquipmentEditorUIState;
   }

   protected EquipmentEditorUIState getConfig() {
      return (EquipmentEditorUIState)this.getInitialUIState();
   }

   @Nullable
   protected InventoryView openInventoryView() {
      EquipmentEditorUIState config = this.getConfig();
      List<? extends EquipmentSlot> supportedSlots = config.getSupportedSlots();
      int inventorySize = ChestLayout.getRequiredSlots(supportedSlots.size());
      Inventory inventory = Bukkit.createInventory((InventoryHolder)null, inventorySize, Messages.equipmentEditorTitle);
      this.updateInventory(inventory);
      Player player = this.getPlayer();
      return player.openInventory(inventory);
   }

   private void updateInventory(Inventory inventory) {
      EquipmentEditorUIState config = this.getConfig();
      List<? extends EquipmentSlot> supportedSlots = config.getSupportedSlots();
      Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> currentEquipment = config.getCurrentEquipment();

      for(int slotIndex = 0; slotIndex < supportedSlots.size(); ++slotIndex) {
         EquipmentSlot equipmentSlot = (EquipmentSlot)supportedSlots.get(slotIndex);
         if (slotIndex >= inventory.getSize()) {
            break;
         }

         UnmodifiableItemStack equipmentItem = (UnmodifiableItemStack)currentEquipment.get(equipmentSlot);
         ItemStack editorItem = this.toEditorEquipmentItem(equipmentSlot, ItemUtils.asItemStackOrNull(equipmentItem));
         inventory.setItem(slotIndex, editorItem);
      }

   }

   public void updateInventory() {
      Inventory inventory = this.getInventory();
      this.updateInventory(inventory);
      this.syncInventory();
   }

   @Nullable
   private ItemStack toEditorEquipmentItem(EquipmentSlot equipmentSlot, @Nullable @ReadOnly ItemStack item) {
      ItemStack editorItem;
      if (ItemUtils.isEmpty(item)) {
         editorItem = new ItemStack(Material.ARMOR_STAND);
      } else {
         assert item != null;

         editorItem = item.clone();
      }

      assert editorItem != null;

      this.setEditorEquipmentItemMeta(editorItem, equipmentSlot);
      return editorItem;
   }

   private void setEditorEquipmentItemMeta(@ReadWrite ItemStack item, EquipmentSlot equipmentSlot) {
      String var4 = equipmentSlot.name();
      byte var5 = -1;
      switch(var4.hashCode()) {
      case -1856625337:
         if (var4.equals("SADDLE")) {
            var5 = 7;
         }
         break;
      case 2044322:
         if (var4.equals("BODY")) {
            var5 = 6;
         }
         break;
      case 2153902:
         if (var4.equals("FEET")) {
            var5 = 2;
         }
         break;
      case 2209903:
         if (var4.equals("HAND")) {
            var5 = 0;
         }
         break;
      case 2213344:
         if (var4.equals("HEAD")) {
            var5 = 5;
         }
         break;
      case 2332709:
         if (var4.equals("LEGS")) {
            var5 = 3;
         }
         break;
      case 37796191:
         if (var4.equals("OFF_HAND")) {
            var5 = 1;
         }
         break;
      case 64089825:
         if (var4.equals("CHEST")) {
            var5 = 4;
         }
      }

      String displayName;
      switch(var5) {
      case 0:
         displayName = Messages.equipmentSlotMainhand;
         break;
      case 1:
         displayName = Messages.equipmentSlotOffhand;
         break;
      case 2:
         displayName = Messages.equipmentSlotFeet;
         break;
      case 3:
         displayName = Messages.equipmentSlotLegs;
         break;
      case 4:
         displayName = Messages.equipmentSlotChest;
         break;
      case 5:
         displayName = Messages.equipmentSlotHead;
         break;
      case 6:
         displayName = Messages.equipmentSlotBody;
         break;
      case 7:
         displayName = Messages.equipmentSlotSaddle;
         break;
      default:
         displayName = EnumUtils.formatEnumName(equipmentSlot.name());
      }

      ItemUtils.setDisplayNameAndLore(item, displayName, Messages.equipmentSlotLore);
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
      event.setCancelled(true);
      if (!event.isShiftClick()) {
         if (!this.isAutomaticShiftLeftClick()) {
            int rawSlot = event.getRawSlot();
            if (rawSlot >= 0) {
               InventoryView view = event.getView();
               if (InventoryViewUtils.isTopInventory(view, rawSlot)) {
                  this.handleEditorInventoryClick(event);
               } else if (InventoryViewUtils.isPlayerInventory(view, rawSlot)) {
                  this.handlePlayerInventoryClick(event);
               }

            }
         }
      }
   }

   private void handlePlayerInventoryClick(InventoryClickEvent event) {
      assert event.isCancelled();

      UIHelpers.swapCursor(event.getView(), event.getRawSlot());
   }

   private void handleEditorInventoryClick(InventoryClickEvent event) {
      assert event.isCancelled();

      InventoryView view = event.getView();
      this.handleEditorInventoryClick(view, event.getRawSlot(), event.isLeftClick(), event.isRightClick(), () -> {
         return ItemUtils.cloneOrNullIfEmpty(view.getCursor());
      });
   }

   private void handleEditorInventoryClick(InventoryView view, int rawSlot, boolean leftClick, boolean rightClick, Supplier<ItemStack> getCursorCopy) {
      EquipmentEditorUIState config = this.getConfig();
      List<? extends EquipmentSlot> supportedSlots = config.getSupportedSlots();
      if (rawSlot < supportedSlots.size()) {
         EquipmentSlot equipmentSlot = (EquipmentSlot)supportedSlots.get(rawSlot);
         Inventory inventory = view.getTopInventory();
         if (rightClick) {
            Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
               if (this.isOpen() && !this.abortIfContextInvalid()) {
                  inventory.setItem(rawSlot, this.toEditorEquipmentItem(equipmentSlot, (ItemStack)null));
                  this.onEquipmentChanged(equipmentSlot, (UnmodifiableItemStack)null);
               }
            });
         } else {
            ItemStack cursorClone = (ItemStack)getCursorCopy.get();
            if (leftClick && !ItemUtils.isEmpty(cursorClone)) {
               assert cursorClone != null;

               Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
                  if (this.isOpen() && !this.abortIfContextInvalid()) {
                     cursorClone.setAmount(1);
                     ItemStack substitutedItem = PlaceholderItems.replaceNonNull(cursorClone);
                     this.onEquipmentChanged(equipmentSlot, UnmodifiableItemStack.of(substitutedItem));
                     inventory.setItem(rawSlot, this.toEditorEquipmentItem(equipmentSlot, substitutedItem));
                  }
               });
            }

         }
      }
   }

   protected void onEquipmentChanged(EquipmentSlot slot, @Nullable UnmodifiableItemStack item) {
      EquipmentEditorUIState config = this.getConfig();
      BiConsumer<EquipmentSlot, UnmodifiableItemStack> onEquipmentChanged = config.getOnEquipmentChanged();
      onEquipmentChanged.accept(slot, item);
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
      event.setCancelled(true);
      ItemStack cursorClone = event.getOldCursor();
      if (!ItemUtils.isEmpty(cursorClone)) {
         assert cursorClone != null;

         Set<Integer> rawSlots = event.getRawSlots();
         if (rawSlots.size() == 1) {
            int rawSlot = (Integer)rawSlots.iterator().next();
            if (rawSlot >= 0) {
               InventoryView view = event.getView();
               if (InventoryViewUtils.isTopInventory(view, rawSlot)) {
                  boolean isLeftClick = event.getType() == DragType.EVEN;
                  boolean isRightClick = event.getType() == DragType.SINGLE;
                  this.handleEditorInventoryClick(view, rawSlot, isLeftClick, isRightClick, () -> {
                     return cursorClone;
                  });
               } else if (InventoryViewUtils.isPlayerInventory(view, rawSlot)) {
                  UIHelpers.swapCursorDelayed(view, rawSlot);
               }

            }
         }
      }
   }

   protected void onInventoryClose(@Nullable InventoryCloseEvent closeEvent) {
   }
}
