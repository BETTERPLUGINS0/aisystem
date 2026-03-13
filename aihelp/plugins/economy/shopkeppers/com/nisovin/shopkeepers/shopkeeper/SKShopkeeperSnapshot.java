package com.nisovin.shopkeepers.shopkeeper;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperSnapshot;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.DataContainerSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.InstantSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SKShopkeeperSnapshot implements ShopkeeperSnapshot {
   public static final int MAX_NAME_LENGTH = 64;
   private static final Property<String> NAME;
   private static final Property<Instant> TIMESTAMP;
   private static final Property<DataContainer> DATA;
   public static final DataSerializer<SKShopkeeperSnapshot> SERIALIZER;
   public static final DataSerializer<List<? extends SKShopkeeperSnapshot>> LIST_SERIALIZER;
   private final String name;
   private final Instant timestamp;
   private final ShopkeeperData shopkeeperData;

   public static boolean isNameValid(String name) {
      try {
         validateName(name);
         return true;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   public static void validateName(String name) {
      Validate.notEmpty(name, "name is null or empty");
      Validate.isTrue(name.length() <= 64, () -> {
         return "name is more than 64 characters long: " + name;
      });
      Validate.isTrue(!TextUtils.containsColorChar(name), () -> {
         return "name contains color code character '§': " + name;
      });
   }

   public SKShopkeeperSnapshot(String name, Instant timestamp, ShopkeeperData shopkeeperData) {
      validateName(name);
      Validate.notNull(timestamp, (String)"timestamp is null");
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      this.name = name;
      this.timestamp = timestamp;
      this.shopkeeperData = shopkeeperData;
   }

   public final String getName() {
      return this.name;
   }

   public final Instant getTimestamp() {
      return this.timestamp;
   }

   public final ShopkeeperData getShopkeeperData() {
      return this.shopkeeperData;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getSimpleName() + " [name=");
      builder.append(this.name);
      builder.append(", timestamp=");
      builder.append(this.timestamp);
      builder.append(", shopkeeperData=");
      builder.append(this.shopkeeperData);
      builder.append("]");
      return builder.toString();
   }

   public final int hashCode() {
      return super.hashCode();
   }

   public final boolean equals(@Nullable Object obj) {
      return super.equals(obj);
   }

   static {
      NAME = (new BasicProperty()).dataKeyAccessor("name", StringSerializers.SCALAR).validator(SKShopkeeperSnapshot::validateName).build();
      TIMESTAMP = (new BasicProperty()).dataKeyAccessor("timestamp", InstantSerializers.ISO).build();
      DATA = (new BasicProperty()).dataKeyAccessor("data", DataContainerSerializers.DEFAULT).build();
      SERIALIZER = new DataSerializer<SKShopkeeperSnapshot>() {
         @Nullable
         public Object serialize(SKShopkeeperSnapshot value) {
            Validate.notNull(value, (String)"value is null");
            DataContainer dataContainer = DataContainer.create();
            dataContainer.set((DataSaver)SKShopkeeperSnapshot.NAME, value.getName());
            dataContainer.set((DataSaver)SKShopkeeperSnapshot.TIMESTAMP, value.getTimestamp());
            dataContainer.set((DataSaver)SKShopkeeperSnapshot.DATA, value.getShopkeeperData());
            return dataContainer.serialize();
         }

         public SKShopkeeperSnapshot deserialize(Object data) throws InvalidDataException {
            DataContainer dataContainer = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);

            try {
               String name = (String)dataContainer.get((DataLoader)SKShopkeeperSnapshot.NAME);
               Instant timestamp = (Instant)dataContainer.get((DataLoader)SKShopkeeperSnapshot.TIMESTAMP);
               ShopkeeperData shopkeeperData = ShopkeeperData.ofNonNull((DataContainer)dataContainer.get((DataLoader)SKShopkeeperSnapshot.DATA));
               return new SKShopkeeperSnapshot(name, timestamp, shopkeeperData);
            } catch (MissingDataException var6) {
               throw new InvalidDataException(var6.getMessage(), var6);
            }
         }
      };
      LIST_SERIALIZER = new DataSerializer<List<? extends SKShopkeeperSnapshot>>() {
         @Nullable
         public Object serialize(@ReadOnly List<? extends SKShopkeeperSnapshot> value) {
            Validate.notNull(value, (String)"value is null");
            List<Object> snapshotListData = new ArrayList(value.size());
            value.forEach((snapshot) -> {
               Object snapshotData = Unsafe.assertNonNull(SKShopkeeperSnapshot.SERIALIZER.serialize(snapshot));
               snapshotListData.add(snapshotData);
            });
            return snapshotListData;
         }

         public List<? extends SKShopkeeperSnapshot> deserialize(Object data) throws InvalidDataException {
            Validate.notNull(data, "data is null");
            if (!(data instanceof List)) {
               throw new InvalidDataException("Data is not a List, but of type " + data.getClass().getName() + "!");
            } else {
               List<?> snapshotListData = (List)data;
               List<SKShopkeeperSnapshot> snapshots = new ArrayList(snapshotListData.size());
               Iterator var4 = snapshotListData.iterator();

               while(var4.hasNext()) {
                  Object snapshotData = var4.next();

                  try {
                     if (snapshotData == null) {
                        throw new InvalidDataException("Data is null!");
                     }

                     assert snapshotData != null;

                     SKShopkeeperSnapshot snapshot = (SKShopkeeperSnapshot)SKShopkeeperSnapshot.SERIALIZER.deserialize(snapshotData);
                     snapshots.add(snapshot);
                  } catch (InvalidDataException var8) {
                     int snapshotNumber = snapshots.size() + 1;
                     throw new InvalidDataException("Shopkeeper snapshot " + snapshotNumber + " is invalid: " + var8.getMessage(), var8);
                  }
               }

               return snapshots;
            }
         }
      };
   }
}
