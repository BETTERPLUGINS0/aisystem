package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.bukkit.MaterialValidators;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ItemStackSerializers;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.MinecraftEnumSerializers;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.NamespacedKeySerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ItemData {
   @Nullable
   private static Integer SERIALIZER_DATA_VERSION = null;
   private static final Property<Material> ITEM_TYPE;
   private static final String META_TYPE_KEY = "meta-type";
   private static final Map<Material, String> META_TYPE_BY_ITEM_TYPE;
   public static final DataSerializer<ItemData> SERIALIZER;
   private final UnmodifiableItemStack dataItem;
   @Nullable
   @ReadOnly
   private ItemStackMetaTag serializedMetaData;

   public static void setSerializerDataVersion(int dataVersion) {
      SERIALIZER_DATA_VERSION = dataVersion;
   }

   @Nullable
   private static String getMetaType(Material itemType) {
      Validate.notNull(itemType, (String)"itemType is null");
      String metaType = (String)META_TYPE_BY_ITEM_TYPE.get(itemType);
      if (metaType != null) {
         return metaType;
      } else {
         assert metaType == null;

         if (META_TYPE_BY_ITEM_TYPE.containsKey(itemType)) {
            return null;
         } else {
            ItemMeta itemMeta = (new ItemStack(itemType)).getItemMeta();
            if (itemMeta != null) {
               metaType = (String)itemMeta.serialize().get("meta-type");
               if (metaType == null) {
                  throw new IllegalStateException("Could not determine the meta type of " + itemMeta.getClass().getName() + "!");
               }
            }

            META_TYPE_BY_ITEM_TYPE.put(itemType, metaType);
            return metaType;
         }
      }
   }

   public ItemData(Material type) {
      this(UnmodifiableItemStack.ofNonNull(new ItemStack(type)));
   }

   public ItemData(Material type, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore) {
      this(UnmodifiableItemStack.ofNonNull(ItemUtils.createItemStack((Material)type, 1, displayName, lore)));
   }

   public ItemData(ItemData otherItemData, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore) {
      this(UnmodifiableItemStack.ofNonNull(ItemUtils.createItemStack((ItemData)otherItemData, 1, displayName, lore)));
   }

   public ItemData(@ReadOnly ItemStack dataItem) {
      this(ItemUtils.unmodifiableCopyWithAmount((ItemStack)dataItem, 1));
   }

   public ItemData(UnmodifiableItemStack dataItem) {
      this.serializedMetaData = null;
      Validate.notNull(dataItem, (String)"dataItem is null");
      this.dataItem = ItemUtils.unmodifiableCopyWithAmount((UnmodifiableItemStack)dataItem, 1);
   }

   public UnmodifiableItemStack asUnmodifiableItemStack() {
      return this.dataItem;
   }

   public Material getType() {
      return this.dataItem.getType();
   }

   public int getMaxStackSize() {
      return this.dataItem.getMaxStackSize();
   }

   public ItemData withType(Material type) {
      Validate.notNull(type, (String)"type is null");
      Validate.isTrue(type.isItem(), () -> {
         return "type is not an item: " + String.valueOf(type);
      });
      if (this.getType() == type) {
         return this;
      } else {
         ItemStack newDataItem = this.createItemStack();
         newDataItem.setType(type);
         return new ItemData(UnmodifiableItemStack.ofNonNull(newDataItem));
      }
   }

   private ItemStackMetaTag getSerializedMetaData() {
      if (this.serializedMetaData == null) {
         this.serializedMetaData = ItemStackMetaTag.of(ItemUtils.asItemStack(this.dataItem));
      }

      assert this.serializedMetaData != null;

      return this.serializedMetaData;
   }

   public boolean hasItemMeta() {
      return !this.getSerializedMetaData().isEmpty();
   }

   @Nullable
   public ItemMeta getItemMeta() {
      return this.dataItem.getItemMeta();
   }

   public ItemStack createItemStack() {
      return this.createItemStack(1);
   }

   public ItemStack createItemStack(int amount) {
      return ItemUtils.copyWithAmount(this.dataItem, amount);
   }

   public UnmodifiableItemStack createUnmodifiableItemStack(int amount) {
      return UnmodifiableItemStack.ofNonNull(this.createItemStack(amount));
   }

   public boolean isSimilar(@Nullable @ReadOnly ItemStack other) {
      return this.dataItem.isSimilar(other);
   }

   public boolean isSimilar(@Nullable UnmodifiableItemStack other) {
      return other != null && other.isSimilar(this.dataItem);
   }

   public boolean matches(@Nullable @ReadOnly ItemStack item) {
      return this.matches(item, true);
   }

   public boolean matches(@Nullable UnmodifiableItemStack item) {
      return this.matches(ItemUtils.asItemStackOrNull(item));
   }

   public boolean matches(@Nullable @ReadOnly ItemStack item, boolean matchPartialLists) {
      return ItemUtils.matchesData(item, this.getType(), this.getSerializedMetaData(), matchPartialLists);
   }

   public boolean matches(@Nullable UnmodifiableItemStack item, boolean matchPartialLists) {
      return this.matches(ItemUtils.asItemStackOrNull(item), matchPartialLists);
   }

   public boolean matches(@Nullable ItemData itemData) {
      return this.matches(itemData, true);
   }

   public boolean matches(@Nullable ItemData itemData, boolean matchPartialLists) {
      if (itemData == null) {
         return false;
      } else {
         return itemData.getType() != this.getType() ? false : ItemUtils.matchesData(itemData.getSerializedMetaData(), this.getSerializedMetaData(), matchPartialLists);
      }
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ItemData [data=");
      builder.append(this.dataItem);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.dataItem.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof ItemData)) {
         return false;
      } else {
         ItemData other = (ItemData)obj;
         return this.dataItem.equals((Object)other.dataItem);
      }
   }

   public Object serialize() {
      return Unsafe.assertNonNull(SERIALIZER.serialize(this));
   }

   static {
      ITEM_TYPE = (new BasicProperty()).dataKeyAccessor("type", MinecraftEnumSerializers.Materials.LENIENT).validator(MaterialValidators.IS_ITEM).validator(MaterialValidators.NON_LEGACY).build();
      META_TYPE_BY_ITEM_TYPE = new HashMap();
      SERIALIZER = new DataSerializer<ItemData>() {
         @Nullable
         public Object serialize(ItemData value) {
            Validate.notNull(value, (String)"value is null");
            NamespacedKey itemTypeKey = RegistryUtils.getKeyOrThrow(value.getType());
            ItemStackComponentsData componentsData = ItemStackComponentsData.of(ItemUtils.asItemStack(value.dataItem));
            if (componentsData == null) {
               return NamespacedKeySerializers.DEFAULT.serialize(itemTypeKey);
            } else {
               DataContainer dataContainer = DataContainer.create();
               dataContainer.set((DataSaver)ItemStackSerializers.ID, itemTypeKey);
               dataContainer.set((DataSaver)ItemStackSerializers.COMPONENTS, componentsData);
               return dataContainer.serialize();
            }
         }

         public ItemData deserialize(Object data) throws InvalidDataException {
            Validate.notNull(data, "data is null");

            try {
               Integer dataVersion = ItemData.SERIALIZER_DATA_VERSION;
               UnmodifiableItemStack dataItem;
               DataContainer dataContainer;
               if (data instanceof String) {
                  String dataString = (String)data;
                  dataContainer = DataContainer.create();
                  dataContainer.set((String)ItemStackSerializers.ID.getName(), dataString);
                  dataContainer.set((String)ItemStackSerializers.DATA_VERSION.getName(), dataVersion);
                  dataItem = (UnmodifiableItemStack)ItemStackSerializers.UNMODIFIABLE.deserialize(dataContainer);
               } else {
                  dataContainer = DataContainer.of(data);
                  if (dataContainer != null) {
                     dataContainer = DataContainer.ofNonNull(dataContainer.getValuesCopy());
                     dataContainer.set((String)ItemStackSerializers.DATA_VERSION.getName(), dataVersion);
                     dataItem = (UnmodifiableItemStack)ItemStackSerializers.UNMODIFIABLE.deserialize(dataContainer);
                  } else {
                     dataItem = (UnmodifiableItemStack)ItemStackSerializers.UNMODIFIABLE.deserialize(data);
                  }
               }

               return new ItemData(dataItem);
            } catch (InvalidDataException var7) {
               try {
                  return this.legacyDeserialize(data);
               } catch (InvalidDataException var6) {
                  throw var7;
               }
            }
         }

         private ItemData legacyDeserialize(Object data) throws InvalidDataException {
            DataContainer itemDataData = null;
            Material itemType;
            if (data instanceof String) {
               itemType = (Material)MinecraftEnumSerializers.Materials.LENIENT.deserialize((String)data);

               try {
                  ItemData.ITEM_TYPE.validateValue(itemType);
               } catch (Exception var9) {
                  throw new InvalidDataException(var9.getMessage(), var9);
               }
            } else {
               itemDataData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);

               try {
                  itemType = (Material)itemDataData.get((DataLoader)ItemData.ITEM_TYPE);
               } catch (MissingDataException var8) {
                  throw new InvalidDataException(var8.getMessage(), var8);
               }

               if (itemDataData.size() <= 1) {
                  itemDataData = null;
               }
            }

            assert itemType != null;

            ItemStack dataItem = new ItemStack(itemType);
            if (itemDataData != null) {
               Map<String, Object> itemMetaData = itemDataData.getValuesCopy();
               ConfigUtils.convertSectionsToMaps(itemMetaData);
               String metaType = ItemData.getMetaType(itemType);
               if (metaType == null) {
                  throw new InvalidDataException("Items of type " + itemType.name() + " do not support metadata!");
               }

               itemMetaData.put("meta-type", metaType);
               ItemMeta itemMeta = ItemSerialization.deserializeItemMeta(itemMetaData);
               dataItem.setItemMeta(itemMeta);
            }

            ItemData itemData = new ItemData(UnmodifiableItemStack.ofNonNull(dataItem));
            return itemData;
         }
      };
   }
}
