package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.NumberParseException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.type.range.DoubleRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.Range;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class DoubleParser<C> extends NumberParser<C, Double, DoubleRange> {
   @API(
      status = Status.STABLE
   )
   public static final double DEFAULT_MINIMUM = Double.NEGATIVE_INFINITY;
   @API(
      status = Status.STABLE
   )
   public static final double DEFAULT_MAXIMUM = Double.POSITIVE_INFINITY;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Double> doubleParser() {
      return doubleParser(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Double> doubleParser(final double minValue) {
      return ParserDescriptor.of(new DoubleParser(minValue, Double.POSITIVE_INFINITY), (Class)Double.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Double> doubleParser(final double minValue, final double maxValue) {
      return ParserDescriptor.of(new DoubleParser(minValue, maxValue), (Class)Double.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Double> doubleComponent() {
      return CommandComponent.builder().parser(doubleParser());
   }

   public DoubleParser(final double min, final double max) {
      super(Range.doubleRange(min, max));
   }

   @NonNull
   public ArgumentParseResult<Double> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return !commandInput.isValidDouble((DoubleRange)this.range()) ? ArgumentParseResult.failure(new DoubleParser.DoubleParseException(commandInput.peekString(), this, commandContext)) : ArgumentParseResult.success(commandInput.readDouble());
   }

   public boolean hasMax() {
      return ((DoubleRange)this.range()).maxDouble() != Double.POSITIVE_INFINITY;
   }

   public boolean hasMin() {
      return ((DoubleRange)this.range()).minDouble() != Double.NEGATIVE_INFINITY;
   }

   @API(
      status = Status.STABLE
   )
   public static final class DoubleParseException extends NumberParseException {
      @API(
         status = Status.STABLE
      )
      public DoubleParseException(@NonNull final String input, @NonNull final DoubleParser<?> parser, @NonNull final CommandContext<?> commandContext) {
         super(input, parser, commandContext);
      }

      @NonNull
      public String numberType() {
         return "double";
      }
   }
}
