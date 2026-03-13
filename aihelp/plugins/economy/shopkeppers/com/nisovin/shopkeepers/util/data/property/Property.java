package com.nisovin.shopkeepers.util.data.property;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.serialization.DataAccessor;
import com.nisovin.shopkeepers.util.data.serialization.DataLoader;
import com.nisovin.shopkeepers.util.data.serialization.DataSaver;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Property<T> extends DataAccessor<T> {
   String getName();

   boolean isNullable();

   boolean hasDefaultValue();

   T getDefaultValue();

   void validateValue(@Nullable T var1);

   String toString(@Nullable T var1);

   DataLoader<? extends T> getLoader();

   DataSaver<? super T> getSaver();

   DataLoader<? extends T> getUnvalidatedLoader();

   DataSaver<? super T> getUnvalidatedSaver();

   void save(DataContainer var1, @Nullable T var2);

   T load(DataContainer var1) throws InvalidDataException;

   T loadOrDefault(DataContainer var1) throws InvalidDataException;
}
