package com.nisovin.shopkeepers.util.data.property.validation;

import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ChainedPropertyValidator<T> implements PropertyValidator<T> {
   private final PropertyValidator<? super T> first;
   private final PropertyValidator<? super T> second;

   public ChainedPropertyValidator(PropertyValidator<? super T> first, PropertyValidator<? super T> second) {
      Validate.notNull(first, (String)"first is null");
      Validate.notNull(second, (String)"second is null");
      this.first = first;
      this.second = second;
   }

   public void validate(@NonNull T value) {
      this.first.validate(value);
      this.second.validate(value);
   }
}
