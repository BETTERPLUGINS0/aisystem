package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Entity;

public class EntityArgument extends CommandArgument<Entity> {
   protected final ArgumentFilter<? super Entity> filter;
   private final EntityByUUIDArgument entityUUIDArgument;
   private final TypedFirstOfArgument<Entity> firstOfArgument;

   public EntityArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public EntityArgument(String name, ArgumentFilter<? super Entity> filter) {
      this(name, filter, 3);
   }

   public EntityArgument(String name, ArgumentFilter<? super Entity> filter, int minimumUUIDCompletionInput) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
      this.entityUUIDArgument = new EntityByUUIDArgument(name + ":uuid", filter, minimumUUIDCompletionInput) {
         protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
            return ((EntityArgument)Unsafe.initialized(EntityArgument.this)).getUUIDCompletionSuggestions(input, context, minimumCompletionInput, idPrefix);
         }
      };
      this.firstOfArgument = new TypedFirstOfArgument(name + ":firstOf", Arrays.asList(this.entityUUIDArgument), false, false);
      this.firstOfArgument.setParent(this);
   }

   public Entity parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return (Entity)this.firstOfArgument.parseValue(input, context, argsReader);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.firstOfArgument.complete(input, context, argsReader);
   }

   protected Iterable<? extends UUID> getUUIDCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return EntityUUIDArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
