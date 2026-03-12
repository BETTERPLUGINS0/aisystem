package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.NumberParseException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ByteRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.Range;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class ByteParser<C> extends NumberParser<C, Byte, ByteRange> implements BlockingSuggestionProvider.Strings<C> {
   @API(
      status = Status.STABLE
   )
   public static final byte DEFAULT_MINIMUM = -128;
   @API(
      status = Status.STABLE
   )
   public static final byte DEFAULT_MAXIMUM = 127;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Byte> byteParser() {
      return byteParser((byte)-128, (byte)127);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Byte> byteParser(final byte minValue) {
      return ParserDescriptor.of(new ByteParser(minValue, (byte)127), (Class)Byte.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Byte> byteParser(final byte minValue, final byte maxValue) {
      return ParserDescriptor.of(new ByteParser(minValue, maxValue), (Class)Byte.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Byte> byteComponent() {
      return CommandComponent.builder().parser(byteParser());
   }

   public ByteParser(final byte min, final byte max) {
      super(Range.byteRange(min, max));
   }

   @NonNull
   public ArgumentParseResult<Byte> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return !commandInput.isValidByte((ByteRange)this.range()) ? ArgumentParseResult.failure(new ByteParser.ByteParseException(commandInput.peekString(), this, commandContext)) : ArgumentParseResult.success(commandInput.readByte());
   }

   public boolean hasMax() {
      return ((ByteRange)this.range()).maxByte() != 127;
   }

   public boolean hasMin() {
      return ((ByteRange)this.range()).minByte() != -128;
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return IntegerParser.getSuggestions(this.range(), input);
   }

   @API(
      status = Status.STABLE
   )
   public static final class ByteParseException extends NumberParseException {
      @API(
         status = Status.STABLE
      )
      public ByteParseException(@NonNull final String input, @NonNull final ByteParser<?> parser, @NonNull final CommandContext<?> commandContext) {
         super(input, parser, commandContext);
      }

      @NonNull
      public String numberType() {
         return "byte";
      }
   }
}
