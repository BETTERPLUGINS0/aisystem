package ac.grim.grimac.api;

import ac.grim.grimac.api.alerts.AlertManager;
import ac.grim.grimac.api.common.BasicReloadable;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.config.ConfigReloadable;
import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.bukkit.entity.Player;

public interface GrimAbstractAPI extends ConfigReloadable, BasicReloadable {
   @NotNull
   EventBus getEventBus();

   /** @deprecated */
   @Deprecated
   @Nullable
   GrimUser getGrimUser(Player var1);

   @Nullable
   GrimUser getGrimUser(UUID var1);

   void registerVariable(String var1, @Nullable Function<GrimUser, String> var2);

   void registerVariable(String var1, @Nullable String var2);

   String getGrimVersion();

   void registerFunction(String var1, @Nullable Function<Object, Object> var2);

   @Nullable
   Function<Object, Object> getFunction(String var1);

   AlertManager getAlertManager();

   ConfigManager getConfigManager();

   default void reload() {
      this.reload(this.getConfigManager());
   }

   default CompletableFuture<Boolean> reloadAsync() {
      return this.reloadAsync(this.getConfigManager());
   }

   boolean hasStarted();

   int getCurrentTick();

   @NotNull
   GrimPlugin getGrimPlugin(@NotNull Object var1);
}
