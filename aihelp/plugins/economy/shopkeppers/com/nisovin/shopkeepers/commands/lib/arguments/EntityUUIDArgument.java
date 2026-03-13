package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityUUIDArgument extends ObjectUUIDArgument {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 3;

   public EntityUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public EntityUUIDArgument(String name, ArgumentFilter<? super UUID> filter) {
      this(name, filter, 3);
   }

   public EntityUUIDArgument(String name, ArgumentFilter<? super UUID> filter, int minimumCompletionInput) {
      super(name, filter, minimumCompletionInput);
   }

   public static Iterable<? extends UUID> getDefaultCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String uuidPrefix, ArgumentFilter<? super Entity> filter) {
      CommandSender sender = input.getSender();
      if (sender instanceof Player) {
         Player player = (Player)sender;
         Entity targetEntity = EntityUtils.getTargetedEntity(player, (entity) -> {
            return filter.test(input, context, entity);
         });
         if (targetEntity != null) {
            String normalizedUUIDPrefix = uuidPrefix.toLowerCase(Locale.ROOT);
            if (targetEntity.getUniqueId().toString().startsWith(normalizedUUIDPrefix)) {
               return Collections.singleton(targetEntity.getUniqueId());
            }
         }
      }

      return Collections.emptyList();
   }

   protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
      return getDefaultCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix, ArgumentFilter.acceptAny());
   }
}
