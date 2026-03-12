package com.nisovin.shopkeepers.util.data.serialization;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface DataSerializer<T> {
   @Nullable
   Object serialize(T var1);

   @NonNull
   T deserialize(Object var1) throws InvalidDataException;
}
