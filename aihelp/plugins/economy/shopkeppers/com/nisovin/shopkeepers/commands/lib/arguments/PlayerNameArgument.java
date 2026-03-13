package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.commands.lib.CommandInput;
import com.nisovin.shopkeepers.commands.lib.argument.filter.ArgumentFilter;
import com.nisovin.shopkeepers.commands.lib.context.CommandContextView;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.entity.Player;

public class PlayerNameArgument extends ObjectNameArgument {
   public static final int DEFAULT_MINIMUM_COMPLETION_INPUT = 0;

   public PlayerNameArgument(String name) {
      this(name, ArgumentFilter.acceptAny());
   }

   public PlayerNameArgument(String name, ArgumentFilter<? super String> filter) {
      this(name, filter, 0);
   }

   public PlayerNameArgument(String name, ArgumentFilter<? super String> filter, int minimumCompletionInput) {
      super(name, false, filter, minimumCompletionInput);
   }

   public Text getMissingArgumentErrorMsg() {
      Text text = Messages.commandPlayerArgumentMissing;
      text.setPlaceholderArguments(this.getDefaultErrorMsgArgs());
      return text;
   }

   public static Iterable<? extends String> getDefaultCompletionSuggestions(CommandInput input, CommandContextView context, int minimumCompletionInput, String namePrefix, ArgumentFilter<? super Player> playerFilter, boolean includeDisplayNames) {
      if (namePrefix.length() < minimumCompletionInput) {
         return Collections.emptyList();
      } else {
         String normalizedNamePrefix = StringUtils.normalize(namePrefix);
         Stream var10000 = EntityUtils.getOnlinePlayersStream().filter((player) -> {
            return playerFilter.test(input, context, player);
         }).map((player) -> {
            String name = (String)Unsafe.assertNonNull(player.getName());
            if (StringUtils.normalize(name).startsWith(normalizedNamePrefix)) {
               return name;
            } else {
               if (includeDisplayNames) {
                  String displayName = TextUtils.stripColor(player.getDisplayName());
                  String normalizedWithCase = StringUtils.normalizeKeepCase(displayName);
                  String normalized = normalizedWithCase.toLowerCase(Locale.ROOT);
                  if (normalized.startsWith(normalizedNamePrefix)) {
                     return normalizedWithCase;
                  }
               }

               return null;
            }
         }).filter(Objects::nonNull).map(Unsafe::assertNonNull);
         Objects.requireNonNull(var10000);
         Iterable<String> suggestions = var10000::iterator;
         return suggestions;
      }
   }

   protected Iterable<? extends String> getCompletionSuggestions(CommandInput input, CommandContextView context, String idPrefix) {
      return getDefaultCompletionSuggestions(input, context, this.minimumCompletionInput, idPrefix, ArgumentFilter.acceptAny(), true);
   }
}
