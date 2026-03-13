package com.nisovin.shopkeepers.shopkeeper.offers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.PriceOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.items.ItemUpdates;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.value.DataValue;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.bukkit.ItemStackValidators;
import com.nisovin.shopkeepers.util.data.property.validation.java.IntegerValidators;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ItemStackSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemMigration;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKPriceOffer implements PriceOffer {
   private final UnmodifiableItemStack item;
   private final int price;
   private static final Property<UnmodifiableItemStack> ITEM;
   private static final Property<Integer> PRICE;
   public static final DataSerializer<PriceOffer> SERIALIZER;
   public static final DataSerializer<List<? extends PriceOffer>> LIST_SERIALIZER;

   public SKPriceOffer(ItemStack item, int price) {
      this(ItemUtils.nonNullUnmodifiableClone(item), price);
   }

   public SKPriceOffer(UnmodifiableItemStack item, int price) {
      Validate.isTrue(!ItemUtils.isEmpty(item), "item is empty");
      Validate.isTrue(price > 0, "price has to be positive");
      this.item = item;
      this.price = price;
   }

   public UnmodifiableItemStack getItem() {
      return this.item;
   }

   public int getPrice() {
      return this.price;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SKPriceOffer [item=");
      builder.append(this.item);
      builder.append(", price=");
      builder.append(this.price);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.item.hashCode();
      result = 31 * result + this.price;
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof SKPriceOffer)) {
         return false;
      } else {
         SKPriceOffer other = (SKPriceOffer)obj;
         if (this.price != other.price) {
            return false;
         } else {
            return this.item.equals((Object)other.item);
         }
      }
   }

   public static void saveOffers(DataValue dataValue, @Nullable @ReadOnly List<? extends PriceOffer> offers) {
      Validate.notNull(dataValue, (String)"dataValue is null");
      if (offers == null) {
         dataValue.clear();
      } else {
         Object offerListData = LIST_SERIALIZER.serialize(offers);
         dataValue.set(offerListData);
      }
   }

   public static List<? extends PriceOffer> loadOffers(DataValue dataValue) throws InvalidDataException {
      Validate.notNull(dataValue, (String)"dataValue is null");
      Object offerListData = dataValue.get();
      return offerListData == null ? Collections.emptyList() : (List)LIST_SERIALIZER.deserialize(offerListData);
   }

   public static boolean migrateOffers(DataValue dataValue, String logPrefix) throws InvalidDataException {
      Validate.notNull(logPrefix, (String)"logPrefix is null");
      List<? extends PriceOffer> offers = loadOffers(dataValue);
      List<? extends PriceOffer> migratedOffers = migrateItems(offers, logPrefix);
      if (offers == migratedOffers) {
         return false;
      } else {
         saveOffers(dataValue, migratedOffers);
         Log.debug(DebugOptions.itemMigrations, () -> {
            return logPrefix + "Migrated items of trade offers.";
         });
         return true;
      }
   }

   private static List<? extends PriceOffer> migrateItems(@ReadOnly List<? extends PriceOffer> offers, String logPrefix) throws InvalidDataException {
      Validate.notNull(offers, (String)"offers is null");

      assert !CollectionUtils.containsNull(offers);

      List<PriceOffer> migratedOffers = null;
      int size = offers.size();

      for(int i = 0; i < size; ++i) {
         PriceOffer offer = (PriceOffer)offers.get(i);

         assert offer != null;

         boolean itemsMigrated = false;
         UnmodifiableItemStack item = offer.getItem();

         assert !ItemUtils.isEmpty(item);

         UnmodifiableItemStack migratedItem = ItemMigration.migrateItemStack(item);
         if (!ItemUtils.isSimilar(item, migratedItem)) {
            if (ItemUtils.isEmpty(migratedItem)) {
               throw new InvalidDataException("Item migration failed for price offer " + (i + 1) + ": " + String.valueOf(offer));
            }

            item = (UnmodifiableItemStack)Unsafe.assertNonNull(migratedItem);
            itemsMigrated = true;
         }

         if (!itemsMigrated) {
            if (migratedOffers != null) {
               migratedOffers.add(offer);
            }
         } else {
            if (migratedOffers == null) {
               migratedOffers = new ArrayList(size);

               for(int j = 0; j < i; ++j) {
                  PriceOffer oldOffer = (PriceOffer)offers.get(j);

                  assert oldOffer != null;

                  migratedOffers.add(oldOffer);
               }
            }

            assert !ItemUtils.isEmpty(item);

            migratedOffers.add(new SKPriceOffer(item, offer.getPrice()));
         }
      }

      return (List)(migratedOffers == null ? offers : migratedOffers);
   }

   public static int updateItems(@ReadWrite List<PriceOffer> offers, String logPrefix) {
      Validate.notNull(logPrefix, (String)"logPrefix is null");
      Validate.notNull(offers, (String)"offers is null");

      assert !CollectionUtils.containsNull(offers);

      int updatedItems = 0;
      int size = offers.size();

      for(int i = 0; i < size; ++i) {
         PriceOffer offer = (PriceOffer)offers.get(i);

         assert offer != null;

         boolean itemsUpdated = false;
         UnmodifiableItemStack item = offer.getItem();

         assert !ItemUtils.isEmpty(item);

         UnmodifiableItemStack updatedItem = ItemUpdates.updateItem(item);
         if (updatedItem != item) {
            assert updatedItem != null && !ItemUtils.isEmpty(updatedItem);

            if (ItemUtils.isEmpty(updatedItem)) {
               Log.warning(logPrefix + "Item update failed for price offer " + (i + 1) + ": " + String.valueOf(offer));
               continue;
            }

            item = updatedItem;
            itemsUpdated = true;
            ++updatedItems;
         }

         if (itemsUpdated) {
            Log.debug(DebugOptions.itemUpdates, logPrefix + "Updated item for price offer " + (i + 1));

            assert !ItemUtils.isEmpty(item);

            offers.set(i, new SKPriceOffer(item, offer.getPrice()));
         }
      }

      return updatedItems;
   }

   static {
      ITEM = (new BasicProperty()).dataKeyAccessor("item", ItemStackSerializers.UNMODIFIABLE).validator(ItemStackValidators.Unmodifiable.NON_EMPTY).build();
      PRICE = (new BasicProperty()).dataKeyAccessor("price", NumberSerializers.INTEGER).validator(IntegerValidators.POSITIVE).build();
      SERIALIZER = new DataSerializer<PriceOffer>() {
         @Nullable
         public Object serialize(PriceOffer value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer offerData = DataContainer.create();
            offerData.set((DataSaver)SKPriceOffer.ITEM, value.getItem());
            offerData.set((DataSaver)SKPriceOffer.PRICE, value.getPrice());
            return offerData.serialize();
         }

         public PriceOffer deserialize(Object data) throws InvalidDataException {
            DataContainer offerData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);

            try {
               UnmodifiableItemStack item = (UnmodifiableItemStack)offerData.get((DataLoader)SKPriceOffer.ITEM);
               int price = (Integer)offerData.get((DataLoader)SKPriceOffer.PRICE);
               return new SKPriceOffer(item, price);
            } catch (MissingDataException var5) {
               throw new InvalidDataException(var5.getMessage(), var5);
            }
         }
      };
      LIST_SERIALIZER = new DataSerializer<List<? extends PriceOffer>>() {
         @Nullable
         public Object serialize(@ReadOnly List<? extends PriceOffer> value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer offerListData = DataContainer.create();
            int id = 1;

            for(Iterator var4 = value.iterator(); var4.hasNext(); ++id) {
               PriceOffer offer = (PriceOffer)var4.next();
               Validate.notNull(offer, (String)"list of offers contains null");
               offerListData.set(String.valueOf(id), SKPriceOffer.SERIALIZER.serialize(offer));
            }

            return offerListData.serialize();
         }

         public List<? extends PriceOffer> deserialize(Object data) throws InvalidDataException {
            DataContainer offerListData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
            Set<? extends String> keys = offerListData.getKeys();
            List<PriceOffer> offers = new ArrayList(keys.size());

            PriceOffer offer;
            for(Iterator var5 = keys.iterator(); var5.hasNext(); offers.add(offer)) {
               String id = (String)var5.next();
               Object offerData = Unsafe.assertNonNull(offerListData.get(id));

               try {
                  offer = (PriceOffer)SKPriceOffer.SERIALIZER.deserialize(offerData);
               } catch (InvalidDataException var10) {
                  throw new InvalidDataException("Invalid price offer " + id + ": " + var10.getMessage(), var10);
               }
            }

            return offers;
         }
      };
   }
}
