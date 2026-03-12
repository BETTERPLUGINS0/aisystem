package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Pair;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FirstOfArgument extends FallbackArgument<Pair<? extends CommandArgument<?>, ?>> {
   public static final String FORMAT_DELIMITER = "|";
   private final List<CommandArgument<?>> arguments;
   @Nullable
   private final String reducedFormat;

   public FirstOfArgument(String name, List<? extends CommandArgument<?>> arguments) {
      this(name, arguments, true, false);
   }

   public FirstOfArgument(String name, List<? extends CommandArgument<?>> arguments, boolean joinFormats) {
      this(name, arguments, joinFormats, false);
   }

   public FirstOfArgument(String name, List<? extends CommandArgument<?>> arguments, boolean joinFormats, boolean reverseFormat) {
      super(name);
      Validate.notNull(arguments, (String)"arguments is null");
      Validate.isTrue(!arguments.isEmpty(), "arguments is empty");
      List<CommandArgument<?>> argumentsList = new ArrayList(arguments.size());
      this.arguments = Collections.unmodifiableList(argumentsList);
      Iterator var6 = arguments.iterator();

      while(var6.hasNext()) {
         CommandArgument<?> argument = (CommandArgument)var6.next();
         Validate.notNull(argument, (String)"arguments contains null");
         argument.setParent(this);
         argumentsList.add(argument);
      }

      assert !this.arguments.isEmpty();

      if (joinFormats) {
         String delimiter = "|";
         StringBuilder format = new StringBuilder();
         ListIterator iterator = this.arguments.listIterator(reverseFormat ? this.arguments.size() : 0);

         while(true) {
            if (reverseFormat) {
               if (!iterator.hasPrevious()) {
                  break;
               }
            } else if (!iterator.hasNext()) {
               break;
            }

            CommandArgument<?> argument = reverseFormat ? (CommandArgument)iterator.previous() : (CommandArgument)iterator.next();

            assert argument != null;

            String argumentFormat = argument.getReducedFormat();
            if (!argumentFormat.isEmpty()) {
               format.append(argumentFormat).append(delimiter);
            }
         }

         if (format.length() == 0) {
            this.reducedFormat = "";
         } else {
            this.reducedFormat = format.substring(0, format.length() - delimiter.length());
         }
      } else {
         this.reducedFormat = null;
      }

   }

   public boolean isOptional() {
      Iterator var1 = this.arguments.iterator();

      CommandArgument argument;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         argument = (CommandArgument)var1.next();
      } while(!argument.isOptional());

      return true;
   }

   public String getReducedFormat() {
      return this.reducedFormat != null ? this.reducedFormat : super.getReducedFormat();
   }

   private <I> Pair<? extends CommandArgument<?>, ?> parseFirstOf(Iterable<? extends I> inputs, FirstOfArgument.Parser<I> parser, ArgumentsReader argsReader, boolean parsingFallbacks) throws ArgumentParseException {
      ArgumentsReader argsReaderState = argsReader.createSnapshot();
      Object fallbacks;
      if (!parsingFallbacks) {
         fallbacks = new ArrayList();
      } else {
         fallbacks = Collections.emptyList();
      }

      boolean nullParsed = false;
      ArgumentParseException firstParseException = null;

      for(Iterator var9 = inputs.iterator(); var9.hasNext(); argsReader.setState(argsReaderState)) {
         Object input = var9.next();

         try {
            Pair<? extends CommandArgument<?>, ?> result = parser.parse(input);
            if (result != null) {
               return result;
            }

            nullParsed = true;
         } catch (FallbackArgumentException var12) {
            if (parsingFallbacks) {
               String var10000 = var12.getArgument().getName();
               throw Validate.State.error("Argument '" + var10000 + "' threw another FallbackArgumentException while parsing fallback: " + String.valueOf(var12));
            }

            ((List)fallbacks).add(var12);
         } catch (ArgumentRejectedException var13) {
            throw var13;
         } catch (ArgumentParseException var14) {
            if (firstParseException == null) {
               firstParseException = var14;
            }
         }
      }

      if (!((List)fallbacks).isEmpty()) {
         throw new FirstOfArgument.FirstOfFallbackException(this, (List)fallbacks);
      } else if (nullParsed) {
         return null;
      } else {
         assert firstParseException != null;

         throw firstParseException;
      }
   }

   private Pair<? extends CommandArgument<?>, ?> toPair(CommandArgument<?> argument, Object value) {
      return value == null ? null : Pair.of(argument, value);
   }

   public Pair<? extends CommandArgument<?>, ?> parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      Pair<? extends CommandArgument<?>, ?> result = this.parseFirstOf(this.arguments, (argument) -> {
         return this.toPair(argument, argument.parse(input, context, argsReader));
      }, argsReader, false);
      if (result != null) {
         context.put(this.getName(), result);
      }

      return result;
   }

   @Nullable
   public Pair<? extends CommandArgument<?>, ?> parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.parseFirstOf(this.arguments, (argument) -> {
         return this.toPair(argument, argument.parseValue(input, context, argsReader));
      }, argsReader, false);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      List<String> suggestions = new ArrayList();
      ArgumentsReader argsReaderState = argsReader.createSnapshot();
      Iterator var6 = this.arguments.iterator();

      while(var6.hasNext()) {
         CommandArgument<?> argument = (CommandArgument)var6.next();
         int limit = 20 - suggestions.size();
         if (limit <= 0) {
            break;
         }

         argsReader.setState(argsReaderState);
         List<? extends String> argumentSuggestions = argument.complete(input, context, argsReader);
         if (argumentSuggestions.size() >= limit) {
            suggestions.addAll(argumentSuggestions.subList(0, limit));
            break;
         }

         suggestions.addAll(argumentSuggestions);
      }

      return Collections.unmodifiableList(suggestions);
   }

   public Pair<? extends CommandArgument<?>, ?> parseFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) throws ArgumentParseException {
      FirstOfArgument.FirstOfFallbackException firstOfFallback = (FirstOfArgument.FirstOfFallbackException)fallbackException;
      List<? extends FallbackArgumentException> originalFallbacks = firstOfFallback.getOriginalFallbacks();
      Pair<? extends CommandArgument<?>, ?> result = this.parseFirstOf(originalFallbacks, (fallback) -> {
         FallbackArgument<?> argument = fallback.getArgument();
         Object value = argument.parseFallback(input, context, argsReader, fallback, parsingFailed);
         return this.toPair(argument, value);
      }, argsReader, true);
      if (result != null) {
         context.put(this.getName(), result);
      }

      return result;
   }

   @FunctionalInterface
   private interface Parser<I> {
      @Nullable
      Pair<? extends CommandArgument<?>, ?> parse(@NonNull I var1) throws ArgumentParseException;
   }

   private static class FirstOfFallbackException extends FallbackArgumentException {
      private static final long serialVersionUID = -1177782345537954263L;
      private final List<? extends FallbackArgumentException> originalFallbacks;

      public FirstOfFallbackException(FirstOfArgument firstOfArgument, List<? extends FallbackArgumentException> originalFallbacks) {
         super(firstOfArgument, (ArgumentParseException)originalFallbacks.get(0));

         assert originalFallbacks != null && !originalFallbacks.isEmpty();

         this.originalFallbacks = originalFallbacks;
      }

      public List<? extends FallbackArgumentException> getOriginalFallbacks() {
         return this.originalFallbacks;
      }
   }
}
