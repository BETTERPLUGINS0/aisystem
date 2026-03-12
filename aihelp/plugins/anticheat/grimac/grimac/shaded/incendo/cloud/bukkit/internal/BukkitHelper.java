package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandMeta;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.PluginHolder;
import ac.grim.grimac.shaded.incendo.cloud.description.CommandDescription;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executor;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public final class BukkitHelper {
   private BukkitHelper() {
   }

   @NonNull
   public static String description(@NonNull final Command<?> command) {
      Optional<String> bukkitDescription = command.commandMeta().optional(BukkitCommandMeta.BUKKIT_DESCRIPTION);
      if (bukkitDescription.isPresent()) {
         return (String)bukkitDescription.get();
      } else {
         CommandDescription description = command.commandDescription();
         return !description.isEmpty() ? description.description().textDescription() : command.rootComponent().description().textDescription();
      }
   }

   @NonNull
   public static String namespacedLabel(@NonNull final PluginHolder manager, @NonNull final String label) {
      return namespacedLabel(manager.owningPlugin().getName(), label);
   }

   @NonNull
   public static String namespacedLabel(@NonNull final String pluginName, @NonNull final String label) {
      return (pluginName + ':' + label).toLowerCase(Locale.ROOT);
   }

   @NonNull
   public static String stripNamespace(@NonNull final PluginHolder manager, @NonNull final String command) {
      return stripNamespace(manager.owningPlugin().getName(), command);
   }

   @NonNull
   public static String stripNamespace(@NonNull final String pluginName, @NonNull final String command) {
      String[] split = command.split(" ");
      if (!split[0].contains(":")) {
         return command;
      } else {
         String token = split[0];
         String[] splitToken = token.split(":");
         if (namespacedLabel(pluginName, splitToken[1]).equals(token)) {
            split[0] = splitToken[1];
            return String.join(" ", split);
         } else {
            return command;
         }
      }
   }

   @NonNull
   public static Executor mainThreadExecutor(@NonNull final PluginHolder pluginHolder) {
      Plugin plugin = pluginHolder.owningPlugin();
      Server server = plugin.getServer();
      return (task) -> {
         if (server.isPrimaryThread()) {
            task.run();
         } else {
            server.getScheduler().runTask(plugin, task);
         }
      };
   }

   public static void ensurePluginEnabledOrEnabling(@NonNull final Plugin plugin) {
      Plugin fromManager = Bukkit.getServer().getPluginManager().getPlugin(plugin.getName());
      if (!plugin.equals(fromManager) || !plugin.isEnabled()) {
         throw new IllegalStateException("The plugin '" + plugin + "' is not (yet?) valid per the PluginManager. Try calling this method from onEnable rather than in the plugin constructor or onLoad.");
      }
   }
}
