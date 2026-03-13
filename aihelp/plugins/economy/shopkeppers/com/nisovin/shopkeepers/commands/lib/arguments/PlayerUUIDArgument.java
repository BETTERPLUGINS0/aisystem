package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUUIDArgument extends ObjectUUIDArgument {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 3;

   public PlayerUUIDArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public PlayerUUIDArgument(String name, ArgumentFilter<? super UUID> filter) {
      this(name, filter, 3);
   }

   public PlayerUUIDArgument(String name, ArgumentFilter<? super UUID> filter, int minimumCompletionInput) {
      super(name, filter, minimumCompletionInput);
   }

   public Text getMissingArgumentErrorMsg() {
      Text text = Messages.commandPlayerArgumentMissing;
      text.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
      return text;
   }

   public static Iterable<? extends UUID> getDefaultCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String uuidPrefix, ArgumentFilter<? super Player> playerFilter) {
      if (uuidPrefix.length() < minimumCompletionInput) {
         return Collections.emptyList();
      } else {
         String normalizedUUIDPrefix = uuidPrefix.toLowerCase(Locale.ROOT);
         Stream var10000 = EntityUtils.getOnlinePlayersStream().filter((player) -> {
            return playerFilter.test(input, context, player);
         }).map(OfflinePlayer::getUniqueId).filter((uuid) -> {
            return uuid.toString().startsWith(normalizedUUIDPrefix);
         });
         Objects.requireNonNull(var10000);
         return var10000::iterator;
      }
   }

   protected Iterable<? extends UUID> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
      return getDefaultCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix, ArgumentFilter.acceptAny());
   }
}
