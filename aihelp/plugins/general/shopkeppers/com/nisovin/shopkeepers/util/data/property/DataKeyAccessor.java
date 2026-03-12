package com.nisovin.shopkeepers.util.data.property;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.serialization.DataAccessor;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.MissingDataException;
import com.nisovin.shopkeepers.util.java.PredicateUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class DataKeyAccessor<T> implements DataAccessor<T> {
   private final String dataKey;
   private final DataSerializer<T> serializer;
   private Predicate<Object> emptyDataPredicate = PredicateUtils.alwaysFalse();

   public DataKeyAccessor(String dataKey, DataSerializer<T> serializer) {
      Validate.notEmpty(dataKey, "dataKey is null or empty");
      Validate.notNull(serializer, (String)"serializer is null");
      this.dataKey = dataKey;
      this.serializer = serializer;
   }

   public final String getDataKey() {
      return this.dataKey;
   }

   public final DataSerializer<T> getSerializer() {
      return this.serializer;
   }

   public final <A extends DataKeyAccessor<T>> A emptyDataPredicate(Predicate<Object> emptyDataPredicate) {
      Validate.notNull(emptyDataPredicate, (String)"emptyDataPredicate is null");
      this.emptyDataPredicate = emptyDataPredicate;
      return this;
   }

   public final void save(DataContainer dataContainer, @Nullable T value) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      if (value == null) {
         this.setData(dataContainer, (Object)null);
      } else {
         Object serialized = this.serializer.serialize(value);
         this.setData(dataContainer, serialized);
      }

   }

   @NonNull
   public final T load(DataContainer dataContainer) throws InvalidDataException {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      Object data = this.getData(dataContainer);
      if (data != null && !this.emptyDataPredicate.test(data)) {
         Object value;
         try {
            value = this.serializer.deserialize(data);
         } catch (MissingDataException var5) {
            throw new RuntimeException("Serializer of type " + this.serializer.getClass().getName() + " threw an unexpected " + MissingDataException.class.getName() + " during deserialization: " + String.valueOf(data), var5);
         }

         if (Unsafe.cast(value) == null) {
            String var10002 = this.getClass().getName();
            throw new RuntimeException("Serializer of type " + var10002 + " deserialized data to null: " + String.valueOf(data));
         } else {
            return value;
         }
      } else {
         throw this.missingDataError();
      }
   }

   protected MissingDataException missingDataError() {
      return new MissingDataException("Missing data.");
   }

   @Nullable
   private final Object getData(DataContainer dataContainer) {
      assert dataContainer != null;

      return dataContainer.get(this.dataKey);
   }

   private final void setData(DataContainer dataContainer, @Nullable Object data) {
      assert dataContainer != null;

      dataContainer.set(this.dataKey, data);
   }
}
