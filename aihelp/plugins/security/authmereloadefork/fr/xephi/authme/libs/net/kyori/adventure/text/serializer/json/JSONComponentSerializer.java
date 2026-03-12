package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.util.PlatformAPI;
import fr.xephi.authme.libs.net.kyori.option.OptionState;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JSONComponentSerializer extends ComponentSerializer<Component, Component, String> {
   @NotNull
   static JSONComponentSerializer json() {
      return JSONComponentSerializerAccessor.Instances.INSTANCE;
   }

   @NotNull
   static JSONComponentSerializer.Builder builder() {
      return (JSONComponentSerializer.Builder)JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
   }

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      JSONComponentSerializer instance();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Supplier<JSONComponentSerializer.Builder> builder();
   }

   public interface Builder {
      @NotNull
      JSONComponentSerializer.Builder options(@NotNull final OptionState flags);

      @NotNull
      JSONComponentSerializer.Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor);

      /** @deprecated */
      @Deprecated
      @NotNull
      JSONComponentSerializer.Builder downsampleColors();

      @NotNull
      JSONComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer);

      /** @deprecated */
      @Deprecated
      @NotNull
      JSONComponentSerializer.Builder emitLegacyHoverEvent();

      @NotNull
      JSONComponentSerializer build();
   }
}
