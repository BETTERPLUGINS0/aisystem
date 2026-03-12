package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.OptionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.ComponentSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JSONComponentSerializer extends ComponentSerializer<Component, Component, String> {
   @NotNull
   static JSONComponentSerializer json() {
      return JSONComponentSerializerAccessor.Instances.INSTANCE;
   }

   @NotNull
   static JSONComponentSerializer.Builder builder() {
      return (JSONComponentSerializer.Builder)JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
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
}
