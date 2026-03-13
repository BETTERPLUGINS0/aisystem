package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.commands.lib.context.SimpleCommandContext;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class CompoundArgument<T> extends CommandArgument<T> {
   public static final String FORMAT_DELIMITER = " ";
   private final List<? extends CommandArgument<?>> arguments;
   private final boolean useReducedFormat;
   @Nullable
   private final String reducedFormat;

   public CompoundArgument(String name, List<? extends CommandArgument<?>> arguments) {
      this(name, arguments, true);
   }

   public CompoundArgument(String name, List<? extends CommandArgument<?>> arguments, boolean joinFormats) {
      this(name, arguments, joinFormats, true);
   }

   public CompoundArgument(String name, List<? extends CommandArgument<?>> arguments, boolean joinFormats, boolean useReducedFormat) {
      super(name);
      Validate.notNull(arguments, (String)"arguments is null");
      Validate.isTrue(!arguments.isEmpty(), "arguments is empty");
      List<CommandArgument<?>> argumentsList = new ArrayList(arguments.size());
      this.arguments = Collections.unmodifiableList(argumentsList);
      Iterator var6 = arguments.iterator();

      while(var6.hasNext()) {
         CommandArgument<?> argument = (CommandArgument)var6.next();
         Validate.notNull(argument, (String)"arguments contains null");
         Validate.isTrue(!(argument instanceof FallbackArgument), "arguments contains a FallbackArgument");
         argument.setParent(this);
         argumentsList.add(argument);
      }

      assert !this.arguments.isEmpty();

      this.useReducedFormat = joinFormats ? useReducedFormat : false;
      if (joinFormats) {
         String delimiter = " ";
         StringBuilder format = new StringBuilder();
         Iterator var8 = this.arguments.iterator();

         while(var8.hasNext()) {
            CommandArgument<?> argument = (CommandArgument)var8.next();
            String argumentFormat = argument.getFormat();
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

   public List<? extends CommandArgument<?>> getArguments() {
      return this.arguments;
   }

   public boolean isOptional() {
      Iterator var1 = this.arguments.iterator();

      CommandArgument argument;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         argument = (CommandArgument)var1.next();
      } while(argument.isOptional());

      return false;
   }

   public String getFormat() {
      return this.useReducedFormat ? this.getReducedFormat() : super.getFormat();
   }

   public String getReducedFormat() {
      return this.reducedFormat != null ? this.reducedFormat : super.getReducedFormat();
   }

   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      CommandContext localContext = context.copy();
      Iterator var5 = this.arguments.iterator();

      while(var5.hasNext()) {
         CommandArgument<?> argument = (CommandArgument)var5.next();
         argument.parse(input, localContext, argsReader);
      }

      return this.parseCompoundValue(input, localContext.getView(), argsReader);
   }

   protected abstract T parseCompoundValue(CommandInput var1, CommandContextView var2, ArgumentsReader var3) throws ArgumentParseException;

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      List<String> suggestions = new ArrayList();
      CommandContext localContext = new SimpleCommandContext();
      CommandContextView localContextView = localContext.getView();
      Iterator var7 = this.arguments.iterator();

      while(var7.hasNext()) {
         CommandArgument<?> argument = (CommandArgument)var7.next();
         int remainingArgs = argsReader.getRemainingSize();
         if (remainingArgs == 0) {
            break;
         }

         ArgumentsReader argsReaderState = argsReader.createSnapshot();

         try {
            argument.parse(input, localContext, argsReader);
            if (!argsReader.hasNext()) {
               argsReader.setState(argsReaderState);
               suggestions.addAll(argument.complete(input, localContextView, argsReader));
               break;
            }

            if (argsReader.getRemainingSize() == remainingArgs) {
               suggestions.addAll(argument.complete(input, localContextView, argsReader));
               argsReader.setState(argsReaderState);
            }
         } catch (ArgumentParseException var12) {
            if (argsReader.getRemainingSize() != remainingArgs) {
               argsReader.setState(argsReaderState);
               suggestions.addAll(argument.complete(input, localContextView, argsReader));
               break;
            }

            argsReader.setState(argsReaderState);
         }
      }

      return Collections.unmodifiableList(suggestions);
   }
}
