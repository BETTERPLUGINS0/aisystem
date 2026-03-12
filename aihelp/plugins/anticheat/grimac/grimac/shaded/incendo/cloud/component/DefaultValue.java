package ac.grim.grimac.shaded.incendo.cloud.component;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
@FunctionalInterface
public interface DefaultValue<C, T> {
   @NonNull
   static <C, T> DefaultValue<C, T> constant(@NonNull final T value) {
      return new DefaultValue.ConstantDefaultValue(Objects.requireNonNull(value, "value"));
   }

   @NonNull
   static <C, T> DefaultValue<C, T> dynamic(@NonNull final DefaultValue.DefaultValueProvider<C, T> expression) {
      Objects.requireNonNull(expression, "expression");
      return failableDynamic((ctx) -> {
         return ArgumentParseResult.success(expression.evaluateDefault(ctx));
      });
   }

   @NonNull
   static <C, T> DefaultValue<C, T> failableDynamic(@NonNull final DefaultValue<C, T> expression) {
      return new DefaultValue.DynamicDefaultValue((DefaultValue)Objects.requireNonNull(expression, "expression"));
   }

   @NonNull
   static <C, T> DefaultValue<C, T> parsed(@NonNull final String value) {
      return new DefaultValue.ParsedDefaultValue(value);
   }

   @NonNull
   ArgumentParseResult<T> evaluateDefault(@NonNull CommandContext<C> context);

   public static final class ConstantDefaultValue<C, T> implements DefaultValue<C, T> {
      private final ArgumentParseResult<T> value;

      private ConstantDefaultValue(@NonNull final T value) {
         this.value = ArgumentParseResult.success(value);
      }

      @NonNull
      public ArgumentParseResult<T> evaluateDefault(@NonNull final CommandContext<C> context) {
         return this.value;
      }

      public boolean equals(final Object object) {
         if (this == object) {
            return true;
         } else if (object != null && this.getClass() == object.getClass()) {
            DefaultValue.ConstantDefaultValue<?, ?> that = (DefaultValue.ConstantDefaultValue)object;
            return Objects.equals(this.value.parsedValue().get(), that.value.parsedValue().get());
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.value});
      }

      // $FF: synthetic method
      ConstantDefaultValue(Object x0, Object x1) {
         this(x0);
      }
   }

   @API(
      status = Status.STABLE
   )
   @FunctionalInterface
   public interface DefaultValueProvider<C, T> {
      @NonNull
      T evaluateDefault(@NonNull CommandContext<C> context);
   }

   public static final class DynamicDefaultValue<C, T> implements DefaultValue<C, T> {
      private final DefaultValue<C, T> defaultValue;

      private DynamicDefaultValue(@NonNull final DefaultValue<C, T> defaultValue) {
         this.defaultValue = defaultValue;
      }

      @NonNull
      public ArgumentParseResult<T> evaluateDefault(@NonNull final CommandContext<C> context) {
         return this.defaultValue.evaluateDefault(context);
      }

      public boolean equals(final Object object) {
         if (this == object) {
            return true;
         } else if (object != null && this.getClass() == object.getClass()) {
            DefaultValue.DynamicDefaultValue<?, ?> that = (DefaultValue.DynamicDefaultValue)object;
            return Objects.equals(this.defaultValue, that.defaultValue);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.defaultValue});
      }

      // $FF: synthetic method
      DynamicDefaultValue(DefaultValue x0, Object x1) {
         this(x0);
      }
   }

   public static final class ParsedDefaultValue<C, T> implements DefaultValue<C, T> {
      private final String value;

      private ParsedDefaultValue(@NonNull final String string) {
         this.value = string;
      }

      @NonNull
      public ArgumentParseResult<T> evaluateDefault(@NonNull final CommandContext<C> context) {
         throw new UnsupportedOperationException();
      }

      @NonNull
      public String value() {
         return this.value;
      }

      public boolean equals(final Object object) {
         if (this == object) {
            return true;
         } else if (object != null && this.getClass() == object.getClass()) {
            DefaultValue.ParsedDefaultValue<?, ?> that = (DefaultValue.ParsedDefaultValue)object;
            return Objects.equals(this.value, that.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.value});
      }

      // $FF: synthetic method
      ParsedDefaultValue(String x0, Object x1) {
         this(x0);
      }
   }
}
