package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.commands.lib.arguments.AbstractAmbiguousPlayerNameHandler;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.UUID;

public class AmbiguousUserNameHandler extends AbstractAmbiguousPlayerNameHandler<User> {
   public AmbiguousUserNameHandler(String input, Iterable<? extends User> matches) {
      this(input, matches, 5);
   }

   public AmbiguousUserNameHandler(String input, Iterable<? extends User> matches, int maxEntries) {
      super(input, matches, maxEntries);
   }

   protected String getName(User match) {
      assert match != null;

      return TextUtils.getPlayerNameOrUnknown(match.getName());
   }

   protected UUID getUniqueId(User match) {
      assert match != null;

      return match.getUniqueId();
   }
}
