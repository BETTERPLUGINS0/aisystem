package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.NumberParseException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.type.range.LongRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.Range;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class LongParser<C> extends NumberParser<C, Long, LongRange> implements BlockingSuggestionProvider.Strings<C> {
   @API(
      status = Status.STABLE
   )
   public static final long DEFAULT_MINIMUM = Long.MIN_VALUE;
   @API(
      status = Status.STABLE
   )
   public static final long DEFAULT_MAXIMUM = Long.MAX_VALUE;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Long> longParser() {
      return longParser(Long.MIN_VALUE, Long.MAX_VALUE);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Long> longParser(final long minValue) {
      return ParserDescriptor.of(new LongParser(minValue, Long.MAX_VALUE), (Class)Long.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Long> longParser(final long minValue, final long maxValue) {
      return ParserDescriptor.of(new LongParser(minValue, maxValue), (Class)Long.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Long> longComponent() {
      return CommandComponent.builder().parser(longParser());
   }

   public LongParser(final long min, final long max) {
      super(Range.longRange(min, max));
   }

   @NonNull
   public ArgumentParseResult<Long> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return !commandInput.isValidLong((LongRange)this.range()) ? ArgumentParseResult.failure(new LongParser.LongParseException(commandInput.peekString(), this, commandContext)) : ArgumentParseResult.success(commandInput.readLong());
   }

   public boolean hasMax() {
      return ((LongRange)this.range()).maxLong() != Long.MAX_VALUE;
   }

   public boolean hasMin() {
      return ((LongRange)this.range()).minLong() != Long.MIN_VALUE;
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return IntegerParser.getSuggestions(this.range(), input);
   }

   @API(
      status = Status.STABLE
   )
   public static final class LongParseException extends NumberParseException {
      @API(
         status = Status.STABLE
      )
      public LongParseException(@NonNull final String input, @NonNull final LongParser<?> parser, @NonNull final CommandContext<?> commandContext) {
         super(input, parser, commandContext);
      }

      @NonNull
      public String numberType() {
         return "long";
      }
   }
}
