package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentParseException;
import com.nisovin.shopkeepers.commands.lib.argument.ArgumentsReader;
import com.nisovin.shopkeepers.commands.lib.argument.CommandArgument;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentRejectedException;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerArgument extends CommandArgument<Player> {
   protected final ArgumentFilter<? super Player> filter;
   private final PlayerByUUIDArgument playerUUIDArgument;
   private final PlayerByNameArgument playerNameArgument;
   private final TypedFirstOfArgument<Player> firstOfArgument;

   public PlayerArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public PlayerArgument(String name, ArgumentFilter<? super Player> filter) {
      this(name, filter, 0, 3);
   }

   public PlayerArgument(String name, ArgumentFilter<? super Player> filter, int minimalNameCompletionInput, int minimumUUIDCompletionInput) {
      super(name);
      Validate.notNull(filter, (String)"filter is null");
      this.filter = filter;
      this.playerUUIDArgument = new PlayerByUUIDArgument(name + ":uuid", filter, minimumUUIDCompletionInput) {
         protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
            return ((PlayerArgument)Unsafe.initialized(PlayerArgument.this)).getUUIDCompletionSuggestions(input, context, minimumCompletionInput, idPrefix);
         }
      };
      this.playerNameArgument = new PlayerByNameArgument(name + ":name", filter, minimalNameCompletionInput) {
         @Nullable
         public Player getObject(CommandInput input, CommandContextView context, String nameInput) throws ArgumentParseException {
            return ((PlayerArgument)Unsafe.initialized(PlayerArgument.this)).getPlayerByName(nameInput);
         }

         protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
            return ((PlayerArgument)Unsafe.initialized(PlayerArgument.this)).getNameCompletionSuggestions(input, context, minimumCompletionInput, idPrefix);
         }
      };
      this.firstOfArgument = new TypedFirstOfArgument(name + ":firstOf", Arrays.asList(this.playerUUIDArgument, this.playerNameArgument), false, false);
      this.firstOfArgument.setParent(this);
   }

   public Player parseValue(CommandInput input, CommandContextView context, ArgumentsReader argsReader) throws ArgumentParseException {
      return (Player)this.firstOfArgument.parseValue(input, context, argsReader);
   }

   public List<? extends String> complete(CommandInput input, CommandContextView context, ArgumentsReader argsReader) {
      return this.firstOfArgument.complete(input, context, argsReader);
   }

   @Nullable
   public Player getPlayerByName(String nameInput) throws ArgumentRejectedException {
      return this.playerNameArgument.getDefaultPlayerByName(nameInput);
   }

   protected Iterable<? extends String> getNameCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return PlayerNameArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter, true);
   }

   protected Iterable<? extends UUID> getUUIDCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String idPrefix) {
      return PlayerUUIDArgument.getDefaultCompletionSuggestions(input, context, minimumCompletionInput, idPrefix, this.filter);
   }
}
