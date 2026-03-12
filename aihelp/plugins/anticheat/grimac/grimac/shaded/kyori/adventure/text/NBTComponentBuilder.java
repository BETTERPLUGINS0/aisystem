package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface NBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends ComponentBuilder<C, B> {
   @Contract("_ -> this")
   @NotNull
   B nbtPath(@NotNull final String nbtPath);

   @Contract("_ -> this")
   @NotNull
   B interpret(final boolean interpret);

   @Contract("_ -> this")
   @NotNull
   B separator(@Nullable final ComponentLike separator);
}
