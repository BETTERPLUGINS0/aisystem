package fr.xephi.authme.libs.net.kyori.adventure.platform;

import fr.xephi.authme.libs.net.kyori.adventure.audience.Audience;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointered;
import fr.xephi.authme.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import fr.xephi.authme.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface AudienceProvider extends AutoCloseable {
   @NotNull
   Audience all();

   @NotNull
   Audience console();

   @NotNull
   Audience players();

   @NotNull
   Audience player(@NotNull final UUID playerId);

   @NotNull
   default Audience permission(@NotNull final Key permission) {
      return this.permission(permission.namespace() + '.' + permission.value());
   }

   @NotNull
   Audience permission(@NotNull final String permission);

   @NotNull
   Audience world(@NotNull final Key world);

   @NotNull
   Audience server(@NotNull final String serverName);

   @NotNull
   ComponentFlattener flattener();

   void close();

   public interface Builder<P extends AudienceProvider, B extends AudienceProvider.Builder<P, B>> {
      @NotNull
      B componentRenderer(@NotNull final ComponentRenderer<Pointered> componentRenderer);

      @NotNull
      B partition(@NotNull final Function<Pointered, ?> partitionFunction);

      @NotNull
      default <T> B componentRenderer(@NotNull final Function<Pointered, T> partition, @NotNull final ComponentRenderer<T> componentRenderer) {
         return this.partition(partition).componentRenderer(componentRenderer.mapContext(partition));
      }

      @NotNull
      P build();
   }
}
