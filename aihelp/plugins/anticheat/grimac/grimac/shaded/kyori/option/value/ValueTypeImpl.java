package ac.grim.grimac.shaded.kyori.option.value;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.jspecify.annotations.Nullable;

abstract class ValueTypeImpl<T> implements ValueType<T> {
   private final Class<T> type;

   ValueTypeImpl(final Class<T> type) {
      this.type = type;
   }

   public Class<T> type() {
      return this.type;
   }

   static IllegalArgumentException doNotKnowHowToTurn(final String input, final Class<?> expected, @Nullable final String message) {
      throw new IllegalArgumentException("Do not know how to turn value '" + input + "' into a " + expected.getName() + (message == null ? "" : ": " + message));
   }

   static final class EnumType<E extends Enum<E>> extends ValueTypeImpl<E> {
      private final Map<String, E> values = new HashMap();

      EnumType(final Class<E> type) {
         super(type);
         Enum[] var2 = (Enum[])type.getEnumConstants();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            E entry = var2[var4];
            this.values.put(entry.name().toLowerCase(Locale.ROOT), entry);
         }

      }

      public E parse(final String plainValue) throws IllegalArgumentException {
         E result = (Enum)this.values.get(plainValue.toLowerCase(Locale.ROOT));
         if (result == null) {
            throw doNotKnowHowToTurn(plainValue, this.type(), (String)null);
         } else {
            return result;
         }
      }
   }

   static final class Types {
      static ValueType<String> STRING = new ValueTypeImpl<String>(String.class) {
         public String parse(final String plainValue) throws IllegalArgumentException {
            return plainValue;
         }
      };
      static ValueType<Boolean> BOOLEAN = new ValueTypeImpl<Boolean>(Boolean.class) {
         public Boolean parse(final String plainValue) throws IllegalArgumentException {
            if (plainValue.equalsIgnoreCase("true")) {
               return Boolean.TRUE;
            } else if (plainValue.equalsIgnoreCase("false")) {
               return Boolean.FALSE;
            } else {
               throw doNotKnowHowToTurn(plainValue, Boolean.class, (String)null);
            }
         }
      };
      static ValueType<Integer> INT = new ValueTypeImpl<Integer>(Integer.class) {
         public Integer parse(final String plainValue) throws IllegalArgumentException {
            try {
               return Integer.decode(plainValue);
            } catch (NumberFormatException var3) {
               throw doNotKnowHowToTurn(plainValue, Integer.class, var3.getMessage());
            }
         }
      };
      static ValueType<Double> DOUBLE = new ValueTypeImpl<Double>(Double.class) {
         public Double parse(final String plainValue) throws IllegalArgumentException {
            try {
               return Double.parseDouble(plainValue);
            } catch (NumberFormatException var3) {
               throw doNotKnowHowToTurn(plainValue, Double.class, var3.getMessage());
            }
         }
      };

      private Types() {
      }
   }
}
