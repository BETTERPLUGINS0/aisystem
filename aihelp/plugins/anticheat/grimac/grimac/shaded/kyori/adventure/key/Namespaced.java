package ac.grim.grimac.shaded.kyori.adventure.key;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface Namespaced {
   @KeyPattern.Namespace
   @NotNull
   String namespace();
}
