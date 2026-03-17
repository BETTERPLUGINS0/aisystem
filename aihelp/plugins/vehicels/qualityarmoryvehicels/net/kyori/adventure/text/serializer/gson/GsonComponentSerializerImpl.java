/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.ComponentSerializerImpl;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.gson.SerializerFactory;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import net.kyori.adventure.util.Services;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GsonComponentSerializerImpl
implements GsonComponentSerializer {
    private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);
    static final Consumer<GsonComponentSerializer.Builder> BUILDER = SERVICE.map(GsonComponentSerializer.Provider::builder).orElseGet(() -> builder -> {});
    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;
    private final @Nullable LegacyHoverEventSerializer legacyHoverSerializer;
    private final OptionState flags;

    GsonComponentSerializerImpl(OptionState optionState, @Nullable LegacyHoverEventSerializer legacyHoverEventSerializer) {
        this.flags = optionState;
        this.legacyHoverSerializer = legacyHoverEventSerializer;
        this.populator = gsonBuilder -> {
            gsonBuilder.registerTypeAdapterFactory(new SerializerFactory(optionState, legacyHoverEventSerializer));
            return gsonBuilder;
        };
        this.serializer = ((GsonBuilder)this.populator.apply(new GsonBuilder().disableHtmlEscaping())).create();
    }

    @Override
    @NotNull
    public Gson serializer() {
        return this.serializer;
    }

    @Override
    @NotNull
    public UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String string) {
        Component component = this.serializer().fromJson(string, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        }
        return component;
    }

    @Override
    @Nullable
    public Component deserializeOr(@Nullable String string, @Nullable Component component) {
        if (string == null) {
            return component;
        }
        Component component2 = this.serializer().fromJson(string, Component.class);
        if (component2 == null) {
            return component;
        }
        return component2;
    }

    @Override
    @NotNull
    public String serialize(@NotNull Component component) {
        return this.serializer().toJson(component);
    }

    @Override
    @NotNull
    public Component deserializeFromTree(@NotNull JsonElement jsonElement) {
        Component component = this.serializer().fromJson(jsonElement, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(jsonElement);
        }
        return component;
    }

    @Override
    @NotNull
    public JsonElement serializeToTree(@NotNull Component component) {
        return this.serializer().toJsonTree(component);
    }

    @Override
    @NotNull
    public GsonComponentSerializer.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static /* synthetic */ Optional access$000() {
        return SERVICE;
    }

    static final class BuilderImpl
    implements GsonComponentSerializer.Builder {
        private OptionState flags = JSONOptions.byDataVersion();
        private @Nullable LegacyHoverEventSerializer legacyHoverSerializer;

        BuilderImpl() {
            BUILDER.accept(this);
        }

        BuilderImpl(GsonComponentSerializerImpl gsonComponentSerializerImpl) {
            this();
            this.flags = gsonComponentSerializerImpl.flags;
            this.legacyHoverSerializer = gsonComponentSerializerImpl.legacyHoverSerializer;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder options(@NotNull OptionState optionState) {
            this.flags = Objects.requireNonNull(optionState, "flags");
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder editOptions(@NotNull Consumer<OptionState.Builder> consumer) {
            OptionState.Builder builder = OptionState.optionState().values(this.flags);
            Objects.requireNonNull(consumer, "flagEditor").accept(builder);
            this.flags = builder.build();
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer legacyHoverEventSerializer) {
            this.legacyHoverSerializer = legacyHoverEventSerializer;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer build() {
            return new GsonComponentSerializerImpl(this.flags, this.legacyHoverSerializer);
        }
    }

    static final class Instances {
        static final GsonComponentSerializer INSTANCE = GsonComponentSerializerImpl.access$000().map(GsonComponentSerializer.Provider::gson).orElseGet(() -> new GsonComponentSerializerImpl(JSONOptions.byDataVersion(), null));
        static final GsonComponentSerializer LEGACY_INSTANCE = GsonComponentSerializerImpl.access$000().map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> new GsonComponentSerializerImpl(JSONOptions.byDataVersion().at(2525), null));

        Instances() {
        }
    }
}

