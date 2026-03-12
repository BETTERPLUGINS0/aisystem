package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.graph.MutableGraph;
import fr.xephi.authme.libs.net.kyori.adventure.audience.Audience;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetAudienceProvider;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Knob;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointered;
import fr.xephi.authme.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import fr.xephi.authme.libs.net.kyori.adventure.text.renderer.ComponentRenderer;
import fr.xephi.authme.libs.net.kyori.adventure.translation.GlobalTranslator;
import fr.xephi.authme.libs.net.kyori.adventure.translation.Translator;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
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
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class BukkitAudiencesImpl extends FacetAudienceProvider<CommandSender, BukkitAudience> implements BukkitAudiences, Listener {
   private static final Map<String, BukkitAudiences> INSTANCES;
   private final Plugin plugin;

   static BukkitAudiencesImpl.Builder builder(@NotNull final Plugin plugin) {
      return new BukkitAudiencesImpl.Builder(plugin);
   }

   static BukkitAudiences instanceFor(@NotNull final Plugin plugin) {
      return builder(plugin).build();
   }

   BukkitAudiencesImpl(@NotNull final Plugin plugin, @NotNull final ComponentRenderer<Pointered> componentRenderer) {
      super(componentRenderer);
      this.plugin = (Plugin)Objects.requireNonNull(plugin, "plugin");
      CommandSender console = this.plugin.getServer().getConsoleSender();
      this.addViewer(console);
      Iterator var4 = this.plugin.getServer().getOnlinePlayers().iterator();

      while(var4.hasNext()) {
         Player player = (Player)var4.next();
         this.addViewer(player);
      }

      this.registerEvent(PlayerJoinEvent.class, EventPriority.LOWEST, (event) -> {
         this.addViewer(event.getPlayer());
      });
      this.registerEvent(PlayerQuitEvent.class, EventPriority.MONITOR, (event) -> {
         this.removeViewer(event.getPlayer());
      });
   }

   @NotNull
   public Audience sender(@NotNull final CommandSender sender) {
      if (sender instanceof Player) {
         return this.player((Player)sender);
      } else if (sender instanceof ConsoleCommandSender) {
         return this.console();
      } else if (sender instanceof ProxiedCommandSender) {
         return this.sender(((ProxiedCommandSender)sender).getCallee());
      } else {
         return (Audience)(!(sender instanceof Entity) && !(sender instanceof Block) ? this.createAudience(Collections.singletonList(sender)) : Audience.empty());
      }
   }

   @NotNull
   public Audience player(@NotNull final Player player) {
      return super.player(player.getUniqueId());
   }

   @NotNull
   protected BukkitAudience createAudience(@NotNull final Collection<CommandSender> viewers) {
      return new BukkitAudience(this.plugin, this, viewers);
   }

   public void close() {
      INSTANCES.remove(this.plugin.getName());
      super.close();
   }

   @NotNull
   public ComponentFlattener flattener() {
      return BukkitComponentSerializer.FLATTENER;
   }

   private <T extends Event> void registerEvent(@NotNull final Class<T> type, @NotNull final EventPriority priority, @NotNull final Consumer<T> callback) {
      Objects.requireNonNull(callback, "callback");
      this.plugin.getServer().getPluginManager().registerEvent(type, this, priority, (listener, event) -> {
         callback.accept(event);
      }, this.plugin, true);
   }

   private void registerLocaleEvent(final EventPriority priority, @NotNull final BiConsumer<Player, Locale> callback) {
      Class<?> eventClass = MinecraftReflection.findClass("org.bukkit.event.player.PlayerLocaleChangeEvent");
      if (eventClass == null) {
         eventClass = MinecraftReflection.findClass("com.destroystokyo.paper.event.player.PlayerLocaleChangeEvent");
      }

      MethodHandle getMethod = MinecraftReflection.findMethod(eventClass, "getLocale", String.class);
      if (getMethod == null) {
         getMethod = MinecraftReflection.findMethod(eventClass, "getNewLocale", String.class);
      }

      if (getMethod != null && PlayerEvent.class.isAssignableFrom(eventClass)) {
         this.registerEvent(eventClass, priority, (event) -> {
            Player player = event.getPlayer();

            String locale;
            try {
               locale = getMethod.invoke(event);
            } catch (Throwable var7) {
               Knob.logError(var7, "Failed to accept %s: %s", eventClass.getName(), player);
               return;
            }

            callback.accept(player, toLocale(locale));
         });
      }

   }

   @NotNull
   private static Locale toLocale(@Nullable final String string) {
      if (string != null) {
         Locale locale = Translator.parseLocale(string);
         if (locale != null) {
            return locale;
         }
      }

      return Locale.US;
   }

   static {
      Knob.OUT = (message) -> {
         Bukkit.getLogger().log(Level.INFO, message);
      };
      Knob.ERR = (message, error) -> {
         Bukkit.getLogger().log(Level.WARNING, message, error);
      };
      INSTANCES = Collections.synchronizedMap(new HashMap(4));
   }

   static final class Builder implements BukkitAudiences.Builder {
      @NotNull
      private final Plugin plugin;
      private ComponentRenderer<Pointered> componentRenderer;

      Builder(@NotNull final Plugin plugin) {
         this.plugin = (Plugin)Objects.requireNonNull(plugin, "plugin");
         this.componentRenderer((ptr) -> {
            return (Locale)ptr.getOrDefault(Identity.LOCALE, BukkitAudiencesImpl.DEFAULT_LOCALE);
         }, GlobalTranslator.renderer());
      }

      @NotNull
      public BukkitAudiencesImpl.Builder componentRenderer(@NotNull final ComponentRenderer<Pointered> componentRenderer) {
         this.componentRenderer = (ComponentRenderer)Objects.requireNonNull(componentRenderer, "component renderer");
         return this;
      }

      @NotNull
      public BukkitAudiences.Builder partition(@NotNull final Function<Pointered, ?> partitionFunction) {
         Objects.requireNonNull(partitionFunction, "partitionFunction");
         return this;
      }

      @NotNull
      public BukkitAudiences build() {
         return (BukkitAudiences)BukkitAudiencesImpl.INSTANCES.computeIfAbsent(this.plugin.getName(), (name) -> {
            this.softDepend("ViaVersion");
            return new BukkitAudiencesImpl(this.plugin, this.componentRenderer);
         });
      }

      private void softDepend(@NotNull final String pluginName) {
         PluginDescriptionFile file = this.plugin.getDescription();
         if (!file.getName().equals(pluginName)) {
            try {
               Field softDepend = MinecraftReflection.needField(file.getClass(), "softDepend");
               List<String> dependencies = (List)softDepend.get(file);
               if (!dependencies.contains(pluginName)) {
                  List<String> newList = ImmutableList.builder().addAll((Iterable)dependencies).add((Object)pluginName).build();
                  softDepend.set(file, newList);
               }
            } catch (Throwable var7) {
               Knob.logError(var7, "Failed to inject softDepend in plugin.yml: %s %s", this.plugin, pluginName);
            }

            try {
               PluginManager manager = this.plugin.getServer().getPluginManager();
               Field dependencyGraphField = MinecraftReflection.needField(manager.getClass(), "dependencyGraph");
               MutableGraph<String> graph = (MutableGraph)dependencyGraphField.get(manager);
               graph.putEdge(file.getName(), pluginName);
            } catch (Throwable var6) {
            }

         }
      }
   }
}
