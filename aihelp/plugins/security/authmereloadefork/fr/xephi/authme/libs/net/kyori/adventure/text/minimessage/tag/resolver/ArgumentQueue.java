package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
