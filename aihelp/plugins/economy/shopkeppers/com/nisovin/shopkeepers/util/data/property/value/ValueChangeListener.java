package com.nisovin.shopkeepers.util.data.property.value;

import java.util.Set;

@FunctionalInterface
public interface ValueChangeListener<T> {
   void onValueChanged(PropertyValue<T> var1, T var2, T var3, Set<? extends PropertyValue.UpdateFlag> var4);
}
