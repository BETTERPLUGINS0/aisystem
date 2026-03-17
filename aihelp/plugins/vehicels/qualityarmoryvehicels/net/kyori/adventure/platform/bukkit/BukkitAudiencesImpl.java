/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.ProxiedCommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginDescriptionFile
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import com.google.common.collect.ImmutableList;
import com.google.common.graph.MutableGraph;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.bukkit.BukkitAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.platform.bukkit.MinecraftReflection;
import net.kyori.adventure.platform.facet.FacetAudienceProvider;
import net.kyori.adventure.platform.facet.Knob;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class BukkitAudiencesImpl
extends FacetAudienceProvider<CommandSender, BukkitAudience>
implements BukkitAudiences,
Listener {
    private static final Map<String, BukkitAudiences> INSTANCES;
    private final Plugin plugin;

    static Builder builder(@NotNull Plugin plugin) {
        return new Builder(plugin);
    }

    static BukkitAudiences instanceFor(@NotNull Plugin plugin) {
        return BukkitAudiencesImpl.builder(plugin).build();
    }

    BukkitAudiencesImpl(@NotNull Plugin plugin, @NotNull ComponentRenderer<Pointered> componentRenderer) {
        super(componentRenderer);
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        ConsoleCommandSender consoleCommandSender = this.plugin.getServer().getConsoleSender();
        this.addViewer(consoleCommandSender);
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.addViewer(player);
        }
        this.registerEvent(PlayerJoinEvent.class, EventPriority.LOWEST, playerJoinEvent -> this.addViewer(playerJoinEvent.getPlayer()));
        this.registerEvent(PlayerQuitEvent.class, EventPriority.MONITOR, playerQuitEvent -> this.removeViewer(playerQuitEvent.getPlayer()));
    }

    @Override
    @NotNull
    public Audience sender(@NotNull CommandSender commandSender) {
        if (commandSender instanceof Player) {
            return this.player((Player)commandSender);
        }
        if (commandSender instanceof ConsoleCommandSender) {
            return this.console();
        }
        if (commandSender instanceof ProxiedCommandSender) {
            return this.sender(((ProxiedCommandSender)commandSender).getCallee());
        }
        if (commandSender instanceof Entity || commandSender instanceof Block) {
            return Audience.empty();
        }
        return this.createAudience(Collections.singletonList(commandSender));
    }

    @Override
    @NotNull
    public Audience player(@NotNull Player player) {
        return super.player(player.getUniqueId());
    }

    @Override
    @NotNull
    protected BukkitAudience createAudience(@NotNull Collection<CommandSender> collection) {
        return new BukkitAudience(this.plugin, this, collection);
    }

    @Override
    public void close() {
        INSTANCES.remove(this.plugin.getName());
        super.close();
    }

    @Override
    @NotNull
    public ComponentFlattener flattener() {
        return BukkitComponentSerializer.FLATTENER;
    }

    private <T extends Event> void registerEvent(@NotNull Class<T> clazz, @NotNull EventPriority eventPriority, @NotNull Consumer<T> consumer) {
        Objects.requireNonNull(consumer, "callback");
        this.plugin.getServer().getPluginManager().registerEvent(clazz, (Listener)this, eventPriority, (listener, event) -> consumer.accept(event), this.plugin, true);
    }

    private void registerLocaleEvent(EventPriority eventPriority, @NotNull BiConsumer<Player, Locale> biConsumer) {
        MethodHandle methodHandle;
        Class<?> clazz = MinecraftReflection.findClass("org.bukkit.event.player.PlayerLocaleChangeEvent");
        if (clazz == null) {
            clazz = MinecraftReflection.findClass("com.destroystokyo.paper.event.player.PlayerLocaleChangeEvent");
        }
        if ((methodHandle = MinecraftReflection.findMethod(clazz, "getLocale", String.class, new Class[0])) == null) {
            methodHandle = MinecraftReflection.findMethod(clazz, "getNewLocale", String.class, new Class[0]);
        }
        if (methodHandle != null && PlayerEvent.class.isAssignableFrom(clazz)) {
            Class<?> clazz2 = clazz;
            MethodHandle methodHandle2 = methodHandle;
            this.registerEvent(clazz2, eventPriority, playerEvent -> {
                String string;
                Player player = playerEvent.getPlayer();
                try {
                    string = methodHandle2.invoke((PlayerEvent)playerEvent);
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to accept %s: %s", clazz2.getName(), player);
                    return;
                }
                biConsumer.accept(player, BukkitAudiencesImpl.toLocale(string));
            });
        }
    }

    @NotNull
    private static Locale toLocale(@Nullable String string) {
        Locale locale;
        if (string != null && (locale = Translator.parseLocale(string)) != null) {
            return locale;
        }
        return Locale.US;
    }

    static {
        Knob.OUT = string -> Bukkit.getLogger().log(Level.INFO, (String)string);
        Knob.ERR = (string, throwable) -> Bukkit.getLogger().log(Level.WARNING, (String)string, (Throwable)throwable);
        INSTANCES = Collections.synchronizedMap(new HashMap(4));
    }

    static final class Builder
    implements BukkitAudiences.Builder {
        @NotNull
        private final Plugin plugin;
        private ComponentRenderer<Pointered> componentRenderer;

        Builder(@NotNull Plugin plugin) {
            this.plugin = Objects.requireNonNull(plugin, "plugin");
            this.componentRenderer(pointered -> pointered.getOrDefault(Identity.LOCALE, DEFAULT_LOCALE), GlobalTranslator.renderer());
        }

        @Override
        @NotNull
        public Builder componentRenderer(@NotNull ComponentRenderer<Pointered> componentRenderer) {
            this.componentRenderer = Objects.requireNonNull(componentRenderer, "component renderer");
            return this;
        }

        @Override
        public @NotNull BukkitAudiences.Builder partition(@NotNull Function<Pointered, ?> function) {
            Objects.requireNonNull(function, "partitionFunction");
            return this;
        }

        @Override
        @NotNull
        public BukkitAudiences build() {
            return INSTANCES.computeIfAbsent(this.plugin.getName(), string -> {
                this.softDepend("ViaVersion");
                return new BukkitAudiencesImpl(this.plugin, this.componentRenderer);
            });
        }

        private void softDepend(@NotNull String string) {
            Object object;
            Object object2;
            Field field;
            PluginDescriptionFile pluginDescriptionFile = this.plugin.getDescription();
            if (pluginDescriptionFile.getName().equals(string)) {
                return;
            }
            try {
                field = MinecraftReflection.needField(pluginDescriptionFile.getClass(), "softDepend");
                object2 = (List)field.get(pluginDescriptionFile);
                if (!object2.contains(string)) {
                    object = ((ImmutableList.Builder)((ImmutableList.Builder)ImmutableList.builder().addAll((Iterable)object2)).add(string)).build();
                    field.set(pluginDescriptionFile, object);
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to inject softDepend in plugin.yml: %s %s", this.plugin, string);
            }
            try {
                field = this.plugin.getServer().getPluginManager();
                object2 = MinecraftReflection.needField(field.getClass(), "dependencyGraph");
                object = (MutableGraph)((Field)object2).get(field);
                object.putEdge(pluginDescriptionFile.getName(), string);
            } catch (Throwable throwable) {
                // empty catch block
            }
        }
    }
}

