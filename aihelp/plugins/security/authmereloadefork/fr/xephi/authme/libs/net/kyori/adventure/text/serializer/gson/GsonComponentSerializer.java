package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.GsonBuilder;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.net.kyori.adventure.builder.AbstractBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import fr.xephi.authme.libs.net.kyori.adventure.util.Buildable;
import fr.xephi.authme.libs.net.kyori.adventure.util.PlatformAPI;
import fr.xephi.authme.libs.net.kyori.option.OptionState;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GsonComponentSerializer extends JSONComponentSerializer, Buildable<GsonComponentSerializer, GsonComponentSerializer.Builder> {
   @NotNull
   static GsonComponentSerializer gson() {
      return GsonComponentSerializerImpl.Instances.INSTANCE;
   }

   @NotNull
   static GsonComponentSerializer colorDownsamplingGson() {
      return GsonComponentSerializerImpl.Instances.LEGACY_INSTANCE;
   }

   static GsonComponentSerializer.Builder builder() {
      return new GsonComponentSerializerImpl.BuilderImpl();
   }

   @NotNull
   Gson serializer();

   @NotNull
   UnaryOperator<GsonBuilder> populator();

   @NotNull
   Component deserializeFromTree(@NotNull final JsonElement input);

   @NotNull
   JsonElement serializeToTree(@NotNull final Component component);

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      GsonComponentSerializer gson();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      GsonComponentSerializer gsonLegacy();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Consumer<GsonComponentSerializer.Builder> builder();
   }

   public interface Builder extends AbstractBuilder<GsonComponentSerializer>, Buildable.Builder<GsonComponentSerializer>, JSONComponentSerializer.Builder {
      @NotNull
      GsonComponentSerializer.Builder options(@NotNull final OptionState flags);

      @NotNull
      GsonComponentSerializer.Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor);

      @NotNull
      default GsonComponentSerializer.Builder downsampleColors() {
         return this.editOptions((features) -> {
            features.value(JSONOptions.EMIT_RGB, false);
         });
      }

      /** @deprecated */
      @Deprecated
      @NotNull
      default GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer) {
         return this.legacyHoverEventSerializer((fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)serializer);
      }

      @NotNull
      GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer serializer);

      /** @deprecated */
      @Deprecated
      @NotNull
      default GsonComponentSerializer.Builder emitLegacyHoverEvent() {
         return this.editOptions((b) -> {
            b.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.BOTH);
         });
      }

      @NotNull
      GsonComponentSerializer build();
   }
}
