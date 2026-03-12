package com.nisovin.shopkeepers.commands.lib.arguments;

import com.nisovin.shopkeepers.commands.lib.argument.ambiguity.AmbiguousInputHandler;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractAmbiguousPlayerNameHandler<P> extends AmbiguousInputHandler<P> {
   public AbstractAmbiguousPlayerNameHandler(String input, Iterable<? extends P> matches) {
      this(input, matches, 5);
   }

   public AbstractAmbiguousPlayerNameHandler(String input, Iterable<? extends P> matches, int maxEntries) {
      super(input, matches, maxEntries);
   }

   @Nullable
   protected Text getHeaderText() {
      Text header = Messages.ambiguousPlayerName;
      header.setPlaceholderArguments("name", this.input);
      return header;
   }

   protected abstract String getName(P var1);

   protected abstract UUID getUniqueId(P var1);

   protected Text getEntryText(P match, int index) {
      assert match != null;

      String matchName = this.getName(match);
      UUID matchUUID = this.getUniqueId(match);
      String matchUUIDString = matchUUID.toString();
      Text entry = Messages.ambiguousPlayerNameEntry;
      entry.setPlaceholderArguments("index", index, "name", Text.insertion(matchName).childText(matchName).buildRoot(), "uuid", Text.insertion(matchUUIDString).childText(matchUUIDString).buildRoot());
      return entry.copy();
   }

   @Nullable
   protected Text getMoreText() {
      return Messages.ambiguousPlayerNameMore;
   }
}
