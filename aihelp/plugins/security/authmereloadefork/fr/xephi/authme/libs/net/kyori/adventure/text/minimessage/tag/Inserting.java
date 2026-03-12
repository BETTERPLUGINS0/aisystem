package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Inserting extends Tag {
   @NotNull
   Component value();

   default boolean allowsChildren() {
      return true;
   }
}
