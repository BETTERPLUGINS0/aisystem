package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "ParserDescriptor",
   generator = "Immutables"
)
@Immutable
final class ParserDescriptorImpl<C, T> implements ParserDescriptor<C, T> {
   @NonNull
   private final ArgumentParser<C, T> parser;
   @NonNull
   private final TypeToken<T> valueType;

   private ParserDescriptorImpl(@NonNull ArgumentParser<C, T> parser, @NonNull TypeToken<T> valueType) {
      this.parser = (ArgumentParser)Objects.requireNonNull(parser, "parser");
      this.valueType = (TypeToken)Objects.requireNonNull(valueType, "valueType");
   }

   private ParserDescriptorImpl(ParserDescriptorImpl<C, T> original, @NonNull ArgumentParser<C, T> parser, @NonNull TypeToken<T> valueType) {
      this.parser = parser;
      this.valueType = valueType;
   }

   @NonNull
   public ArgumentParser<C, T> parser() {
      return this.parser;
   }

   @NonNull
   public TypeToken<T> valueType() {
      return this.valueType;
   }

   public final ParserDescriptorImpl<C, T> withParser(ArgumentParser<C, T> value) {
      if (this.parser == value) {
         return this;
      } else {
         ArgumentParser<C, T> newValue = (ArgumentParser)Objects.requireNonNull(value, "parser");
         return new ParserDescriptorImpl(this, newValue, this.valueType);
      }
   }

   public final ParserDescriptorImpl<C, T> withValueType(TypeToken<T> value) {
      if (this.valueType == value) {
         return this;
      } else {
         TypeToken<T> newValue = (TypeToken)Objects.requireNonNull(value, "valueType");
         return new ParserDescriptorImpl(this, this.parser, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof ParserDescriptorImpl && this.equalTo(0, (ParserDescriptorImpl)another);
      }
   }

   private boolean equalTo(int synthetic, ParserDescriptorImpl<?, ?> another) {
      return this.parser.equals(another.parser) && this.valueType.equals(another.valueType);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.parser.hashCode();
      h += (h << 5) + this.valueType.hashCode();
      return h;
   }

   public String toString() {
      return "ParserDescriptor{parser=" + this.parser + ", valueType=" + this.valueType + "}";
   }

   public static <C, T> ParserDescriptorImpl<C, T> of(@NonNull ArgumentParser<C, T> parser, @NonNull TypeToken<T> valueType) {
      return new ParserDescriptorImpl(parser, valueType);
   }

   public static <C, T> ParserDescriptorImpl<C, T> copyOf(ParserDescriptor<C, T> instance) {
      return instance instanceof ParserDescriptorImpl ? (ParserDescriptorImpl)instance : of(instance.parser(), instance.valueType());
   }
}
