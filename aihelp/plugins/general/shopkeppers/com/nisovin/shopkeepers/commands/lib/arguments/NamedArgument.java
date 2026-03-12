package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NamedArgument<T> extends CommandArgument<T> {
   private static final String NAME_DELIMITER = "=";
   private final CommandArgument<T> argument;

   public NamedArgument(CommandArgument<T> argument) {
      super(((CommandArgument)Validate.notNull(argument, (String)"argument is null")).getName());
      this.argument = argument;
      argument.setParent(this);
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

   private String getArgumentPrefix() {
      return this.argument.getDisplayName() + "=";
   }

   public String getReducedFormat() {
      return this.getArgumentPrefix() + "...";
   }

   private <R> R parseNamedArgument(CommandInput input, ArgumentsReader argsReader, NamedArgument.Parser<R> parser) throws ArgumentParseException {
      return this.parseNamedArgument(input, argsReader, this.getArgumentPrefix(), parser);
   }

   private <R> R parseNamedArgument(CommandInput input, ArgumentsReader argsReader, String argPrefix, NamedArgument.Parser<R> parser) throws ArgumentParseException {
      String arg = argsReader.peekIfPresent();
      if (arg == null) {
         throw this.missingArgumentError();
      } else if (!arg.startsWith(argPrefix)) {
         throw this.invalidArgumentError(arg);
      } else {
         String namelessArg = arg.substring(argPrefix.length());
         List<String> adjustedArgs = new ArrayList(input.getArguments());

         for(int i = 0; i < adjustedArgs.size(); ++i) {
            if (((String)adjustedArgs.get(i)).equals(arg)) {
               adjustedArgs.set(i, namelessArg);
               break;
            }
         }

         CommandInput adjustedInput = new CommandInput(input.getSender(), input.getCommand(), input.getCommandAlias(), adjustedArgs);
         ArgumentsReader adjustedArgsReader = new ArgumentsReader(adjustedInput);
         adjustedArgsReader.setCursor(argsReader.getCursor());

         try {
            R value = parser.parse(adjustedInput, adjustedArgsReader);
            argsReader.setCursor(adjustedArgsReader.getCursor());
            return value;
         } catch (FallbackArgumentException var11) {
            String var10000 = this.getName();
            throw Validate.State.error("NamedArgument '" + var10000 + "' does not support fallbacks! Observed: " + String.valueOf(var11));
         } catch (ArgumentParseException var12) {
            throw new ArgumentRejectedException(this, var12.getMessageText(), var12);
         }
      }
   }

   public T parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.parseNamedArgument(input, argsReader, (adjustedInput, adjustedArgsReader) -> {
         return this.argument.parse(adjustedInput, context, adjustedArgsReader);
      });
   }

   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.parseNamedArgument(input, argsReader, (adjustedInput, adjustedArgsReader) -> {
         return this.argument.parseValue(adjustedInput, context, adjustedArgsReader);
      });
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      String arg = argsReader.peekIfPresent();
      if (arg == null) {
         return Collections.emptyList();
      } else {
         String argPrefix = this.getArgumentPrefix();
         String prefix = argPrefix.startsWith(arg) ? arg : argPrefix;

         try {
            List<? extends String> suggestions = (List)this.parseNamedArgument(input, argsReader, prefix, (adjustedInput, adjustedArgsReader) -> {
               return this.argument.complete(adjustedInput, context, adjustedArgsReader);
            });
            return suggestions.stream().map((suggestion) -> {
               return argPrefix + suggestion;
            }).toList();
         } catch (ArgumentParseException var8) {
            return Collections.emptyList();
         }
      }
   }

   @FunctionalInterface
   private interface Parser<R> {
      R parse(CommandInput var1, ArgumentsReader var2) throws ArgumentParseException;
   }
}
