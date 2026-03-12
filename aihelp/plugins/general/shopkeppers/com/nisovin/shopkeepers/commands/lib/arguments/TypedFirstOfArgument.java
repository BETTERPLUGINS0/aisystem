package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgument;
import com.nisovin.shopkeepers.commands.lib.argument.fallback.FallbackArgumentException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContext;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Pair;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TypedFirstOfArgument<T> extends FallbackArgument<T> {
   private final FirstOfArgument firstOfArgument;

   public TypedFirstOfArgument(String name, List<? extends CommandArgument<T>> arguments) {
      this(name, arguments, true, false);
   }

   public TypedFirstOfArgument(String name, List<? extends CommandArgument<T>> arguments, boolean joinFormats) {
      this(name, arguments, joinFormats, false);
   }

   public TypedFirstOfArgument(String name, List<? extends CommandArgument<T>> arguments, boolean joinFormats, boolean reverseFormat) {
      super(name);
      this.firstOfArgument = new FirstOfArgument(name + ":firstOf", arguments, joinFormats, reverseFormat);
      this.firstOfArgument.setParent(this);
   }

   public boolean isOptional() {
      return this.firstOfArgument.isOptional();
   }

   public String getReducedFormat() {
      return this.firstOfArgument.getReducedFormat();
   }

   private T getValue(@Nullable Pair<? extends CommandArgument<?>, ?> result) {
      return result != null ? result.getSecond() : Unsafe.uncheckedNull();
   }

   public T parse(CommandInput input, CommandContext context, ArgumentsReader argsReader) throws ArgumentParseException {
      T value = this.getValue(this.firstOfArgument.parse(input, context, argsReader));
      if (value != null) {
         context.put(this.getName(), value);
      }

      return value;
   }

   public T parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return this.getValue(this.firstOfArgument.parseValue(input, context, argsReader));
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.firstOfArgument.complete(input, context, argsReader);
   }

   public T parseFallback(CommandInput input, CommandContext context, ArgumentsReader argsReader, FallbackArgumentException fallbackException, boolean parsingFailed) throws ArgumentParseException {
      return this.getValue(this.firstOfArgument.parseFallback(input, context, argsReader, fallbackException, parsingFailed));
   }
}
