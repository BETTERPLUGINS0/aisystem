package com.nisovin.shopkeepers.util.data.property;

import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
public interface StringConverter<T> {
   StringConverter<Object> DEFAULT = String::valueOf;

   String toString(@Nullable T var1);
}
