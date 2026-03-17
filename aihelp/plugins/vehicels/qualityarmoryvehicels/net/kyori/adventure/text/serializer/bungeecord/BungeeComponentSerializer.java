/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.chat.ComponentSerializer
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.text.serializer.bungeecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Field;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.bungeecord.GsonInjections;
import net.kyori.adventure.text.serializer.bungeecord.SelfSerializable;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public final class BungeeComponentSerializer
implements ComponentSerializer<Component, Component, BaseComponent[]> {
    private static boolean SUPPORTED = true;
    private static final BungeeComponentSerializer MODERN;
    private static final BungeeComponentSerializer PRE_1_16;
    private final GsonComponentSerializer serializer;
    private final LegacyComponentSerializer legacySerializer;

    public static boolean isNative() {
        return SUPPORTED;
    }

    public static BungeeComponentSerializer get() {
        return MODERN;
    }

    public static BungeeComponentSerializer legacy() {
        return PRE_1_16;
    }

    public static BungeeComponentSerializer of(GsonComponentSerializer gsonComponentSerializer, LegacyComponentSerializer legacyComponentSerializer) {
        if (gsonComponentSerializer == null || legacyComponentSerializer == null) {
            return null;
        }
        return new BungeeComponentSerializer(gsonComponentSerializer, legacyComponentSerializer);
    }

    public static boolean inject(Gson gson) {
        boolean bl = GsonInjections.injectGson(Objects.requireNonNull(gson, "existing"), gsonBuilder -> {
            GsonComponentSerializer.gson().populator().apply((GsonBuilder)gsonBuilder);
            gsonBuilder.registerTypeAdapterFactory(new SelfSerializable.AdapterFactory());
        });
        SUPPORTED &= bl;
        return bl;
    }

    private BungeeComponentSerializer(GsonComponentSerializer gsonComponentSerializer, LegacyComponentSerializer legacyComponentSerializer) {
        this.serializer = gsonComponentSerializer;
        this.legacySerializer = legacyComponentSerializer;
    }

    private static void bind() {
        try {
            Field field = GsonInjections.field(net.md_5.bungee.chat.ComponentSerializer.class, "gson");
            BungeeComponentSerializer.inject((Gson)field.get(null));
        } catch (Throwable throwable) {
            SUPPORTED = false;
        }
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull @NotNull BaseComponent @NotNull [] baseComponentArray) {
        Objects.requireNonNull(baseComponentArray, "input");
        if (baseComponentArray.length == 1 && baseComponentArray[0] instanceof AdapterComponent) {
            return ((AdapterComponent)baseComponentArray[0]).component;
        }
        return this.serializer.deserialize(net.md_5.bungee.chat.ComponentSerializer.toString((BaseComponent[])baseComponentArray));
    }

    @Override
    @NotNull
    public @NotNull BaseComponent @NotNull [] serialize(@NotNull Component component) {
        Objects.requireNonNull(component, "component");
        if (SUPPORTED) {
            return new BaseComponent[]{new AdapterComponent(component)};
        }
        return net.md_5.bungee.chat.ComponentSerializer.parse((String)((String)this.serializer.serialize(component)));
    }

    static {
        BungeeComponentSerializer.bind();
        MODERN = new BungeeComponentSerializer(GsonComponentSerializer.gson(), LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build());
        PRE_1_16 = new BungeeComponentSerializer(GsonComponentSerializer.builder().downsampleColors().emitLegacyHoverEvent().build(), LegacyComponentSerializer.legacySection());
    }

    class AdapterComponent
    extends BaseComponent
    implements SelfSerializable {
        private final Component component;
        private volatile String legacy;

        AdapterComponent(Component component) {
            this.component = component;
        }

        public String toLegacyText() {
            if (this.legacy == null) {
                this.legacy = BungeeComponentSerializer.this.legacySerializer.serialize(this.component);
            }
            return this.legacy;
        }

        @NotNull
        public BaseComponent duplicate() {
            return this;
        }

        @Override
        public void write(JsonWriter jsonWriter) {
            BungeeComponentSerializer.this.serializer.serializer().getAdapter(Component.class).write(jsonWriter, this.component);
        }
    }
}

