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

public class TypedFallbackArgument<T> extends FallbackArgument<T> {
   private final AnyFallbackArgument anyFallbackArgument;

   public TypedFallbackArgument(CommandArgument<T> argument, CommandArgument<T> fallbackArgument) {
      super(((CommandArgument)Validate.notNull(argument, (String)"argument is null")).getName());
      this.anyFallbackArgument = new AnyFallbackArgument(argument, fallbackArgument);
      this.anyFallbackArgument.setParent(this);
   }

   public CommandArgument<T> getOriginalArgument() {
      return this.anyFallbackArgument.getOriginalArgument();
   }

   public CommandArgument<T> getFallbackArgument() {
      return this.anyFallbackArgument.getFallbackArgument();
   }

   public String getReducedFormat() {
      return this.anyFallbackArgument.getReducedFormat();
   }

   public boolean isOptional() {
      return this.anyFallbackArgument.isOptional();
   }

   public Text getMissingArgumentErrorMsg() {
      return this.anyFallbackArgument.getMissingArgumentErrorMsg();
   }

   public Text getInvalidArgumentErrorMsg(String argumentInput) {
      return this.anyFallbackArgument.getInvalidArgumentErrorMsg(argumentInput);
   }

   public T parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.anyFallbackArgument.parse(input, context, argsReader);
   }

   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.anyFallbackArgument.parseValue(input, context, argsReader);
   }

   public T parseFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) throws ArgumentParseException {
      return this.anyFallbackArgument.parseFallback(input, context, argsReader, fallbackException, parsingFailed);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.anyFallbackArgument.complete(input, context, argsReader);
   }
}
