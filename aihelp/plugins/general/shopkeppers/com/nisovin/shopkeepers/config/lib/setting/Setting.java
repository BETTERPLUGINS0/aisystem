package com.nisovin.shopkeepers.config.lib.setting;

import com.nisovin.shopkeepers.config.lib.Config;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Setting<T> {
   Config getConfig();

   String getConfigKey();

   ValueType<T> getValueType();

   @Nullable
   T getValue();

   void setValue(@Nullable T var1) throws ValueLoadException;
}
