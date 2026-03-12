package com.nisovin.shopkeepers.util.data.serialization;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public interface DataLoader<T> {
   T load(DataContainer var1) throws InvalidDataException;
}
