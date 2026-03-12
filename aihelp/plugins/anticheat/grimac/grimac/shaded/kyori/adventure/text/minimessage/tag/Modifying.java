package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree.Node;

@ApiStatus.OverrideOnly
public interface Modifying extends Tag {
   default void visit(@NotNull final Node current, final int depth) {
   }

   default void postVisit() {
   }

   Component apply(@NotNull final Component current, final int depth);
}
