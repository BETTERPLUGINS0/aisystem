package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.NumberParseException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.type.range.FloatRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.Range;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class FloatParser<C> extends NumberParser<C, Float, FloatRange> {
   @API(
      status = Status.STABLE
   )
   public static final float DEFAULT_MINIMUM = Float.NEGATIVE_INFINITY;
   @API(
      status = Status.STABLE
   )
   public static final float DEFAULT_MAXIMUM = Float.POSITIVE_INFINITY;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Float> floatParser() {
      return floatParser(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Float> floatParser(final float minValue) {
      return ParserDescriptor.of(new FloatParser(minValue, Float.POSITIVE_INFINITY), (Class)Float.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Float> floatParser(final float minValue, final float maxValue) {
      return ParserDescriptor.of(new FloatParser(minValue, maxValue), (Class)Float.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Float> floatComponent() {
      return CommandComponent.builder().parser(floatParser());
   }

   public FloatParser(final float min, final float max) {
      super(Range.floatRange(min, max));
   }

   @NonNull
   public ArgumentParseResult<Float> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return !commandInput.isValidFloat((FloatRange)this.range()) ? ArgumentParseResult.failure(new FloatParser.FloatParseException(commandInput.peekString(), this, commandContext)) : ArgumentParseResult.success(commandInput.readFloat());
   }

   public boolean hasMax() {
      return ((FloatRange)this.range()).maxFloat() != Float.POSITIVE_INFINITY;
   }

   public boolean hasMin() {
      return ((FloatRange)this.range()).minFloat() != Float.NEGATIVE_INFINITY;
   }

   @API(
      status = Status.STABLE
   )
   public static final class FloatParseException extends NumberParseException {
      @API(
         status = Status.STABLE
      )
      public FloatParseException(@NonNull final String input, @NonNull final FloatParser<?> parser, @NonNull final CommandContext<?> commandContext) {
         super(input, parser, commandContext);
      }

      @NonNull
      public String numberType() {
         return "float";
      }
   }
}
