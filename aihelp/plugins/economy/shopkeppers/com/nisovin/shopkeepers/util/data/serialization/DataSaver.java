package com.nisovin.shopkeepers.util.data.serialization;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface DataSaver<T> {
   void save(DataContainer var1, @Nullable T var2);
}
