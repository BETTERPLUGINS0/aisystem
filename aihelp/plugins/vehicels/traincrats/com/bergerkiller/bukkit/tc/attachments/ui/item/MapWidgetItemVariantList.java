package com.bergerkiller.bukkit.tc.attachments.ui.item;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapFont.Alignment;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.wrappers.ChatText;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetArrow;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;
import com.bergerkiller.bukkit.tc.attachments.ui.SetValueTarget;
import com.bergerkiller.bukkit.tc.attachments.ui.models.ResourcePackModelListing;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.ListedItemModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public abstract class MapWidgetItemVariantList extends MapWidget implements SetValueTarget, ItemChangedListener {
   private final List<ItemChangedListener> itemChangedListeners = new ArrayList();
   private final MapWidgetArrow nav_left;
   private final MapWidgetArrow nav_right;
   private final MapWidgetTooltip below_tooltip;
   private final MapTexture background;
   private List<CommonItemStack> variants;
   private Map<CommonItemStack, MapTexture> iconCache;
   private int variantIndex;

   public MapWidgetItemVariantList() {
      this.nav_left = new MapWidgetArrow(BlockFace.WEST);
      this.nav_right = new MapWidgetArrow(BlockFace.EAST);
      this.below_tooltip = new MapWidgetTooltip();
      this.iconCache = new HashMap();
      this.variantIndex = 0;
      this.background = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/item_selector_bg.png");
      this.setSize(100, 18);
      this.setFocusable(true);
      this.variants = new ArrayList(0);
      this.nav_left.setPosition(0, 4);
      this.nav_right.setPosition(this.getWidth() - this.nav_right.getWidth(), 4);
      this.nav_left.setVisible(false);
      this.nav_right.setVisible(false);
      this.addWidget(this.nav_left);
      this.addWidget(this.nav_right);
      this.addWidget(this.below_tooltip);
      this.setRetainChildWidgets(true);
      this.itemChangedListeners.add(this);
   }

   public CommonItemStack getItem() {
      return this.variantIndex >= 0 && this.variantIndex < this.variants.size() ? (CommonItemStack)this.variants.get(this.variantIndex) : CommonItemStack.empty();
   }

   public void setItem(ItemStack item) {
      this.setItem(CommonItemStack.of(item));
   }

   public void setItem(CommonItemStack item) {
      this.loadVariants(item);
      if (item.isEmpty()) {
         this.invalidate();
         this.fireItemChangeEvent();
      } else {
         this.variantIndex = 0;

         for(int i = 0; i < this.variants.size(); ++i) {
            CommonItemStack variant = (CommonItemStack)this.variants.get(i);
            if (variant.equalsIgnoreAmount(item)) {
               this.variantIndex = i;
               break;
            }

            if (item.isDamageSupported() && variant.getDamage() == item.getDamage()) {
               this.variantIndex = i;
            }
         }

         this.invalidate();
         this.fireItemChangeEvent();
      }
   }

   private void loadVariants(CommonItemStack item) {
      if (item.isEmpty()) {
         this.variants = new ArrayList();
         this.variantIndex = 0;
      } else {
         ResourcePackModelListing models = TrainCarts.plugin.getModelListing();
         if (models.isBareItem(item.toBukkit())) {
            this.variants = new ArrayList(models.root().bareItemStacks().keySet());
         } else {
            int customModelData;
            if (item.isDamageSupported()) {
               customModelData = item.getMaxDamage();
               this.variants = new ArrayList(customModelData + 1);

               for(int i = 0; i <= customModelData; ++i) {
                  this.variants.add(item.clone().setDamage(i));
               }

            } else {
               this.variants = (List)ItemUtil.getItemVariants(item.getType()).stream().filter(Objects::nonNull).map(CommonItemStack::of).map(CommonItemStack::clone).collect(Collectors.toList());
               if (this.variants.size() == 1) {
                  ((CommonItemStack)this.variants.get(0)).toBukkit().setItemMeta(item.toBukkit().getItemMeta());
               } else {
                  Iterator var3 = this.variants.iterator();

                  CommonItemStack variant;
                  while(var3.hasNext()) {
                     variant = (CommonItemStack)var3.next();
                     Iterator var5 = item.toBukkit().getEnchantments().entrySet().iterator();

                     while(var5.hasNext()) {
                        Entry<Enchantment, Integer> enchantment = (Entry)var5.next();
                        variant.addEnchantment((Enchantment)enchantment.getKey(), (Integer)enchantment.getValue());
                     }
                  }

                  Iterator var9;
                  CommonItemStack variant;
                  if (item.hasCustomName()) {
                     ChatText customName = item.getCustomName();
                     var9 = this.variants.iterator();

                     while(var9.hasNext()) {
                        variant = (CommonItemStack)var9.next();
                        variant.setCustomName(customName);
                     }
                  }

                  if (item.isUnbreakable()) {
                     var3 = this.variants.iterator();

                     while(var3.hasNext()) {
                        variant = (CommonItemStack)var3.next();
                        variant.setUnbreakable(true);
                     }
                  }

                  if (item.hasCustomModelData()) {
                     customModelData = item.getCustomModelData();
                     var9 = this.variants.iterator();

                     while(var9.hasNext()) {
                        variant = (CommonItemStack)var9.next();
                        variant.setCustomModelData(customModelData);
                     }
                  }
               }

            }
         }
      }
   }

   public String getAcceptedPropertyName() {
      return "Item Information";
   }

   public boolean acceptTextValue(String value) {
      value = value.trim();

      int nameEnd;
      for(nameEnd = 0; nameEnd < value.length() && value.charAt(nameEnd) != '{' && value.charAt(nameEnd) != ' '; ++nameEnd) {
      }

      String itemName = value.substring(0, nameEnd);
      if (nameEnd >= value.length()) {
         value = "";
      } else {
         value = value.substring(nameEnd).trim();
      }

      if (ParseUtil.isNumeric(itemName)) {
         try {
            this.setVariantIndex(Integer.parseInt(itemName));
         } catch (NumberFormatException var8) {
            return false;
         }
      } else {
         Material newItemMaterial = ParseUtil.parseMaterial(itemName, (Material)null);
         if (newItemMaterial == null) {
            return false;
         }

         CommonItemStack newItem = CommonItemStack.create(newItemMaterial, 1);

         for(nameEnd = 0; nameEnd < value.length() && value.charAt(nameEnd) != '{' && value.charAt(nameEnd) != ' '; ++nameEnd) {
         }

         String damageValueStr = value.substring(0, nameEnd).trim();
         if (!damageValueStr.isEmpty() && newItem.isDamageSupported() && ParseUtil.isNumeric(damageValueStr)) {
            try {
               int damage = Integer.parseInt(damageValueStr);
               if (damage < 0 || damage > newItem.getMaxDamage()) {
                  return false;
               }

               newItem.setDamage(damage);
            } catch (NumberFormatException var9) {
               return false;
            }
         }

         this.setItem(newItem);
      }

      return true;
   }

   public void onFocus() {
      this.nav_left.setVisible(true);
      this.nav_right.setVisible(true);
   }

   public void onBlur() {
      this.nav_left.setVisible(false);
      this.nav_right.setVisible(false);
   }

   public void onDraw() {
      int selector_edge = this.nav_left.getWidth() + 1;
      MapCanvas itemView = this.view.getView(selector_edge, 0, this.getWidth() - 2 * selector_edge, this.getHeight());
      itemView.draw(this.background, 0, 0);
      int x = 1;
      int y = 1;

      for(int index = this.variantIndex - 2; index <= this.variantIndex + 2; ++index) {
         if (index >= 0 && index < this.variants.size()) {
            CommonItemStack item = (CommonItemStack)this.variants.get(index);
            MapTexture icon = (MapTexture)this.iconCache.get(item);
            if (icon == null) {
               icon = MapTexture.createEmpty(16, 16);
               icon.fillItem(TCConfig.resourcePack, item.toBukkit());
               this.iconCache.put(item, icon);
            }

            itemView.draw(icon, x, y);
         }

         x += 17;
      }

      if (this.variantIndex >= 0 && this.variantIndex < this.variants.size()) {
         CommonItemStack item = (CommonItemStack)this.variants.get(this.variantIndex);
         if (item.isDamageSupported()) {
            itemView.setAlignment(Alignment.MIDDLE);
            itemView.draw(MapFont.TINY, 44, 12, (byte)18, Integer.toString(item.getDamage()));
         }
      }

   }

   private void changeVariantIndex(int offset) {
      this.setVariantIndex(this.variantIndex + offset);
   }

   private void setVariantIndex(int newVariantIndex) {
      if (newVariantIndex < 0) {
         newVariantIndex = 0;
      } else if (newVariantIndex >= this.variants.size()) {
         newVariantIndex = this.variants.size() - 1;
      }

      if (this.variantIndex != newVariantIndex) {
         this.variantIndex = newVariantIndex;
         this.invalidate();
         this.fireItemChangeEvent();
         this.display.playSound(SoundEffect.CLICK);
      }
   }

   public void onKeyReleased(MapKeyEvent event) {
      super.onKeyReleased(event);
      if (event.getKey() == Key.LEFT) {
         this.nav_left.stopFocus();
      } else if (event.getKey() == Key.RIGHT) {
         this.nav_right.stopFocus();
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.LEFT) {
         this.changeVariantIndex(-1 - event.getRepeat() / 40);
         this.nav_left.sendFocus();
      } else if (event.getKey() == Key.RIGHT) {
         this.changeVariantIndex(1 + event.getRepeat() / 40);
         this.nav_right.sendFocus();
      } else {
         super.onKeyPressed(event);
      }

   }

   public void registerItemChangedListener(ItemChangedListener listener, boolean fireEventNow) {
      this.itemChangedListeners.add(listener);
      if (fireEventNow) {
         listener.onItemChanged(this.getItem());
      }

   }

   private void fireItemChangeEvent() {
      CommonItemStack item = this.getItem();
      ListedItemModel itemMeta = TrainCarts.plugin.getModelListing().getBareItemModel(item.toBukkit());
      if (itemMeta != null) {
         this.below_tooltip.setText(itemMeta.name());
         this.below_tooltip.setVisible(true);
      } else {
         this.below_tooltip.setText("");
         this.below_tooltip.setVisible(false);
      }

      Iterator var4 = this.itemChangedListeners.iterator();

      while(var4.hasNext()) {
         ItemChangedListener listener = (ItemChangedListener)var4.next();
         listener.onItemChanged(item);
      }

   }

   public void onItemChanged(CommonItemStack item) {
   }
}
