package com.bergerkiller.bukkit.tc.attachments.ui.item;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.ItemDropTarget;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetBlinkyButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetVerticalNavigableList;
import com.bergerkiller.bukkit.tc.attachments.ui.SetValueTarget;
import com.bergerkiller.bukkit.tc.attachments.ui.models.ResourcePackModelListing;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class MapWidgetItemSelector extends MapWidget implements ItemDropTarget, SetValueTarget {
   private final MapWidgetVerticalNavigableList itemOptions = new MapWidgetVerticalNavigableList() {
      public boolean shouldInterceptInput(MapKeyEvent event) {
         return !(this.root.getActivatedWidget() instanceof CustomModelDataSelector);
      }

      public void onLastItemDown(MapKeyEvent event) {
         this.display.playSound(SoundEffect.PISTON_CONTRACT);
         this.setSelectedIndex(0);
         MapWidgetItemSelector.this.setGridOpened(true);
      }
   };
   private final MapWidgetItemVariantList variantList = new MapWidgetItemVariantList() {
      public void onActivate() {
         ResourcePackModelListing listing = TrainCarts.plugin.getModelListing();
         if (listing.isEmpty()) {
            MapWidgetItemSelector.this.setGridOpened(true);
         } else {
            Iterator var2 = this.display.getOwners().iterator();

            while(var2.hasNext()) {
               Player owner = (Player)var2.next();
               if (this.display.isControlling(owner)) {
                  listing.buildDialog(owner).cancelOnRootRightClick(false).title("Select an item model").setCompactingEnabled(TrainCarts.plugin.getPlayer(owner).getModelSearchCompactFolders()).show().thenAccept((result) -> {
                     if (result.success()) {
                        MapWidgetItemSelector.this.setSelectedItem(result.selectedBareItem());
                     }

                  });
               }
            }

         }
      }
   };
   private final MapWidgetItemPreview preview = new MapWidgetItemPreview() {
   };
   private final MapWidgetItemGrid grid = new MapWidgetItemGrid() {
      public void onSelectionChanged() {
         MapWidgetItemSelector.this.variantList.setItem(this.getSelectedItem());
      }

      public void onAttached() {
         this.setSelectedItem(MapWidgetItemSelector.this.variantList.getItem());
      }

      public void onKeyPressed(MapKeyEvent event) {
         if (event.getKey() != Key.ENTER && event.getKey() != Key.BACK) {
            super.onKeyPressed(event);
         } else {
            MapWidgetItemSelector.this.setGridOpened(false);
         }
      }
   };
   private final MapWidgetBlinkyButton brightnessButton;

   public MapWidgetItemSelector() {
      this.grid.setDimensions(6, 4);
      this.itemOptions.setSize(100, 18);
      this.itemOptions.setPosition((this.grid.getWidth() - this.itemOptions.getWidth()) / 2, 0);
      this.grid.setPosition(0, this.itemOptions.getHeight() + 1);
      this.grid.addCreativeItems();
      this.preview.setBounds(this.grid.getX(), this.grid.getY(), this.grid.getWidth(), this.grid.getHeight());
      this.setSize(this.grid.getWidth(), this.grid.getY() + this.grid.getHeight());
      this.itemOptions.addTab().addWidget(this.variantList);
      Tab tab = this.itemOptions.addTab(new Tab() {
         private final MapTexture bg_texture;

         {
            this.bg_texture = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/item_options_bg.png");
         }

         public void onDraw() {
            this.view.draw(this.bg_texture, 0, 0);
         }
      });
      final MapWidgetBlinkyButton unbreakableOption = new MapWidgetBlinkyButton() {
         protected MapWidget navigateNextWidget(List<MapWidget> widgets, Key key) {
            return key == Key.LEFT ? null : super.navigateNextWidget(widgets, key);
         }

         public void onClick() {
            CommonItemStack item = MapWidgetItemSelector.this.variantList.getItem();
            if (!item.isEmpty()) {
               item = item.clone();
               item.setUnbreakable(!item.isUnbreakable());
               MapWidgetItemSelector.this.variantList.setItem(item);
            }
         }
      };
      unbreakableOption.setSize(14, 14);
      this.variantList.registerItemChangedListener(new ItemChangedListener() {
         public void onItemChanged(CommonItemStack item) {
            if (item.isUnbreakable()) {
               unbreakableOption.setTooltip("Unbreakable");
               unbreakableOption.setIcon("attachments/item_unbreakable.png");
            } else {
               unbreakableOption.setTooltip("Breakable");
               unbreakableOption.setIcon("attachments/item_breakable.png");
            }

         }
      }, true);
      tab.addWidget(unbreakableOption.setPosition(8, 2));
      final MapWidgetSubmitText nameItemTextBox = new MapWidgetSubmitText() {
         public void onAttached() {
            this.setDescription("Enter Item Display Name\nUse empty space to reset");
         }

         public void onAccept(String text) {
            CommonItemStack item = MapWidgetItemSelector.this.variantList.getItem();
            if (!item.isEmpty()) {
               item = item.clone();
               if (text.trim().isEmpty()) {
                  item.setCustomName((ChatText)null);
               } else {
                  item.setCustomNameMessage(text);
               }

               MapWidgetItemSelector.this.variantList.setItem(item);
            }
         }
      };
      tab.addWidget(nameItemTextBox);
      final MapWidgetBlinkyButton nameItemButton = new MapWidgetBlinkyButton() {
         public void onClick() {
            nameItemTextBox.activate();
         }
      };
      nameItemButton.setSize(14, 14);
      nameItemButton.setIcon("attachments/item_named.png");
      tab.addWidget(nameItemButton.setPosition(23, 2));
      this.variantList.registerItemChangedListener(new ItemChangedListener() {
         public void onItemChanged(CommonItemStack item) {
            if (item.hasCustomName()) {
               nameItemButton.setTooltip("Name (\"" + item.getCustomNameMessage() + "\")");
            } else {
               nameItemButton.setTooltip("Name (None)");
            }

         }
      }, true);
      this.brightnessButton = new MapWidgetBlinkyButton() {
         public void onClick() {
            MapWidgetItemSelector.this.onBrightnessClicked();
         }
      };
      this.brightnessButton.setSize(14, 14);
      this.brightnessButton.setIcon("attachments/item_brightness.png");
      this.brightnessButton.setVisible(false);
      this.brightnessButton.setTooltip("Item Brightness");
      tab.addWidget(this.brightnessButton.setPosition(38, 2));
      final CustomModelDataSelector selector = new CustomModelDataSelector() {
         protected MapWidget navigateNextWidget(List<MapWidget> widgets, Key key) {
            return key == Key.RIGHT ? null : super.navigateNextWidget(widgets, key);
         }

         public void onValueChanged() {
            CommonItemStack item = MapWidgetItemSelector.this.variantList.getItem();
            if (!item.isEmpty()) {
               item = item.clone();
               if (this.getValue() <= 0) {
                  item.clearCustomModelData();
               } else {
                  item.setCustomModelData(this.getValue());
               }

               MapWidgetItemSelector.this.variantList.setItem(item);
            }
         }
      };
      selector.setPosition(54, 2);
      tab.addWidget(selector);
      this.variantList.registerItemChangedListener(new ItemChangedListener() {
         public void onItemChanged(CommonItemStack item) {
            int value = item.hasCustomModelData() ? item.getCustomModelData() : 0;
            selector.setValue(value);
         }
      }, true);
      this.variantList.registerItemChangedListener(new ItemChangedListener() {
         public void onItemChanged(CommonItemStack item) {
            MapWidgetItemSelector.this.preview.setItem(item.toBukkit());
            MapWidgetItemSelector.this.onSelectedItemChanged();
         }
      }, false);
   }

   public void setShowBrightnessButton(boolean show) {
      this.brightnessButton.setVisible(show);
   }

   public MapWidgetItemSelector setSelectedItem(ItemStack item) {
      this.variantList.setItem(item);
      return this;
   }

   public ItemStack getSelectedItem() {
      return this.variantList.getItem().toBukkit();
   }

   public void onAttached() {
      this.addWidget(this.itemOptions);
      this.setGridOpened(false);
   }

   public boolean acceptItem(ItemStack item) {
      this.setGridOpened(false);
      this.variantList.setItem(item);
      this.display.playSound(SoundEffect.CLICK_WOOD);
      return true;
   }

   public String getAcceptedPropertyName() {
      return this.variantList.getAcceptedPropertyName();
   }

   public boolean acceptTextValue(String value) {
      return this.variantList.acceptTextValue(value);
   }

   private void setGridOpened(boolean opened) {
      if (!opened && !this.getWidgets().contains(this.preview)) {
         boolean focus = this.getWidgets().contains(this.grid);
         this.swapWidget(this.grid, this.preview);
         if (focus) {
            this.itemOptions.focus();
         }
      } else if (opened && !this.getWidgets().contains(this.grid)) {
         ((MapWidgetItemGrid)this.swapWidget(this.preview, this.grid)).activate();
      }

   }

   public abstract void onSelectedItemChanged();

   public void onBrightnessClicked() {
   }
}
