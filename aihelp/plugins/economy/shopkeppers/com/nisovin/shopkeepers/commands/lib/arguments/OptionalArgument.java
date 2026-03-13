package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class OptionalArgument<T> extends FallbackArgument<T> {
   protected final CommandArgument<T> argument;

   public OptionalArgument(CommandArgument<T> argument) {
      super(((CommandArgument)Validate.notNull(argument, (String)"argument is null")).getName());
      argument.setParent(this);
      this.argument = argument;
   }

   public boolean isOptional() {
      return true;
   }

   public String getReducedFormat() {
      return this.argument.getReducedFormat();
   }

   public Text getRequiresPlayerErrorMsg() {
      return this.argument.getRequiresPlayerErrorMsg();
   }

   public Text getMissingArgumentErrorMsg() {
      return this.argument.getMissingArgumentErrorMsg();
   }

   public Text getInvalidArgumentErrorMsg(String argumentInput) {
      return this.argument.getInvalidArgumentErrorMsg(argumentInput);
   }

   private T parseOrNull(OptionalArgument.Parser<T> parser, ArgumentsReader argsReader) throws FallbackArgumentException {
      ArgumentsReader argsReaderState = argsReader.createSnapshot();

      Object value;
      try {
         value = parser.parse();
      } catch (FallbackArgumentException var6) {
         throw new FallbackArgumentException(this, var6);
      } catch (ArgumentParseException var7) {
         argsReader.setState(argsReaderState);
         value = null;
      }

      return value;
   }

   @Nullable
   public T parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.parseOrNull(() -> {
         return this.argument.parse(input, context, argsReader);
      }, argsReader);
   }

   @Nullable
   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.parseOrNull(() -> {
         return this.argument.parseValue(input, context, argsReader);
      }, argsReader);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.argument.complete(input, context, argsReader);
   }

   @Nullable
   public T parseFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) throws ArgumentParseException {
      FallbackArgumentException originalFallback = (FallbackArgumentException)fallbackException.getOriginalException();
      return this.parseOrNull(() -> {
         return ((FallbackArgument)this.argument).parseFallback(input, context, argsReader, originalFallback, parsingFailed);
      }, argsReader);
   }

   @FunctionalInterface
   private interface Parser<T> {
      T parse() throws ArgumentParseException;
   }
}
