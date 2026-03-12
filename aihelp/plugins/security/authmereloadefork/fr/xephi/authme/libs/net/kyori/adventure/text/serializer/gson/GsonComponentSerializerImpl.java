package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson;

import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.GsonBuilder;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.JSONOptions;
import fr.xephi.authme.libs.net.kyori.adventure.util.Services;
import fr.xephi.authme.libs.net.kyori.option.OptionState;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GsonComponentSerializerImpl implements GsonComponentSerializer {
   private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);
   static final Consumer<GsonComponentSerializer.Builder> BUILDER;
   private final Gson serializer;
   private final UnaryOperator<GsonBuilder> populator;
   @Nullable
   private final fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
   private final OptionState flags;

   GsonComponentSerializerImpl(final OptionState flags, @Nullable final fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer) {
      this.flags = flags;
      this.legacyHoverSerializer = legacyHoverSerializer;
      this.populator = (builder) -> {
         builder.registerTypeAdapterFactory(new SerializerFactory(flags, legacyHoverSerializer));
         return builder;
      };
      this.serializer = ((GsonBuilder)this.populator.apply((new GsonBuilder()).disableHtmlEscaping())).create();
   }

   @NotNull
   public Gson serializer() {
      return this.serializer;
   }

   @NotNull
   public UnaryOperator<GsonBuilder> populator() {
      return this.populator;
   }

   @NotNull
   public Component deserialize(@NotNull final String string) {
      Component component = (Component)this.serializer().fromJson(string, Component.class);
      return component;
   }

   @Nullable
   public Component deserializeOr(@Nullable final String input, @Nullable final Component fallback) {
      if (input == null) {
         return fallback;
      } else {
         Component component = (Component)this.serializer().fromJson(input, Component.class);
         return component == null ? fallback : component;
      }
   }

   @NotNull
   public String serialize(@NotNull final Component component) {
      return this.serializer().toJson((Object)component);
   }

   @NotNull
   public Component deserializeFromTree(@NotNull final JsonElement input) {
      Component component = (Component)this.serializer().fromJson(input, Component.class);
      return component;
   }

   @NotNull
   public JsonElement serializeToTree(@NotNull final Component component) {
      return this.serializer().toJsonTree(component);
   }

   @NotNull
   public GsonComponentSerializer.Builder toBuilder() {
      return new GsonComponentSerializerImpl.BuilderImpl(this);
   }

   static {
      BUILDER = (Consumer)SERVICE.map(GsonComponentSerializer.Provider::builder).orElseGet(() -> {
         return (builder) -> {
         };
      });
   }

   static final class BuilderImpl implements GsonComponentSerializer.Builder {
      private OptionState flags;
      @Nullable
      private fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;

      BuilderImpl() {
         this.flags = JSONOptions.byDataVersion();
         GsonComponentSerializerImpl.BUILDER.accept(this);
      }

      BuilderImpl(final GsonComponentSerializerImpl serializer) {
         this();
         this.flags = serializer.flags;
         this.legacyHoverSerializer = serializer.legacyHoverSerializer;
      }

      @NotNull
      public GsonComponentSerializer.Builder options(@NotNull final OptionState flags) {
         this.flags = (OptionState)Objects.requireNonNull(flags, "flags");
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor) {
         OptionState.Builder builder = OptionState.optionState().values(this.flags);
         ((Consumer)Objects.requireNonNull(optionEditor, "flagEditor")).accept(builder);
         this.flags = builder.build();
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer serializer) {
         this.legacyHoverSerializer = serializer;
         return this;
      }

      @NotNull
      public GsonComponentSerializer build() {
         return new GsonComponentSerializerImpl(this.flags, this.legacyHoverSerializer);
      }
   }

   static final class Instances {
      static final GsonComponentSerializer INSTANCE;
      static final GsonComponentSerializer LEGACY_INSTANCE;

      static {
         INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gson).orElseGet(() -> {
            return new GsonComponentSerializerImpl(JSONOptions.byDataVersion(), (fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)null);
         });
         LEGACY_INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> {
            return new GsonComponentSerializerImpl(JSONOptions.byDataVersion().at(2525), (fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer)null);
         });
      }
   }
}
