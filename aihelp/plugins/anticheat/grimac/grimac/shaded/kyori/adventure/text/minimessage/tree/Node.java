package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;

@ApiStatus.NonExtendable
public interface Node {
   @NotNull
   String toString();

   @NotNull
   List<? extends Node> children();

   @Nullable
   Node parent();

   @ApiStatus.NonExtendable
   public interface Root extends Node {
      @NotNull
      String input();
   }
}
