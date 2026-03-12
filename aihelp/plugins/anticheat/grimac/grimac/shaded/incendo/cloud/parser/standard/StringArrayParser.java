package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class StringArrayParser<C> implements ArgumentParser<C, String[]> {
   private static final Pattern FLAG_PATTERN = Pattern.compile("(-[A-Za-z_\\-0-9])|(--[A-Za-z_\\-0-9]*)");
   private final boolean flagYielding;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String[]> stringArrayParser() {
      return ParserDescriptor.of(new StringArrayParser(), (Class)String[].class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String[]> flagYieldingStringArrayParser() {
      return ParserDescriptor.of(new StringArrayParser(true), (Class)String[].class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, String[]> characterComponent() {
      return CommandComponent.builder().parser(stringArrayParser());
   }

   public StringArrayParser() {
      this.flagYielding = false;
   }

   @API(
      status = Status.STABLE
   )
   public StringArrayParser(final boolean flagYielding) {
      this.flagYielding = flagYielding;
   }

   @NonNull
   public ArgumentParseResult<String[]> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      int size = commandInput.remainingTokens();
      int i;
      if (this.flagYielding) {
         List<String> result = new LinkedList();

         for(i = 0; i < size; ++i) {
            String string = commandInput.peekString();
            if (string.isEmpty() || FLAG_PATTERN.matcher(string).matches()) {
               break;
            }

            result.add(commandInput.readString());
         }

         return ArgumentParseResult.success((String[])result.toArray(new String[0]));
      } else {
         String[] result = new String[size];

         for(i = 0; i < result.length; ++i) {
            result[i] = commandInput.readString();
         }

         return ArgumentParseResult.success(result);
      }
   }
}
