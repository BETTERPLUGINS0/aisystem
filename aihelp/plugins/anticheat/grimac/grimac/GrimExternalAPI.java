package ac.grim.grimac;

import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.alerts.AlertManager;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.event.events.GrimReloadEvent;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.manager.config.ConfigManagerFileImpl;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.ConfigReloadObserver;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.Generated;
import org.bukkit.entity.Player;

public class GrimExternalAPI implements GrimAbstractAPI, ConfigReloadObserver, StartableInitable {
   private final GrimAPI api;
   private final Map<String, Function<GrimUser, String>> variableReplacements = new ConcurrentHashMap();
   private final Map<String, String> staticReplacements = new ConcurrentHashMap();
   private final Map<String, Function<Object, Object>> functions = new ConcurrentHashMap();
   private final ConfigManagerFileImpl configManagerFile = new ConfigManagerFileImpl();
   private ConfigManager configManager = null;
   private boolean started = false;

   public GrimExternalAPI(GrimAPI api) {
      this.api = api;
   }

   @NotNull
   public EventBus getEventBus() {
      return this.api.getEventBus();
   }

   @Nullable
   public GrimUser getGrimUser(Player player) {
      return this.getGrimUser(player.getUniqueId());
   }

   @Nullable
   public GrimUser getGrimUser(UUID uuid) {
      return this.api.getPlayerDataManager().getPlayer(uuid);
   }

   public void registerVariable(String string, Function<GrimUser, String> replacement) {
      if (replacement == null) {
         this.variableReplacements.remove(string);
      } else {
         this.variableReplacements.put(string, replacement);
      }

   }

   public void registerVariable(String variable, String replacement) {
      if (replacement == null) {
         this.staticReplacements.remove(variable);
      } else {
         this.staticReplacements.put(variable, replacement);
      }

   }

   public String getGrimVersion() {
      return this.api.getGrimPlugin().getDescription().getVersion();
   }

   public void registerFunction(String key, Function<Object, Object> function) {
      if (function == null) {
         this.functions.remove(key);
      } else {
         this.functions.put(key, function);
      }

   }

   public Function<Object, Object> getFunction(String key) {
      return (Function)this.functions.get(key);
   }

   public AlertManager getAlertManager() {
      return GrimAPI.INSTANCE.getAlertManager();
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public boolean hasStarted() {
      return this.started;
   }

   public int getCurrentTick() {
      return GrimAPI.INSTANCE.getTickManager().currentTick;
   }

   @NotNull
   public GrimPlugin getGrimPlugin(@NotNull Object o) {
      return this.api.getExtensionManager().getPlugin(o);
   }

   public void load() {
      this.reload((ConfigManager)this.configManagerFile);
      this.api.getLoader().registerAPIService();
   }

   public void start() {
      this.started = true;

      try {
         GrimAPI.INSTANCE.getConfigManager().start();
      } catch (Exception var2) {
         LogUtil.error("Failed to start config manager.", var2);
      }

   }

   public void reload(ConfigManager config) {
      if (config.isLoadedAsync() && this.started) {
         GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
            this.successfulReload(config);
         });
      } else {
         this.successfulReload(config);
      }

   }

   public CompletableFuture<Boolean> reloadAsync(ConfigManager config) {
      if (config.isLoadedAsync() && this.started) {
         CompletableFuture<Boolean> future = new CompletableFuture();
         GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
            future.complete(this.successfulReload(config));
         });
         return future;
      } else {
         return CompletableFuture.completedFuture(this.successfulReload(config));
      }
   }

   private boolean successfulReload(ConfigManager config) {
      try {
         config.reload();
         GrimAPI.INSTANCE.getConfigManager().load(config);
         if (this.started) {
            GrimAPI.INSTANCE.getConfigManager().start();
         }

         this.onReload(config);
         if (this.started) {
            GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
               GrimAPI.INSTANCE.getEventBus().post(new GrimReloadEvent(true));
            });
         }

         return true;
      } catch (Exception var3) {
         LogUtil.error("Failed to reload config", var3);
         if (this.started) {
            GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
               GrimAPI.INSTANCE.getEventBus().post(new GrimReloadEvent(false));
            });
         }

         return false;
      }
   }

   public void onReload(ConfigManager newConfig) {
      if (newConfig == null) {
         LogUtil.warn("ConfigManager not set. Using default config file manager.");
         this.configManager = this.configManagerFile;
      } else {
         this.configManager = newConfig;
      }

      this.updateVariables();
      GrimAPI.INSTANCE.getAlertManager().reload(this.configManager);
      GrimAPI.INSTANCE.getDiscordManager().reload();
      GrimAPI.INSTANCE.getSpectateManager().reload();
      GrimAPI.INSTANCE.getViolationDatabaseManager().reload();
      if (this.started) {
         Iterator var2 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

         while(var2.hasNext()) {
            GrimPlayer player = (GrimPlayer)var2.next();
            player.runSafely(() -> {
               player.reload(this.configManager);
            });
         }

      }
   }

   private void updateVariables() {
      this.variableReplacements.putIfAbsent("%player%", GrimUser::getName);
      this.variableReplacements.putIfAbsent("%uuid%", (user) -> {
         return user.getUniqueId().toString();
      });
      this.variableReplacements.putIfAbsent("%ping%", (user) -> {
         return user.getTransactionPing().makeConcatWithConstants<invokedynamic>(user.getTransactionPing());
      });
      this.variableReplacements.putIfAbsent("%brand%", GrimUser::getBrand);
      this.variableReplacements.putIfAbsent("%h_sensitivity%", (user) -> {
         double var10000 = user.getHorizontalSensitivity();
         return ((int)Math.round(var10000 * 200.0D)).makeConcatWithConstants<invokedynamic>((int)Math.round(var10000 * 200.0D));
      });
      this.variableReplacements.putIfAbsent("%v_sensitivity%", (user) -> {
         double var10000 = user.getVerticalSensitivity();
         return ((int)Math.round(var10000 * 200.0D)).makeConcatWithConstants<invokedynamic>((int)Math.round(var10000 * 200.0D));
      });
      this.variableReplacements.putIfAbsent("%fast_math%", (user) -> {
         return (!user.isVanillaMath()).makeConcatWithConstants<invokedynamic>(!user.isVanillaMath());
      });
      this.variableReplacements.putIfAbsent("%tps%", (user) -> {
         return String.format("%.2f", GrimAPI.INSTANCE.getPlatformServer().getTPS());
      });
      this.variableReplacements.putIfAbsent("%version%", GrimUser::getVersionName);
      this.staticReplacements.put("%prefix%", MessageUtil.translateAlternateColorCodes('&', GrimAPI.INSTANCE.getConfigManager().getPrefix()));
      this.staticReplacements.putIfAbsent("%grim_version%", this.getGrimVersion());
   }

   @Generated
   public Map<String, Function<GrimUser, String>> getVariableReplacements() {
      return this.variableReplacements;
   }

   @Generated
   public Map<String, String> getStaticReplacements() {
      return this.staticReplacements;
   }
}
