package com.nisovin.shopkeepers.commands.arguments;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.commands.lib.argument.ambiguity.AmbiguousInputHandler;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AmbiguousShopkeeperNameHandler extends AmbiguousInputHandler<Shopkeeper> {
   public AmbiguousShopkeeperNameHandler(String input, Iterable<? extends Shopkeeper> matches) {
      this(input, matches, 5);
   }

   public AmbiguousShopkeeperNameHandler(String input, Iterable<? extends Shopkeeper> matches, int maxEntries) {
      super(input, matches, maxEntries);
   }

   @Nullable
   protected Text getHeaderText() {
      Text header = Messages.ambiguousShopkeeperName;
      header.setPlaceholderArguments("name", this.input);
      return header;
   }

   protected Text getEntryText(@Nullable Shopkeeper match, int index) {
      assert match != null;

      String id = String.valueOf(match.getId());
      String name = match.getName();
      String uniqueId = match.getUniqueId().toString();
      Text entry = Messages.ambiguousShopkeeperNameEntry;
      entry.setPlaceholderArguments("index", index, "id", Text.insertion(id).childText(id).buildRoot(), "name", Text.insertion(name).childText(name).buildRoot(), "uuid", Text.insertion(uniqueId).childText(uniqueId).buildRoot());
      return entry.copy();
   }

   @Nullable
   protected Text getMoreText() {
      return Messages.ambiguousShopkeeperNameMore;
   }
}
