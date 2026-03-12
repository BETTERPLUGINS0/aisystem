package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.option.OptionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.util.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class GsonComponentSerializerImpl implements GsonComponentSerializer {
   private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);
   static final Consumer<GsonComponentSerializer.Builder> BUILDER;
   private final Gson serializer;
   private final UnaryOperator<GsonBuilder> populator;
   @Nullable
   private final ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
   @Nullable
   private final BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement;
   private final OptionState flags;

   GsonComponentSerializerImpl(OptionState flags, @Nullable ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer, @Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement) {
      this.flags = flags;
      this.legacyHoverSerializer = legacyHoverSerializer;
      this.compatShowAchievement = compatShowAchievement;
      this.populator = (builder) -> {
         builder.registerTypeAdapterFactory(new SerializerFactory(flags, legacyHoverSerializer, compatShowAchievement));
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
   public Component deserialize(@NotNull String string) {
      Component component = (Component)this.serializer().fromJson(string, Component.class);
      if (component == null) {
         throw ComponentSerializerImpl.notSureHowToDeserialize(string);
      } else {
         return component;
      }
   }

   @Nullable
   public Component deserializeOr(@Nullable String input, @Nullable Component fallback) {
      if (input == null) {
         return fallback;
      } else {
         Component component = (Component)this.serializer().fromJson(input, Component.class);
         return component == null ? fallback : component;
      }
   }

   @NotNull
   public String serialize(@NotNull Component component) {
      return this.serializer().toJson(component);
   }

   @NotNull
   public Component deserializeFromTree(@NotNull JsonElement input) {
      Component component = (Component)this.serializer().fromJson(input, Component.class);
      if (component == null) {
         throw ComponentSerializerImpl.notSureHowToDeserialize(input);
      } else {
         return component;
      }
   }

   @NotNull
   public JsonElement serializeToTree(@NotNull Component component) {
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
      private ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer legacyHoverSerializer;
      @Nullable
      private BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement;

      BuilderImpl() {
         this.flags = JSONOptions.byDataVersion();
         GsonComponentSerializerImpl.BUILDER.accept(this);
      }

      BuilderImpl(GsonComponentSerializerImpl serializer) {
         this();
         this.flags = serializer.flags;
         this.legacyHoverSerializer = serializer.legacyHoverSerializer;
         this.compatShowAchievement = serializer.compatShowAchievement;
      }

      @NotNull
      public GsonComponentSerializer.Builder options(@NotNull OptionState flags) {
         this.flags = (OptionState)Objects.requireNonNull(flags, "flags");
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder editOptions(@NotNull Consumer<OptionState.Builder> optionEditor) {
         OptionState.Builder builder = JSONOptions.schema().stateBuilder().values(this.flags);
         ((Consumer)Objects.requireNonNull(optionEditor, "flagEditor")).accept(builder);
         this.flags = builder.build();
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer serializer) {
         this.legacyHoverSerializer = serializer;
         return this;
      }

      @NotNull
      public GsonComponentSerializer.Builder showAchievementToComponent(@Nullable BackwardCompatUtil.ShowAchievementToComponent compatShowAchievement) {
         this.compatShowAchievement = compatShowAchievement;
         return this;
      }

      @NotNull
      public GsonComponentSerializer build() {
         return new GsonComponentSerializerImpl(this.flags, this.legacyHoverSerializer, this.compatShowAchievement);
      }
   }

   static final class Instances {
      static final GsonComponentSerializer INSTANCE;
      static final GsonComponentSerializer LEGACY_INSTANCE;

      static {
         INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gson).orElseGet(() -> {
            return new GsonComponentSerializerImpl(JSONOptions.byDataVersion(), (ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer)null, (BackwardCompatUtil.ShowAchievementToComponent)null);
         });
         LEGACY_INSTANCE = (GsonComponentSerializer)GsonComponentSerializerImpl.SERVICE.map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> {
            return new GsonComponentSerializerImpl(JSONOptions.byDataVersion().at(2525), (ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.json.LegacyHoverEventSerializer)null, (BackwardCompatUtil.ShowAchievementToComponent)null);
         });
      }
   }
}
