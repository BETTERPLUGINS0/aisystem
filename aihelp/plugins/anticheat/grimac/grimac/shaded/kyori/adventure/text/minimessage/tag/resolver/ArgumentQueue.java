package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface ArgumentQueue {
   @NotNull
   Tag.Argument pop();

   @NotNull
   Tag.Argument popOr(@NotNull final String errorMessage);

   @NotNull
   Tag.Argument popOr(@NotNull final Supplier<String> errorMessage);

   @Nullable
   Tag.Argument peek();

   boolean hasNext();

   void reset();
}
