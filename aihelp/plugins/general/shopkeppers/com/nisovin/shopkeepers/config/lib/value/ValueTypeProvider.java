package com.nisovin.shopkeepers.config.lib.value;

import java.lang.reflect.Type;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ValueTypeProvider {
   @Nullable
   ValueType<?> get(Type var1);
}
