package com.nisovin.shopkeepers.util.data.property.validation;

import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface PropertyValidator<T> {
   void validate(@NonNull T var1);
}
