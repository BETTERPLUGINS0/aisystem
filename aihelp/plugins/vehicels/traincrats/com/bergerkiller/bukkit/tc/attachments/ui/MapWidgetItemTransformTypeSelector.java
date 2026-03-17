package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.internal.CommonCapabilities;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.config.transform.ItemTransformType;
import java.util.Arrays;
import java.util.Iterator;

public abstract class MapWidgetItemTransformTypeSelector extends MapWidget {
   private MapWidgetSelectionBox categorySelector;
   private MapWidgetItemTransformTypeSelector.TransformTypeSelector typeSelector;
   private ItemTransformType selectedType;
   public static final MapFont<Character> ITEMTRANSFORMTYPE_FONT = new MapFont<Character>() {
      private MapTexture FONT_TEXTURE = null;

      protected MapTexture loadSprite(Character key) {
         if (this.FONT_TEXTURE == null) {
            this.FONT_TEXTURE = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/font.png");
         }

         if (key == 9398) {
            return this.FONT_TEXTURE.getView(0, 0, 5, 8).clone();
         } else if (key == 9401) {
            return this.FONT_TEXTURE.getView(6, 0, 5, 8).clone();
         } else {
            return key == 9390 ? this.FONT_TEXTURE.getView(12, 0, 5, 8).clone() : MINECRAFT.getSprite(key);
         }
      }

      public boolean isNewline(Character key) {
         return key != null && key == '\n';
      }
   };

   public abstract void onSelectedTypeChanged(ItemTransformType var1);

   public static int defaultHeight() {
      return CommonCapabilities.HAS_DISPLAY_ENTITY ? 23 : 11;
   }

   public MapWidgetItemTransformTypeSelector() {
      this.selectedType = ItemTransformType.Category.ARMORSTAND.defaultType();
      this.setSize(100, defaultHeight());
   }

   public void onAttached() {
      if (CommonCapabilities.HAS_DISPLAY_ENTITY) {
         this.categorySelector = (MapWidgetSelectionBox)this.addWidget(new MapWidgetSelectionBox() {
            private boolean changingItems = false;

            public void onAttached() {
               super.onAttached();
               this.setFont(MapWidgetItemTransformTypeSelector.ITEMTRANSFORMTYPE_FONT);
               this.changingItems = true;
               ItemTransformType.Category[] var1 = ItemTransformType.Category.values();
               int var2 = var1.length;

               for(int var3 = 0; var3 < var2; ++var3) {
                  ItemTransformType.Category category = var1[var3];
                  this.addItem(category.toString());
               }

               this.setSelectedIndex(Arrays.asList(ItemTransformType.Category.values()).indexOf(MapWidgetItemTransformTypeSelector.this.selectedType.category()));
               this.changingItems = false;
            }

            public void onSelectedItemChanged() {
               if (!this.changingItems && this.getSelectedIndex() != -1) {
                  ItemTransformType.Category newCategory = ItemTransformType.Category.values()[this.getSelectedIndex()];
                  ItemTransformType newType = MapWidgetItemTransformTypeSelector.this.selectedType.switchCategory(newCategory);
                  if (!newType.equals(MapWidgetItemTransformTypeSelector.this.selectedType)) {
                     MapWidgetItemTransformTypeSelector.this.selectedType = newType;
                     MapWidgetItemTransformTypeSelector.this.typeSelector.updateItems();
                     MapWidgetItemTransformTypeSelector.this.onSelectedTypeChanged(MapWidgetItemTransformTypeSelector.this.selectedType);
                  }
               }

            }
         });
         this.categorySelector.setClipParent(this.isClipParent());
      }

      this.typeSelector = (MapWidgetItemTransformTypeSelector.TransformTypeSelector)this.addWidget(new MapWidgetItemTransformTypeSelector.TransformTypeSelector());
      this.typeSelector.setClipParent(this.isClipParent());
      this.onBoundsChanged();
   }

   public void onDetached() {
      this.categorySelector = null;
      this.typeSelector = null;
   }

   public void onBoundsChanged() {
      if (this.categorySelector != null) {
         int sliderHeight = (this.getHeight() - 1) / 2;
         this.categorySelector.setBounds(0, 0, this.getWidth(), sliderHeight);
         this.typeSelector.setBounds(0, this.getHeight() - sliderHeight, this.getWidth(), sliderHeight);
      } else {
         this.typeSelector.setBounds(0, 0, this.getWidth(), this.getHeight());
      }

   }

   public ItemTransformType getSelectedType() {
      return this.selectedType;
   }

   public void setSelectedType(ItemTransformType selectedType) {
      this.selectedType = selectedType;
      if (this.typeSelector != null) {
         if (this.categorySelector != null) {
            this.categorySelector.setSelectedIndex(Arrays.asList(ItemTransformType.Category.values()).indexOf(selectedType.category()));
         }

         this.typeSelector.setSelectedIndex(selectedType.category().types().indexOf(selectedType));
      }

   }

   private class TransformTypeSelector extends MapWidgetSelectionBox {
      private boolean changingItems;

      private TransformTypeSelector() {
         this.changingItems = false;
      }

      public void onAttached() {
         super.onAttached();
         this.setFont(MapWidgetItemTransformTypeSelector.ITEMTRANSFORMTYPE_FONT);
         this.updateItems();
      }

      public void updateItems() {
         this.changingItems = true;
         this.clearItems();
         ItemTransformType shownSelectedType = MapWidgetItemTransformTypeSelector.this.selectedType;
         if (!CommonCapabilities.HAS_DISPLAY_ENTITY) {
            shownSelectedType = shownSelectedType.switchCategory(ItemTransformType.Category.ARMORSTAND);
         }

         Iterator var2 = shownSelectedType.category().types().iterator();

         while(var2.hasNext()) {
            ItemTransformType type = (ItemTransformType)var2.next();
            this.addItem(type.typeName());
         }

         this.setSelectedIndex(shownSelectedType.category().types().indexOf(shownSelectedType));
         this.changingItems = false;
      }

      public void onSelectedItemChanged() {
         if (!this.changingItems && this.getSelectedIndex() != -1) {
            MapWidgetItemTransformTypeSelector.this.selectedType = (ItemTransformType)MapWidgetItemTransformTypeSelector.this.selectedType.category().types().get(this.getSelectedIndex());
            MapWidgetItemTransformTypeSelector.this.onSelectedTypeChanged(MapWidgetItemTransformTypeSelector.this.selectedType);
         }

      }

      // $FF: synthetic method
      TransformTypeSelector(Object x1) {
         this();
      }
   }
}
