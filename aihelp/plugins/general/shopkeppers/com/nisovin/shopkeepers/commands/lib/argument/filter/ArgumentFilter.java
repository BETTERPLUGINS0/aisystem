package com.nisovin.shopkeepers.commands.lib.argument.filter;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ArgumentFilter<T> {
   private static final ArgumentFilter<Object> ACCEPT_ANY = new ArgumentFilter<Object>() {
      public boolean test(CommandInput input, CommandContextView context, @Nullable Object value) {
         return true;
      }
   };

   public static <T> ArgumentFilter<T> acceptAny() {
      return ACCEPT_ANY;
   }

   public abstract boolean test(CommandInput var1, CommandContextView var2, T var3);

   public Text getInvalidArgumentErrorMsg(CommandArgument<?> argument, String argumentInput, T value) {
      return argument.getInvalidArgumentErrorMsg(argumentInput);
   }

   public ArgumentRejectedException rejectedArgumentException(CommandArgument<?> argument, String argumentInput, T value) {
      return new ArgumentRejectedException(argument, this.getInvalidArgumentErrorMsg(argument, argumentInput, value));
   }

   public ArgumentFilter<T> and(final ArgumentFilter<? super T> other) {
      Validate.notNull(other, (String)"other");
      return new ArgumentFilter<T>() {
         public boolean test(CommandInput input, CommandContextView context, T value) {
            return ArgumentFilter.this.test(input, context, value) && other.test(input, context, value);
         }
      };
   }
}
