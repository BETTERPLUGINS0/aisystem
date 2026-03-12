package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage;

import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointered;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface Context {
   @Nullable
   Pointered target();

   @NotNull
   Pointered targetOrThrow();

   @NotNull
   <T extends Pointered> T targetAsType(@NotNull final Class<T> targetClass);

   @NotNull
   Component deserialize(@NotNull final String message);

   @NotNull
   Component deserialize(@NotNull final String message, @NotNull final TagResolver resolver);

   @NotNull
   Component deserialize(@NotNull final String message, @NotNull final TagResolver... resolvers);

   @NotNull
   ParsingException newException(@NotNull final String message, @NotNull final ArgumentQueue tags);

   @NotNull
   ParsingException newException(@NotNull final String message);

   @NotNull
   ParsingException newException(@NotNull final String message, @Nullable final Throwable cause, @NotNull final ArgumentQueue args);
}
