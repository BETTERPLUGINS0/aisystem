package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.NumberParseException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.type.range.Range;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ShortRange;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class ShortParser<C> extends NumberParser<C, Short, ShortRange> implements BlockingSuggestionProvider.Strings<C> {
   @API(
      status = Status.STABLE
   )
   public static final short DEFAULT_MINIMUM = -32768;
   @API(
      status = Status.STABLE
   )
   public static final short DEFAULT_MAXIMUM = 32767;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Short> shortParser() {
      return shortParser((short)-32768, (short)32767);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Short> shortParser(final short minValue) {
      return ParserDescriptor.of(new ShortParser(minValue, (short)32767), (Class)Short.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Short> shortParser(final short minValue, final short maxValue) {
      return ParserDescriptor.of(new ShortParser(minValue, maxValue), (Class)Short.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Short> shortComponent() {
      return CommandComponent.builder().parser(shortParser());
   }

   public ShortParser(final short min, final short max) {
      super(Range.shortRange(min, max));
   }

   @NonNull
   public ArgumentParseResult<Short> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return !commandInput.isValidShort((ShortRange)this.range()) ? ArgumentParseResult.failure(new ShortParser.ShortParseException(commandInput.peekString(), this, commandContext)) : ArgumentParseResult.success(commandInput.readShort());
   }

   public boolean hasMax() {
      return ((ShortRange)this.range()).maxShort() != 32767;
   }

   public boolean hasMin() {
      return ((ShortRange)this.range()).minShort() != -32768;
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return IntegerParser.getSuggestions(this.range(), input);
   }

   @API(
      status = Status.STABLE
   )
   public static final class ShortParseException extends NumberParseException {
      @API(
         status = Status.STABLE
      )
      public ShortParseException(@NonNull final String input, @NonNull final ShortParser<?> parser, @NonNull final CommandContext<?> commandContext) {
         super(input, parser, commandContext);
      }

      @NonNull
      public String numberType() {
         return "short";
      }
   }
}
