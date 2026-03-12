package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.UUID;
import java.util.Map.Entry;

public class AmbiguousPlayerEntryNameHandler extends AbstractAmbiguousPlayerNameHandler<Entry<? extends UUID, ? extends String>> {
   public AmbiguousPlayerEntryNameHandler(String input, Iterable<? extends Entry<? extends UUID, ? extends String>> matches) {
      this(input, matches, 5);
   }

   public AmbiguousPlayerEntryNameHandler(String input, Iterable<? extends Entry<? extends UUID, ? extends String>> matches, int maxEntries) {
      super(input, matches, maxEntries);
   }

   protected String getName(Entry<? extends UUID, ? extends String> match) {
      assert match != null;

      return TextUtils.getPlayerNameOrUnknown((String)match.getValue());
   }

   protected UUID getUniqueId(Entry<? extends UUID, ? extends String> match) {
      assert match != null;

      return (UUID)match.getKey();
   }
}
