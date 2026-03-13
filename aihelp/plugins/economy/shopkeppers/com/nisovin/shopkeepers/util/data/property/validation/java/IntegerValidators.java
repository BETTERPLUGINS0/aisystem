package com.nisovin.shopkeepers.util.data.property.validation.java;

import com.nisovin.shopkeepers.util.data.property.validation.PropertyValidator;
import com.nisovin.shopkeepers.util.java.Validate;

public final class IntegerValidators {
   public static final PropertyValidator<Integer> POSITIVE = new IntegerValidators.BoundedIntegerValidator(1, Integer.MAX_VALUE) {
      protected String getOutOfBoundsMessage(int value) {
         return "Value has to be positive, but is " + value + ".";
      }
   };
   public static final PropertyValidator<Integer> NON_NEGATIVE = new IntegerValidators.BoundedIntegerValidator(0, Integer.MAX_VALUE) {
      protected String getOutOfBoundsMessage(int value) {
         return "Value cannot be negative, but is " + value + ".";
      }
   };

   public static PropertyValidator<Integer> bounded(int minValue, int maxValue) {
      return new IntegerValidators.BoundedIntegerValidator(minValue, maxValue);
   }

   private IntegerValidators() {
   }

   private static class BoundedIntegerValidator implements PropertyValidator<Integer> {
      private final int minValue;
      private final int maxValue;

      public BoundedIntegerValidator(int minValue, int maxValue) {
         Validate.isTrue(minValue <= maxValue, () -> {
            return "minValue (" + minValue + ") is greater than maxValue (" + maxValue + ")";
         });
         this.minValue = minValue;
         this.maxValue = maxValue;
      }

      private boolean isInBounds(int value) {
         return value >= this.minValue && value <= this.maxValue;
      }

      public void validate(Integer value) {
         Validate.notNull(value, (String)"value is null");
         Validate.isTrue(this.isInBounds(value), () -> {
            return this.getOutOfBoundsMessage(value);
         });
      }

      protected String getOutOfBoundsMessage(int value) {
         return "Value is out of bounds: min=" + this.minValue + ", max=" + this.maxValue + ", value" + value + ".";
      }
   }
}
