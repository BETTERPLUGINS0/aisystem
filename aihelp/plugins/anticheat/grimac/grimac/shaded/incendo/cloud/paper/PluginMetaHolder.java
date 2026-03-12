package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.PluginHolder;
import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public interface PluginMetaHolder extends PluginHolder {
   PluginMeta owningPluginMeta();

   default Plugin owningPlugin() {
      return (Plugin)Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(this.owningPluginMeta().getName()), () -> {
         return this.owningPluginMeta().getName() + " Plugin instance";
      });
   }

   @API(
      status = Status.INTERNAL
   )
   static PluginMetaHolder fromPluginHolder(final PluginHolder pluginHolder) {
      return new PluginMetaHolder() {
         public PluginMeta owningPluginMeta() {
            return pluginHolder.owningPlugin().getPluginMeta();
         }

         public Plugin owningPlugin() {
            return pluginHolder.owningPlugin();
         }
      };
   }
}
