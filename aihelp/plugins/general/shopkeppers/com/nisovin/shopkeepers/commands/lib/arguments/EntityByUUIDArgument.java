package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EntityByUUIDArgument extends ObjectByIdArgument<UUID, Entity> {
   public EntityByUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public EntityByUUIDArgument(String name, ArgumentFilter<? super Entity> filter) {
      this(name, filter, 3);
   }

   public EntityByUUIDArgument(String name, ArgumentFilter<? super Entity> filter, int minimumCompletionInput) {
      super(name, filter, new ObjectByIdArgument.IdArgumentArgs(minimumCompletionInput));
   }

   protected ObjectIdArgument<UUID> createIdArgument(String name, ObjectByIdArgument.IdArgumentArgs args) {
      return new EntityUUIDArgument(name, ArgumentFilter.acceptAny(), args.minimumCompletionInput) {
         protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
            return EntityByUUIDArgument.this.getCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix);
         }
      };
   }

   @Nullable
   protected Entity getObject(CommandInput input, CommandContextView context, UUID uuid) throws ArgumentParseException {
      return Bukkit.getEntity(uuid);
   }

   protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return EntityUUIDArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
