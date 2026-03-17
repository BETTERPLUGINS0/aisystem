/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.Title
 *  com.destroystokyo.paper.Title$Builder
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import com.destroystokyo.paper.Title;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import net.kyori.adventure.platform.bukkit.CraftBukkitFacet;
import net.kyori.adventure.platform.bukkit.MinecraftReflection;
import net.kyori.adventure.platform.bukkit.SpigotFacet;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.platform.facet.FacetBase;
import net.kyori.adventure.platform.facet.Knob;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PaperFacet<V extends CommandSender>
extends FacetBase<V> {
    private static final boolean SUPPORTED = Knob.isEnabled("paper", true);
    static final Class<?> NATIVE_COMPONENT_CLASS = MinecraftReflection.findClass(String.join((CharSequence)".", "net", "kyori", "adventure", "text", "Component"));
    private static final MethodHandle PAPER_ADVENTURE_AS_VANILLA = PaperFacet.findAsVanillaMethod();
    private static final Class<?> NATIVE_GSON_COMPONENT_SERIALIZER_CLASS = MinecraftReflection.findClass(String.join((CharSequence)".", "net", "kyori", "adventure", "text", "serializer", "gson", "GsonComponentSerializer"));
    private static final Class<?> NATIVE_GSON_COMPONENT_SERIALIZER_IMPL_CLASS = MinecraftReflection.findClass(String.join((CharSequence)".", "net", "kyori", "adventure", "text", "serializer", "gson", "GsonComponentSerializerImpl"));
    private static final MethodHandle NATIVE_GSON_COMPONENT_SERIALIZER_GSON_GETTER = MinecraftReflection.findStaticMethod(NATIVE_GSON_COMPONENT_SERIALIZER_CLASS, "gson", NATIVE_GSON_COMPONENT_SERIALIZER_CLASS, new Class[0]);
    private static final MethodHandle NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD = PaperFacet.findNativeDeserializeMethod();

    @Nullable
    private static MethodHandle findAsVanillaMethod() {
        try {
            Class<?> clazz = MinecraftReflection.findClass("io.papermc.paper.adventure.PaperAdventure");
            Method method = clazz.getDeclaredMethod("asVanilla", NATIVE_COMPONENT_CLASS);
            return MinecraftReflection.lookup().unreflect(method);
        } catch (IllegalAccessException | NoSuchMethodException | NullPointerException exception) {
            return null;
        }
    }

    @Nullable
    private static MethodHandle findNativeDeserializeMethod() {
        try {
            Method method = NATIVE_GSON_COMPONENT_SERIALIZER_IMPL_CLASS.getDeclaredMethod("deserialize", String.class);
            method.setAccessible(true);
            return MinecraftReflection.lookup().unreflect(method);
        } catch (IllegalAccessException | NoSuchMethodException | NullPointerException exception) {
            return null;
        }
    }

    protected PaperFacet(@Nullable Class<? extends V> clazz) {
        super(clazz);
    }

    @Override
    public boolean isSupported() {
        return super.isSupported() && SUPPORTED;
    }

    static class TabList
    extends CraftBukkitFacet.TabList {
        private static final boolean SUPPORTED = MinecraftReflection.hasField(CLASS_CRAFT_PLAYER, NATIVE_COMPONENT_CLASS, "playerListHeader") && MinecraftReflection.hasField(CLASS_CRAFT_PLAYER, NATIVE_COMPONENT_CLASS, "playerListFooter");
        private static final MethodHandle NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD_BOUND = TabList.createBoundNativeDeserializeMethodHandle();

        TabList() {
        }

        @Nullable
        private static MethodHandle createBoundNativeDeserializeMethodHandle() {
            if (SUPPORTED) {
                try {
                    return NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD.bindTo(NATIVE_GSON_COMPONENT_SERIALIZER_GSON_GETTER.invoke());
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to access native GsonComponentSerializer", new Object[0]);
                    return null;
                }
            }
            return null;
        }

        @Override
        public boolean isSupported() {
            return SUPPORTED && super.isSupported() && (CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER != null && CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER != null || PAPER_ADVENTURE_AS_VANILLA != null);
        }

        @Override
        protected Object create117Packet(Player player, @Nullable Object object, @Nullable Object object2) {
            if (CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER == null && CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER == null) {
                return CLIENTBOUND_TAB_LIST_PACKET_CTOR.invoke(PAPER_ADVENTURE_AS_VANILLA.invoke(object == null ? this.createMessage(player, (Component)Component.empty()) : object), PAPER_ADVENTURE_AS_VANILLA.invoke(object2 == null ? this.createMessage(player, (Component)Component.empty()) : object2));
            }
            Object object3 = CLIENTBOUND_TAB_LIST_PACKET_CTOR.invoke(null, null);
            CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER.invoke(object3, object == null ? this.createMessage(player, (Component)Component.empty()) : object);
            CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER.invoke(object3, object2 == null ? this.createMessage(player, (Component)Component.empty()) : object2);
            return object3;
        }

        @Override
        @Nullable
        public Object createMessage(@NotNull Player player, @NotNull Component component) {
            try {
                return NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD_BOUND.invoke((String)GsonComponentSerializer.gson().serialize(component));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to create native Component message", new Object[0]);
                return null;
            }
        }
    }

    static class Title
    extends SpigotFacet.Message<Player>
    implements Facet.Title<Player, BaseComponent[], Title.Builder, com.destroystokyo.paper.Title> {
        private static final boolean SUPPORTED = MinecraftReflection.hasClass("com.destroystokyo.paper.Title");

        protected Title() {
            super(Player.class);
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && SUPPORTED;
        }

        @Override
        public // Could not load outer class - annotation placement on inner may be incorrect
         @NotNull Title.Builder createTitleCollection() {
            return com.destroystokyo.paper.Title.builder();
        }

        @Override
        public void contributeTitle(// Could not load outer class - annotation placement on inner may be incorrect
         @NotNull Title.Builder builder, BaseComponent @NotNull [] baseComponentArray) {
            builder.title(baseComponentArray);
        }

        @Override
        public void contributeSubtitle(// Could not load outer class - annotation placement on inner may be incorrect
         @NotNull Title.Builder builder, BaseComponent @NotNull [] baseComponentArray) {
            builder.subtitle(baseComponentArray);
        }

        @Override
        public void contributeTimes(// Could not load outer class - annotation placement on inner may be incorrect
         @NotNull Title.Builder builder, int n, int n2, int n3) {
            if (n > -1) {
                builder.fadeIn(n);
            }
            if (n2 > -1) {
                builder.stay(n2);
            }
            if (n3 > -1) {
                builder.fadeOut(n3);
            }
        }

        @Override
        @Nullable
        public com.destroystokyo.paper.Title completeTitle(// Could not load outer class - annotation placement on inner may be incorrect
         @NotNull Title.Builder builder) {
            return builder.build();
        }

        @Override
        public void showTitle(@NotNull Player player, @NotNull com.destroystokyo.paper.Title title) {
            player.sendTitle(title);
        }

        @Override
        public void clearTitle(@NotNull Player player) {
            player.hideTitle();
        }

        @Override
        public void resetTitle(@NotNull Player player) {
            player.resetTitle();
        }
    }
}

