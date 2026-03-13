package com.nisovin.shopkeepers.shopkeeper.offers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.offers.TradeOffer;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.items.ItemUpdates;
import com.nisovin.shopkeepers.shopkeeper.SKTradingRecipe;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.value.DataValue;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.bukkit.ItemStackValidators;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ItemStackSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
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

public class SKTradeOffer extends SKTradingRecipe implements TradeOffer {
   private static final Property<UnmodifiableItemStack> RESULT_ITEM;
   private static final Property<UnmodifiableItemStack> ITEM1;
   private static final Property<UnmodifiableItemStack> ITEM2;
   public static final DataSerializer<TradeOffer> SERIALIZER;
   public static final DataSerializer<List<? extends TradeOffer>> LIST_SERIALIZER;

   public SKTradeOffer(@ReadOnly ItemStack resultItem, @ReadOnly ItemStack item1, @Nullable @ReadOnly ItemStack item2) {
      super(resultItem, item1, item2);
   }

   public SKTradeOffer(UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      super(resultItem, item1, item2);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SKTradeOffer [resultItem=");
      builder.append(this.resultItem);
      builder.append(", item1=");
      builder.append(this.item1);
      builder.append(", item2=");
      builder.append(this.item2);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!super.equals(obj)) {
         return false;
      } else {
         return obj instanceof SKTradeOffer;
      }
   }

   public static TradingRecipe toTradingRecipe(TradeOffer offer, boolean outOfStock) {
      return new SKTradingRecipe(offer.getResultItem(), offer.getItem1(), offer.getItem2(), outOfStock);
   }

   public static void saveOffers(DataValue dataValue, @Nullable @ReadOnly List<? extends TradeOffer> offers) {
      Validate.notNull(dataValue, (String)"dataValue is null");
      if (offers == null) {
         dataValue.clear();
      } else {
         Object offerListData = LIST_SERIALIZER.serialize(offers);
         dataValue.set(offerListData);
      }
   }

   public static List<? extends TradeOffer> loadOffers(DataValue dataValue) throws InvalidDataException {
      Validate.notNull(dataValue, (String)"dataValue is null");
      Object offerListData = dataValue.get();
      return offerListData == null ? Collections.emptyList() : (List)LIST_SERIALIZER.deserialize(offerListData);
   }

   public static boolean migrateOffers(DataValue dataValue, String logPrefix) throws InvalidDataException {
      Validate.notNull(logPrefix, (String)"logPrefix is null");
      List<? extends TradeOffer> offers = loadOffers(dataValue);
      List<? extends TradeOffer> migratedOffers = migrateItems(offers, logPrefix);
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

   private static List<? extends TradeOffer> migrateItems(@ReadOnly List<? extends TradeOffer> offers, String logPrefix) throws InvalidDataException {
      Validate.notNull(offers, (String)"offers is null");

      assert !CollectionUtils.containsNull(offers);

      List<TradeOffer> migratedOffers = null;
      int size = offers.size();

      for(int i = 0; i < size; ++i) {
         TradeOffer offer = (TradeOffer)offers.get(i);

         assert offer != null;

         boolean itemsMigrated = false;
         boolean migrationFailed = false;
         UnmodifiableItemStack resultItem = offer.getResultItem();

         assert !ItemUtils.isEmpty(resultItem);

         UnmodifiableItemStack item1 = offer.getItem1();

         assert !ItemUtils.isEmpty(item1);

         UnmodifiableItemStack item2 = offer.getItem2();
         UnmodifiableItemStack migratedResultItem = ItemMigration.migrateItemStack(resultItem);
         if (!ItemUtils.isSimilar(resultItem, migratedResultItem)) {
            if (ItemUtils.isEmpty(migratedResultItem)) {
               migrationFailed = true;
            } else {
               resultItem = (UnmodifiableItemStack)Unsafe.assertNonNull(migratedResultItem);
               itemsMigrated = true;
            }
         }

         UnmodifiableItemStack migratedItem1 = ItemMigration.migrateItemStack(item1);
         if (!ItemUtils.isSimilar(item1, migratedItem1)) {
            if (ItemUtils.isEmpty(migratedItem1)) {
               migrationFailed = true;
            } else {
               item1 = (UnmodifiableItemStack)Unsafe.assertNonNull(migratedItem1);
               itemsMigrated = true;
            }
         }

         UnmodifiableItemStack migratedItem2 = ItemMigration.migrateItemStack(item2);
         if (!ItemUtils.isSimilar(item2, migratedItem2)) {
            if (ItemUtils.isEmpty(migratedItem2) && !ItemUtils.isEmpty(item2)) {
               migrationFailed = true;
            } else {
               item2 = migratedItem2;
               itemsMigrated = true;
            }
         }

         if (migrationFailed) {
            throw new InvalidDataException("Item migration failed for trade offer " + (i + 1) + ": " + String.valueOf(offer));
         }

         if (itemsMigrated) {
            if (migratedOffers == null) {
               migratedOffers = new ArrayList(size);

               for(int j = 0; j < i; ++j) {
                  TradeOffer oldOffer = (TradeOffer)offers.get(j);

                  assert oldOffer != null;

                  migratedOffers.add(oldOffer);
               }
            }

            assert !ItemUtils.isEmpty(resultItem) && !ItemUtils.isEmpty(item1);

            migratedOffers.add(new SKTradeOffer(resultItem, item1, item2));
         } else if (migratedOffers != null) {
            migratedOffers.add(offer);
         }
      }

      return (List)(migratedOffers == null ? offers : migratedOffers);
   }

   public static int updateItems(@ReadWrite List<TradeOffer> offers, String logPrefix) {
      Validate.notNull(logPrefix, (String)"logPrefix is null");
      Validate.notNull(offers, (String)"offers is null");

      assert !CollectionUtils.containsNull(offers);

      int updatedItems = 0;
      int size = offers.size();

      for(int i = 0; i < size; ++i) {
         TradeOffer offer = (TradeOffer)offers.get(i);

         assert offer != null;

         int offerItemsUpdated = 0;
         boolean updateFailed = false;
         UnmodifiableItemStack resultItem = offer.getResultItem();

         assert !ItemUtils.isEmpty(resultItem);

         UnmodifiableItemStack item1 = offer.getItem1();

         assert !ItemUtils.isEmpty(item1);

         UnmodifiableItemStack item2 = offer.getItem2();
         UnmodifiableItemStack updatedResultItem = ItemUpdates.updateItem(resultItem);
         if (updatedResultItem != resultItem) {
            assert updatedResultItem != null && !ItemUtils.isEmpty(updatedResultItem);

            if (ItemUtils.isEmpty(updatedResultItem)) {
               updateFailed = true;
            } else {
               resultItem = updatedResultItem;
               ++offerItemsUpdated;
            }
         }

         UnmodifiableItemStack updatedItem1 = ItemUpdates.updateItem(item1);
         if (updatedItem1 != item1) {
            assert updatedItem1 != null && !ItemUtils.isEmpty(updatedItem1);

            if (ItemUtils.isEmpty(updatedItem1)) {
               updateFailed = true;
            } else {
               item1 = updatedItem1;
               ++offerItemsUpdated;
            }
         }

         UnmodifiableItemStack updatedItem2 = ItemUpdates.updateItem(item2);
         if (updatedItem2 != item2) {
            assert updatedItem2 != null && !ItemUtils.isEmpty(updatedItem2);

            if (ItemUtils.isEmpty(updatedItem2) && !ItemUtils.isEmpty(item2)) {
               updateFailed = true;
            } else {
               item2 = updatedItem2;
               ++offerItemsUpdated;
            }
         }

         if (updateFailed) {
            Log.warning(logPrefix + "Item update failed for trade offer " + (i + 1) + ": " + String.valueOf(offer));
         } else if (offerItemsUpdated > 0) {
            Log.debug(DebugOptions.itemUpdates, logPrefix + "Updated item(s) for trade offer " + (i + 1));
            updatedItems += offerItemsUpdated;

            assert !ItemUtils.isEmpty(resultItem) && !ItemUtils.isEmpty(item1);

            offers.set(i, new SKTradeOffer(resultItem, item1, item2));
         }
      }

      return updatedItems;
   }

   static {
      RESULT_ITEM = (new BasicProperty()).dataKeyAccessor("resultItem", ItemStackSerializers.UNMODIFIABLE).validator(ItemStackValidators.Unmodifiable.NON_EMPTY).build();
      ITEM1 = (new BasicProperty()).dataKeyAccessor("item1", ItemStackSerializers.UNMODIFIABLE).validator(ItemStackValidators.Unmodifiable.NON_EMPTY).build();
      ITEM2 = (new BasicProperty()).dataKeyAccessor("item2", ItemStackSerializers.UNMODIFIABLE).validator(ItemStackValidators.Unmodifiable.NON_EMPTY).nullable().defaultValue((Object)null).build();
      SERIALIZER = new DataSerializer<TradeOffer>() {
         @Nullable
         public Object serialize(TradeOffer value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer offerData = DataContainer.create();
            offerData.set((DataSaver)SKTradeOffer.RESULT_ITEM, value.getResultItem());
            offerData.set((DataSaver)SKTradeOffer.ITEM1, value.getItem1());
            offerData.set((DataSaver)SKTradeOffer.ITEM2, value.getItem2());
            return offerData.serialize();
         }

         public TradeOffer deserialize(Object data) throws InvalidDataException {
            DataContainer offerData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);

            try {
               UnmodifiableItemStack resultItem = (UnmodifiableItemStack)offerData.get((DataLoader)SKTradeOffer.RESULT_ITEM);
               UnmodifiableItemStack item1 = (UnmodifiableItemStack)offerData.get((DataLoader)SKTradeOffer.ITEM1);
               UnmodifiableItemStack item2 = (UnmodifiableItemStack)offerData.get((DataLoader)SKTradeOffer.ITEM2);
               return new SKTradeOffer(resultItem, item1, item2);
            } catch (MissingDataException var6) {
               throw new InvalidDataException(var6.getMessage(), var6);
            }
         }
      };
      LIST_SERIALIZER = new DataSerializer<List<? extends TradeOffer>>() {
         @Nullable
         public Object serialize(@ReadOnly List<? extends TradeOffer> value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer offerListData = DataContainer.create();
            int id = 1;

            for(Iterator var4 = value.iterator(); var4.hasNext(); ++id) {
               TradeOffer offer = (TradeOffer)var4.next();
               Validate.notNull(offer, (String)"list of offers contains null");
               offerListData.set(String.valueOf(id), SKTradeOffer.SERIALIZER.serialize(offer));
            }

            return offerListData.serialize();
         }

         public List<? extends TradeOffer> deserialize(Object data) throws InvalidDataException {
            DataContainer offerListData = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
            Set<? extends String> keys = offerListData.getKeys();
            List<TradeOffer> offers = new ArrayList(keys.size());

            TradeOffer offer;
            for(Iterator var5 = keys.iterator(); var5.hasNext(); offers.add(offer)) {
               String id = (String)var5.next();
               Object offerData = Unsafe.assertNonNull(offerListData.get(id));

               try {
                  offer = (TradeOffer)SKTradeOffer.SERIALIZER.deserialize(offerData);
               } catch (InvalidDataException var10) {
                  throw new InvalidDataException("Invalid trade offer " + id + ": " + var10.getMessage(), var10);
               }
            }

            return offers;
         }
      };
   }
}
