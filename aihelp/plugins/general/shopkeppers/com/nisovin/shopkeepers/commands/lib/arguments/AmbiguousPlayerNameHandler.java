package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.UUID;
import org.bukkit.OfflinePlayer;

public class AmbiguousPlayerNameHandler<P extends OfflinePlayer> extends AbstractAmbiguousPlayerNameHandler<P> {
   public AmbiguousPlayerNameHandler(String input, Iterable<? extends P> matches) {
      this(input, matches, 5);
   }

   public AmbiguousPlayerNameHandler(String input, Iterable<? extends P> matches, int maxEntries) {
      super(input, matches, maxEntries);
   }

   protected String getName(P match) {
      assert match != null;

      return TextUtils.getPlayerNameOrUnknown(match.getName());
   }

   protected UUID getUniqueId(P match) {
      assert match != null;

      return match.getUniqueId();
   }
}
