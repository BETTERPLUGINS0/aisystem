package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface Inserting extends Tag {
   @NotNull
   Component value();

   default boolean allowsChildren() {
      return true;
   }
}
