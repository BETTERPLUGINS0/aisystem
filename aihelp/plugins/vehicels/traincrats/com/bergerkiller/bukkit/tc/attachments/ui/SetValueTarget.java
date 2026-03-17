package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.utils.ParseUtil;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public interface SetValueTarget {
   String getAcceptedPropertyName();

   boolean acceptTextValue(String var1);

   default boolean acceptTextValue(SetValueTarget.Operation operation, String value) {
      return operation == SetValueTarget.Operation.SET && this.acceptTextValue(value);
   }

   public static enum Operation {
      SET,
      ADD,
      SUBTRACT;

      public boolean perform(IntSupplier getter, IntConsumer setter, String value) {
         int parsed = ParseUtil.parseInt(value, Integer.MAX_VALUE);
         if (parsed == Integer.MAX_VALUE && (double)parsed != ParseUtil.parseDouble(value, Double.NaN)) {
            return false;
         } else {
            switch(this) {
            case SET:
               setter.accept(parsed);
               return true;
            case ADD:
               setter.accept(getter.getAsInt() + parsed);
               return true;
            case SUBTRACT:
               setter.accept(getter.getAsInt() - parsed);
               return true;
            default:
               return false;
            }
         }
      }

      public boolean perform(DoubleSupplier getter, DoubleConsumer setter, String value) {
         double parsed = ParseUtil.parseDouble(value, Double.NaN);
         if (Double.isNaN(parsed)) {
            return false;
         } else {
            switch(this) {
            case SET:
               setter.accept(parsed);
               return true;
            case ADD:
               setter.accept(getter.getAsDouble() + parsed);
               return true;
            case SUBTRACT:
               setter.accept(getter.getAsDouble() - parsed);
               return true;
            default:
               return false;
            }
         }
      }

      // $FF: synthetic method
      private static SetValueTarget.Operation[] $values() {
         return new SetValueTarget.Operation[]{SET, ADD, SUBTRACT};
      }
   }
}
