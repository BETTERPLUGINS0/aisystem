package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.MissingArgumentException;
import com.nisovin.shopkeepers.commands.lib.argument.RequiresPlayerArgumentException;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AnyFallbackArgument extends FallbackArgument<Object> {
   protected final CommandArgument<?> argument;
   protected final CommandArgument<?> fallbackArgument;

   public AnyFallbackArgument(CommandArgument<?> argument, CommandArgument<?> fallbackArgument) {
      super(((CommandArgument)Validate.notNull(argument, (String)"argument is null")).getName());
      Validate.notNull(fallbackArgument, (String)"fallbackArgument is null");
      Validate.isTrue(!(fallbackArgument instanceof FallbackArgument), "fallbackArgument cannot be a FallbackArgument itself");
      this.argument = argument;
      this.fallbackArgument = fallbackArgument;
      argument.setParent(this);
      fallbackArgument.setParent(this);
   }

   public CommandArgument<?> getOriginalArgument() {
      return this.argument;
   }

   public CommandArgument<?> getFallbackArgument() {
      return this.fallbackArgument;
   }

   public String getReducedFormat() {
      return this.argument.getReducedFormat();
   }

   public boolean isOptional() {
      return this.argument.isOptional() || this.fallbackArgument.isOptional();
   }

   public Text getMissingArgumentErrorMsg() {
      return this.argument.getMissingArgumentErrorMsg();
   }

   public Text getInvalidArgumentErrorMsg(String argumentInput) {
      return this.argument.getInvalidArgumentErrorMsg(argumentInput);
   }

   @Nullable
   public Object parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      try {
         return this.argument.parse(input, context, argsReader);
      } catch (ArgumentParseException var5) {
         throw new FallbackArgumentException(this, var5);
      }
   }

   @Nullable
   public Object parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      try {
         return this.argument.parseValue(input, context, argsReader);
      } catch (ArgumentParseException var5) {
         throw new FallbackArgumentException(this, var5);
      }
   }

   public Object parseFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) throws ArgumentParseException {
      Optional<Object> originalFallbackValue = (Optional)Unsafe.cast(this.parseOriginalFallback(input, context, argsReader, fallbackException, parsingFailed));
      if (originalFallbackValue != null) {
         return originalFallbackValue.orElse((Object)null);
      } else {
         try {
            return this.fallbackArgument.parse(input, context, argsReader);
         } catch (RequiresPlayerArgumentException | MissingArgumentException var10) {
            if (parsingFailed) {
               throw fallbackException.getRootException();
            } else {
               try {
                  return this.argument.parse(input, context, argsReader);
               } catch (FallbackArgumentException var9) {
                  throw var9.getRootException();
               }
            }
         }
      }
   }

   @Nullable
   protected final Optional<?> parseOriginalFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) {
      ArgumentParseException originalException = fallbackException.getOriginalException();
      if (originalException instanceof FallbackArgumentException) {
         FallbackArgumentException originalFallback = (FallbackArgumentException)originalException;
         FallbackArgument originalFallbackArgument = originalFallback.getArgument();

         try {
            return Optional.ofNullable(originalFallbackArgument.parseFallback(input, context, argsReader, originalFallback, parsingFailed));
         } catch (FallbackArgumentException var10) {
            String var10000 = originalFallbackArgument.getName();
            throw Validate.State.error("Original fallback argument '" + var10000 + "' threw another FallbackArgumentException while parsing fallback: " + String.valueOf(var10));
         } catch (ArgumentParseException var11) {
         }
      }

      return null;
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      ArgumentsReader argsReaderState = argsReader.createSnapshot();
      List<? extends String> argumentSuggestions = this.argument.complete(input, context, argsReader);
      if (argumentSuggestions.size() >= 20) {
         return argumentSuggestions;
      } else {
         List<String> suggestions = new ArrayList(argumentSuggestions);
         int limit = 20 - suggestions.size();

         assert limit > 0;

         argsReader.setState(argsReaderState);
         List<? extends String> fallbackSuggestions = this.fallbackArgument.complete(input, context, argsReader);
         if (fallbackSuggestions.size() <= limit) {
            suggestions.addAll(fallbackSuggestions);
         } else {
            suggestions.addAll(fallbackSuggestions.subList(0, limit));
         }

         return Collections.unmodifiableList(suggestions);
      }
   }
}
