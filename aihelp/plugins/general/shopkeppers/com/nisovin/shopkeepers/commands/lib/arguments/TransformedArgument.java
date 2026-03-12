package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;

public class TransformedArgument<T, R> extends FallbackArgument<R> {
   private final CommandArgument<T> fromArgument;
   private final TransformedArgument.ArgumentTransformer<T, R> transformer;

   public TransformedArgument(CommandArgument<T> fromArgument, TransformedArgument.ArgumentTransformer<T, R> transformer) {
      super(((CommandArgument)Validate.notNull(fromArgument, (String)"fromArgument is null")).getName());
      Validate.notNull(transformer, (String)"transformer is null");
      this.fromArgument = fromArgument;
      fromArgument.setParent(this);
      this.transformer = transformer;
   }

   public R parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      Object fromValue;
      try {
         fromValue = this.fromArgument.parseValue(input, context, argsReader);
      } catch (FallbackArgumentException var6) {
         throw new FallbackArgumentException(this, var6);
      }

      return this.transformer.apply(fromValue);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.fromArgument.complete(input, context, argsReader);
   }

   public R parseFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) throws ArgumentParseException {
      FallbackArgumentException originalFallback = (FallbackArgumentException)fallbackException.getOriginalException();
      T fromValue = ((FallbackArgument)this.fromArgument).parseFallback(input, context, argsReader, originalFallback, parsingFailed);
      return this.transformer.apply(fromValue);
   }

   @FunctionalInterface
   public interface ArgumentTransformer<T, R> {
      R apply(T var1) throws ArgumentParseException;
   }
}
