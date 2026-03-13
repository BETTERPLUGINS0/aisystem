package com.nisovin.shopkeepers.shopcreation;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.NamespacedKeyUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ShopCreationItem {
   private static final NamespacedKey KEY_SHOP_CREATION_ITEM = NamespacedKeyUtils.create("shopkeepers", "shop_creation_item");
   private static final NamespacedKey KEY_SHOP_TYPE = NamespacedKeyUtils.create("shopkeepers", "shop_type");
   private static final NamespacedKey KEY_OBJECT_TYPE = NamespacedKeyUtils.create("shopkeepers", "object_type");
   @Nullable
   private final ItemStack itemStack;
   @Nullable
   private ItemMeta itemMeta;

   public static ItemStack create() {
      return create(1);
   }

   public static ItemStack create(int amount) {
      return Settings.DerivedSettings.shopCreationItemData.createItemStack(amount);
   }

   public static boolean isShopCreationItem(@Nullable UnmodifiableItemStack itemStack) {
      return isShopCreationItem(ItemUtils.asItemStackOrNull(itemStack));
   }

   public static boolean isShopCreationItem(@Nullable @ReadOnly ItemStack itemStack) {
      return (new ShopCreationItem(itemStack)).isShopCreationItem();
   }

   public ShopCreationItem(@Nullable @ReadWrite ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   @Nullable
   private ItemMeta getItemMeta() {
      if (this.itemMeta != null) {
         return this.itemMeta;
      } else if (ItemUtils.isEmpty(this.itemStack)) {
         return null;
      } else {
         assert this.itemStack != null;

         this.itemMeta = (ItemMeta)Unsafe.assertNonNull(this.itemStack.getItemMeta());
         return this.itemMeta;
      }
   }

   public void applyItemMeta() {
      if (this.itemMeta != null) {
         if (this.itemStack != null) {
            assert this.itemStack != null;

            assert this.itemMeta != null;

            this.itemStack.setItemMeta(this.itemMeta);
         }
      }
   }

   public boolean isShopCreationItem() {
      return Settings.identifyShopCreationItemByTag ? this.hasTag() : Settings.shopCreationItem.matches(this.itemStack);
   }

   public boolean hasTag() {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         return dataContainer.has(KEY_SHOP_CREATION_ITEM);
      }
   }

   public boolean addTag() {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         if (dataContainer.has(KEY_SHOP_CREATION_ITEM)) {
            return false;
         } else {
            dataContainer.set(KEY_SHOP_CREATION_ITEM, PersistentDataType.BOOLEAN, true);
            return true;
         }
      }
   }

   public boolean hasShopType() {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         return dataContainer.has(KEY_SHOP_TYPE);
      }
   }

   @Nullable
   public String getShopTypeId() {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return null;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

         try {
            return (String)dataContainer.get(KEY_SHOP_TYPE, PersistentDataType.STRING);
         } catch (IllegalArgumentException var4) {
            return dataContainer.has(KEY_SHOP_TYPE) ? "" : null;
         }
      }
   }

   public boolean setShopType(ShopType<?> shopType) {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         dataContainer.set(KEY_SHOP_TYPE, PersistentDataType.STRING, shopType.getIdentifier());
         return true;
      }
   }

   public boolean hasObjectType() {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         return dataContainer.has(KEY_OBJECT_TYPE);
      }
   }

   @Nullable
   public String getObjectTypeId() {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return null;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

         try {
            return (String)dataContainer.get(KEY_OBJECT_TYPE, PersistentDataType.STRING);
         } catch (IllegalArgumentException var4) {
            return dataContainer.has(KEY_OBJECT_TYPE) ? "" : null;
         }
      }
   }

   public boolean setObjectType(ShopObjectType<?> objectType) {
      ItemMeta meta = this.getItemMeta();
      if (meta == null) {
         return false;
      } else {
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         dataContainer.set(KEY_OBJECT_TYPE, PersistentDataType.STRING, objectType.getIdentifier());
         return true;
      }
   }
}
