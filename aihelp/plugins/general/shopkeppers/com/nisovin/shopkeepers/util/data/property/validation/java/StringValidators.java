package com.nisovin.shopkeepers.util.data.property.validation.java;

import com.nisovin.shopkeepers.util.data.property.validation.PropertyValidator;
import com.nisovin.shopkeepers.util.java.Validate;

public final class StringValidators {
   public static final PropertyValidator<String> NON_EMPTY = (value) -> {
      Validate.isTrue(!value.isEmpty(), "String is empty!");
   };

   private StringValidators() {
   }
}
