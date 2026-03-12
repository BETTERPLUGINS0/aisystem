package ac.grim.grimac.shaded.kyori.adventure.platform.facet;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointer;

@ApiStatus.Internal
public final class FacetPointers {
   private static final String NAMESPACE = "adventure_platform";
   public static final Pointer<String> SERVER = Pointer.pointer(String.class, Key.key("adventure_platform", "server"));
   public static final Pointer<Key> WORLD = Pointer.pointer(Key.class, Key.key("adventure_platform", "world"));
   public static final Pointer<FacetPointers.Type> TYPE = Pointer.pointer(FacetPointers.Type.class, Key.key("adventure_platform", "type"));

   private FacetPointers() {
   }

   public static enum Type {
      PLAYER,
      CONSOLE,
      OTHER;
   }
}
