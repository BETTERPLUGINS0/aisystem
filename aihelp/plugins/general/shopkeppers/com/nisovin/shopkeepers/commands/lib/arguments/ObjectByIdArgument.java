package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ObjectByIdArgument<I, O> extends CommandArgument<O> {
   protected final ArgumentFilter<? super O> filter;
   protected final ObjectIdArgument<I> idArgument;

   public ObjectByIdArgument(String name, ArgumentFilter<? super O> filter, ObjectByIdArgument.IdArgumentArgs idArgumentArgs) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
      this.idArgument = this.createIdArgument(name + ":id", idArgumentArgs);
      this.idArgument.setParent(this);
   }

   protected abstract ObjectIdArgument<I> createIdArgument(String var1, ObjectByIdArgument.IdArgumentArgs var2);

   public Text getMissingArgumentErrorMsg() {
      return this.idArgument.getMissingArgumentErrorMsg();
   }

   @Nullable
   protected abstract O getObject(CommandInput var1, CommandContextView var2, @NonNull I var3) throws ArgumentParseException;

   public O parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      I id = this.idArgument.parseValue(input, context, argsReader);
      O object = this.getObject(input, context, id);
      if (object == null) {
         throw this.invalidArgumentError(this.idArgument.toString(id));
      } else if (!this.filter.test(input, context, object)) {
         throw this.filter.rejectedArgumentException(this, this.idArgument.toString(id), object);
      } else {
         return object;
      }
   }

   protected abstract Iterable<? extends I> getCompletionSuggestions(CommandInput var1, CommandContextView var2, int var3, String var4);

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.idArgument.complete(input, context, argsReader);
   }

   protected static class IdArgumentArgs {
      public final int minimumCompletionInput;
      public final boolean joinRemainingArgs;

      public IdArgumentArgs(int minimumCompletionInput) {
         this(minimumCompletionInput, false);
      }

      public IdArgumentArgs(int minimumCompletionInput, boolean joinRemainingArgs) {
         this.minimumCompletionInput = minimumCompletionInput;
         this.joinRemainingArgs = joinRemainingArgs;
      }
   }
}
