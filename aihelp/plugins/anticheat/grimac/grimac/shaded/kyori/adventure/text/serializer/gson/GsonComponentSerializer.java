package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.kyori.adventure.util.Buildable;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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
         return this.legacyHoverEventSerializer((ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)serializer);
      }

      @NotNull
      GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer serializer);

      /** @deprecated */
      @Deprecated
      @NotNull
      default GsonComponentSerializer.Builder emitLegacyHoverEvent() {
         return this.editOptions((b) -> {
            b.value(JSONOptions.EMIT_HOVER_EVENT_TYPE, JSONOptions.HoverEventValueMode.ALL);
         });
      }

      @NotNull
      GsonComponentSerializer build();
   }
}
