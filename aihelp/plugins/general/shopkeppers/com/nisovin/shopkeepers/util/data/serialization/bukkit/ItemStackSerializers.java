package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.bukkit.DataUtils;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.bukkit.ServerUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemStackComponentsData;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ItemStackSerializers {
   public static final Property<Integer> DATA_VERSION;
   public static final Property<NamespacedKey> ID;
   public static final Property<Integer> COUNT;
   public static final Property<ItemStackComponentsData> COMPONENTS;
   public static final DataSerializer<ItemStack> DEFAULT;
   public static final DataSerializer<UnmodifiableItemStack> UNMODIFIABLE;

   private ItemStackSerializers() {
   }

   static {
      DATA_VERSION = (new BasicProperty()).dataKeyAccessor("DataVersion", NumberSerializers.INTEGER).useDefaultIfMissing().defaultValue(ServerUtils.getDataVersion()).build();
      ID = (new BasicProperty()).dataKeyAccessor("id", NamespacedKeySerializers.DEFAULT).build();
      COUNT = (new BasicProperty()).dataKeyAccessor("count", NumberSerializers.INTEGER).useDefaultIfMissing().defaultValue(1).build();
      COMPONENTS = (new BasicProperty()).dataKeyAccessor("components", new DataSerializer<ItemStackComponentsData>() {
         @Nullable
         public Object serialize(ItemStackComponentsData value) {
            return DataContainerSerializers.DEFAULT.serialize(value);
         }

         public ItemStackComponentsData deserialize(Object data) throws InvalidDataException {
            DataContainer dataContainer = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
            return ItemStackComponentsData.ofNonNull(dataContainer);
         }
      }).nullable().build();
      DEFAULT = new DataSerializer<ItemStack>() {
         @Nullable
         public Object serialize(ItemStack value) {
            Validate.notNull(value, (String)"value is null");
            return value;
         }

         public ItemStack deserialize(Object data) throws InvalidDataException {
            Validate.notNull(data, "data is null");
            if (data instanceof ItemStack) {
               ItemStack itemStack = (ItemStack)data;
               return DataUtils.processNonNullLoadedItemStack(itemStack);
            } else if (data instanceof UnmodifiableItemStack) {
               UnmodifiableItemStack unmodifiableItemStack = (UnmodifiableItemStack)data;
               return unmodifiableItemStack.copy();
            } else {
               DataContainer dataContainer = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);

               try {
                  Integer dataVersion = (Integer)dataContainer.get((DataLoader)ItemStackSerializers.DATA_VERSION);
                  NamespacedKey id = (NamespacedKey)dataContainer.get((DataLoader)ItemStackSerializers.ID);
                  Integer count = (Integer)dataContainer.get((DataLoader)ItemStackSerializers.COUNT);
                  ItemStackComponentsData componentsData = (ItemStackComponentsData)dataContainer.get((DataLoader)ItemStackSerializers.COMPONENTS);

                  ItemStack itemStackx;
                  try {
                     itemStackx = Compat.getProvider().deserializeItemStack(dataVersion, id, count, componentsData);
                  } catch (Exception var11) {
                     throw new InvalidDataException("Failed to deserialize ItemStack!", var11);
                  }

                  if (Unsafe.nullable(itemStackx) == null) {
                     throw new InvalidDataException("Loaded ItemStack is null!");
                  } else {
                     itemStackx = DataUtils.processNonNullLoadedItemStack(itemStackx);
                     return itemStackx;
                  }
               } catch (MissingDataException var12) {
                  throw new InvalidDataException(var12.getMessage(), var12);
               }
            }
         }
      };
      UNMODIFIABLE = new DataSerializer<UnmodifiableItemStack>() {
         @Nullable
         public Object serialize(UnmodifiableItemStack value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer dataContainer = DataContainer.create();
            dataContainer.set((DataSaver)ItemStackSerializers.DATA_VERSION, ServerUtils.getDataVersion());
            dataContainer.set((DataSaver)ItemStackSerializers.ID, RegistryUtils.getKeyOrThrow(value.getType()));
            dataContainer.set((DataSaver)ItemStackSerializers.COUNT, value.getAmount());
            ItemStackComponentsData componentsData = ItemStackComponentsData.of(ItemUtils.asItemStack(value));
            dataContainer.set((DataSaver)ItemStackSerializers.COMPONENTS, componentsData);
            return dataContainer.serialize();
         }

         public UnmodifiableItemStack deserialize(Object data) throws InvalidDataException {
            if (data instanceof UnmodifiableItemStack) {
               UnmodifiableItemStack unmodifiableItemStack = (UnmodifiableItemStack)data;
               return unmodifiableItemStack;
            } else {
               return UnmodifiableItemStack.ofNonNull((ItemStack)ItemStackSerializers.DEFAULT.deserialize(data));
            }
         }
      };
   }
}
