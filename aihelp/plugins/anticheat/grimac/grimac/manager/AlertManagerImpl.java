package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.alerts.AlertManager;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.config.ConfigReloadable;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.bukkit.entity.Player;

public final class AlertManagerImpl implements AlertManager, ConfigReloadable, StartableInitable {
   @NotNull
   private static PlatformServer platformServer;

   public void start() {
      platformServer = GrimAPI.INSTANCE.getPlatformServer();
      this.reload(GrimAPI.INSTANCE.getConfigManager().getConfig());
   }

   public void reload(ConfigManager config) {
      this.setConsoleAlertsEnabled(config.getBooleanElse("alerts.print-to-console", true), true);
      this.setConsoleVerboseEnabled(config.getBooleanElse("verbose.print-to-console", false), true);
      AlertManagerImpl.AlertType.NORMAL.enableMessage = config.getStringElse("alerts-enabled", "%prefix% &fAlerts enabled");
      AlertManagerImpl.AlertType.NORMAL.disableMessage = config.getStringElse("alerts-disabled", "%prefix% &fAlerts disabled");
      AlertManagerImpl.AlertType.VERBOSE.enableMessage = config.getStringElse("verbose-enabled", "%prefix% &fVerbose enabled");
      AlertManagerImpl.AlertType.VERBOSE.disableMessage = config.getStringElse("verbose-disabled", "%prefix% &fVerbose disabled");
      AlertManagerImpl.AlertType.BRAND.enableMessage = config.getStringElse("brands-enabled", "%prefix% &fBrands enabled");
      AlertManagerImpl.AlertType.BRAND.disableMessage = config.getStringElse("brands-disabled", "%prefix% &fBrands disabled");
   }

   @NotNull
   private PlatformPlayer requirePlatformPlayerFromUser(@NotNull GrimUser user) {
      Objects.requireNonNull(user, "user cannot be null");
      if (user instanceof GrimPlayer) {
         GrimPlayer grimPlayer = (GrimPlayer)user;
         PlatformPlayer platformPlayer = grimPlayer.platformPlayer;
         Objects.requireNonNull(platformPlayer, "AlertManager action for user " + user.getName() + " with null platformPlayer (potentially during early join)");
         return platformPlayer;
      } else {
         throw new IllegalArgumentException("AlertManager action called with non-GrimPlayer user: " + user.getName());
      }
   }

   private static void sendToggleMessage(@NotNull PlatformPlayer player, boolean enabled, @NotNull AlertManagerImpl.AlertType type) {
      String rawMessage = type.getToggleMessage(enabled);
      if (!rawMessage.isEmpty()) {
         String messageWithPlaceholders = MessageUtil.replacePlaceholders(player, rawMessage);
         player.sendMessage(MessageUtil.miniMessage(messageWithPlaceholders));
      }
   }

   public boolean hasAlertsEnabled(@NotNull GrimUser player) {
      return this.hasAlertsEnabled(this.requirePlatformPlayerFromUser(player));
   }

   public void setAlertsEnabled(@NotNull GrimUser player, boolean enabled, boolean silent) {
      this.setAlertsEnabled(this.requirePlatformPlayerFromUser(player), enabled, silent);
   }

   public boolean hasVerboseEnabled(@NotNull GrimUser player) {
      return this.hasVerboseEnabled(this.requirePlatformPlayerFromUser(player));
   }

   public void setVerboseEnabled(@NotNull GrimUser player, boolean enabled, boolean silent) {
      this.setVerboseEnabled(this.requirePlatformPlayerFromUser(player), enabled, silent);
   }

   public boolean hasBrandsEnabled(@NotNull GrimUser player) {
      GrimPlayer grimPlayer = (GrimPlayer)player;
      return grimPlayer.platformPlayer == null ? false : this.hasBrandsEnabled(grimPlayer.platformPlayer);
   }

   public void setBrandsEnabled(@NotNull GrimUser player, boolean enabled, boolean silent) {
      this.setPlayerStateAndNotify(this.requirePlatformPlayerFromUser(player), enabled, silent, AlertManagerImpl.AlertType.BRAND);
   }

   public boolean hasAlertsEnabled(Player player) {
      return player == null ? false : this.hasAlertsEnabled(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
   }

   public void toggleAlerts(Player player) {
      if (player != null) {
         this.toggleAlerts(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
      }
   }

   public boolean hasVerboseEnabled(Player player) {
      return player == null ? false : this.hasVerboseEnabled(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
   }

   public void toggleVerbose(Player player) {
      if (player != null) {
         this.toggleVerbose(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
      }
   }

   public void handlePlayerQuit(@Nullable PlatformPlayer platformPlayer) {
      if (platformPlayer != null) {
         AlertManagerImpl.AlertType.NORMAL.players.remove(platformPlayer);
         AlertManagerImpl.AlertType.VERBOSE.players.remove(platformPlayer);
         AlertManagerImpl.AlertType.BRAND.players.remove(platformPlayer);
      }
   }

   public boolean toggleConsoleAlerts() {
      return this.toggleConsoleAlerts(false);
   }

   public boolean toggleConsoleAlerts(boolean silent) {
      return this.setConsoleAlertsEnabled(!this.hasConsoleAlertsEnabled(), silent);
   }

   @Contract("_ -> param1")
   public boolean setConsoleAlertsEnabled(boolean enabled) {
      return this.setConsoleAlertsEnabled(enabled, false);
   }

   @Contract("_, _ -> param1")
   public boolean setConsoleAlertsEnabled(boolean enabled, boolean silent) {
      this.setConsoleStateAndNotify(AlertManagerImpl.AlertType.NORMAL, enabled, silent);
      if (!enabled) {
         this.setConsoleVerboseEnabled(false, silent);
      }

      return enabled;
   }

   @Contract(
      pure = true
   )
   public boolean hasConsoleAlertsEnabled() {
      return AlertManagerImpl.AlertType.NORMAL.console;
   }

   public boolean toggleConsoleVerbose() {
      return this.toggleConsoleVerbose(false);
   }

   public boolean toggleConsoleVerbose(boolean silent) {
      return this.setConsoleVerboseEnabled(!this.hasConsoleVerboseEnabled(), silent);
   }

   @Contract("_ -> param1")
   public boolean setConsoleVerboseEnabled(boolean enabled) {
      return this.setConsoleVerboseEnabled(enabled, false);
   }

   @Contract("_, _ -> param1")
   public boolean setConsoleVerboseEnabled(boolean enabled, boolean silent) {
      if (enabled) {
         this.setConsoleAlertsEnabled(true, silent);
      }

      return this.setConsoleStateAndNotify(AlertManagerImpl.AlertType.VERBOSE, enabled, silent);
   }

   @Contract(
      pure = true
   )
   public boolean hasConsoleVerboseEnabled() {
      return AlertManagerImpl.AlertType.VERBOSE.console;
   }

   public boolean toggleConsoleBrands() {
      return this.toggleConsoleBrands(false);
   }

   public boolean toggleConsoleBrands(boolean silent) {
      return this.setConsoleBrandsEnabled(!this.hasConsoleBrandsEnabled(), silent);
   }

   @Contract("_ -> param1")
   public boolean setConsoleBrandsEnabled(boolean enabled) {
      return this.setConsoleStateAndNotify(AlertManagerImpl.AlertType.BRAND, enabled, false);
   }

   @Contract("_, _ -> param1")
   public boolean setConsoleBrandsEnabled(boolean enabled, boolean silent) {
      return this.setConsoleStateAndNotify(AlertManagerImpl.AlertType.BRAND, enabled, silent);
   }

   @Contract(
      pure = true
   )
   public boolean hasConsoleBrandsEnabled() {
      return AlertManagerImpl.AlertType.BRAND.console;
   }

   @Contract("_, _, _ -> param2")
   private boolean setConsoleStateAndNotify(@NotNull AlertManagerImpl.AlertType type, boolean enabled, boolean silent) {
      if (type.console != enabled && !silent) {
         String rawMessage = type.getToggleMessage(enabled);
         if (!rawMessage.isEmpty()) {
            platformServer.getConsoleSender().sendMessage(MessageUtil.miniMessage(MessageUtil.replacePlaceholders((PlatformPlayer)null, rawMessage)));
         }
      }

      type.console = enabled;
      return enabled;
   }

   private void setPlayerStateAndNotify(@NotNull PlatformPlayer platformPlayer, boolean enabled, boolean silent, @NotNull AlertManagerImpl.AlertType type) {
      Objects.requireNonNull(platformPlayer, "platformPlayer cannot be null");
      boolean changed = enabled ? type.players.add(platformPlayer) : type.players.remove(platformPlayer);
      if (changed && !silent) {
         sendToggleMessage(platformPlayer, enabled, type);
      }

   }

   public boolean toggleBrands(@NotNull PlatformPlayer player) {
      return this.toggleBrands(player, false);
   }

   public boolean toggleBrands(@NotNull PlatformPlayer player, boolean silent) {
      return this.setBrandsEnabled(player, !this.hasBrandsEnabled(player), silent);
   }

   @Contract("_, _ -> param2")
   public boolean setBrandsEnabled(@NotNull PlatformPlayer player, boolean enabled) {
      return this.setBrandsEnabled(player, enabled, false);
   }

   @Contract("_, _, _ -> param2")
   public boolean setBrandsEnabled(@NotNull PlatformPlayer player, boolean enabled, boolean silent) {
      this.setPlayerStateAndNotify(player, enabled, silent, AlertManagerImpl.AlertType.BRAND);
      return enabled;
   }

   @Contract(
      pure = true
   )
   public boolean hasBrandsEnabled(@NotNull PlatformPlayer player) {
      return AlertManagerImpl.AlertType.BRAND.players.contains(player);
   }

   public boolean toggleVerbose(@NotNull PlatformPlayer player) {
      return this.toggleVerbose(player, false);
   }

   public boolean toggleVerbose(@NotNull PlatformPlayer player, boolean silent) {
      return this.setVerboseEnabled(player, !this.hasVerboseEnabled(player), silent);
   }

   @Contract("_, _ -> param2")
   public boolean setVerboseEnabled(@NotNull PlatformPlayer player, boolean enabled) {
      return this.setVerboseEnabled(player, enabled, false);
   }

   @Contract("_, _, _ -> param2")
   public boolean setVerboseEnabled(@NotNull PlatformPlayer player, boolean enabled, boolean silent) {
      if (enabled) {
         this.setAlertsEnabled(player, true, silent);
      }

      this.setPlayerStateAndNotify(player, enabled, silent, AlertManagerImpl.AlertType.VERBOSE);
      return enabled;
   }

   @Contract(
      pure = true
   )
   public boolean hasVerboseEnabled(@NotNull PlatformPlayer player) {
      return AlertManagerImpl.AlertType.VERBOSE.players.contains(player);
   }

   public boolean toggleAlerts(@NotNull PlatformPlayer player) {
      return this.toggleAlerts(player, false);
   }

   public boolean toggleAlerts(@NotNull PlatformPlayer player, boolean silent) {
      return this.setAlertsEnabled(player, !this.hasAlertsEnabled(player), silent);
   }

   @Contract("_, _ -> param2")
   public boolean setAlertsEnabled(@NotNull PlatformPlayer player, boolean enabled) {
      return this.setAlertsEnabled(player, enabled, false);
   }

   @Contract("_, _, _ -> param2")
   public boolean setAlertsEnabled(@NotNull PlatformPlayer player, boolean enabled, boolean silent) {
      this.setPlayerStateAndNotify(player, enabled, silent, AlertManagerImpl.AlertType.NORMAL);
      if (!enabled) {
         this.setVerboseEnabled(player, false, silent);
      }

      return enabled;
   }

   @Contract(
      pure = true
   )
   public boolean hasAlertsEnabled(@NotNull PlatformPlayer player) {
      return AlertManagerImpl.AlertType.NORMAL.players.contains(player);
   }

   public Set<PlatformPlayer> sendBrand(Component component, @Nullable Set<PlatformPlayer> excluding) {
      return AlertManagerImpl.AlertType.BRAND.send(component, excluding);
   }

   public Set<PlatformPlayer> sendVerbose(Component component, @Nullable Set<PlatformPlayer> excluding) {
      return AlertManagerImpl.AlertType.VERBOSE.send(component, excluding);
   }

   public Set<PlatformPlayer> sendAlert(Component component, @Nullable Set<PlatformPlayer> excluding) {
      return AlertManagerImpl.AlertType.NORMAL.send(component, excluding);
   }

   @Contract(
      pure = true
   )
   public boolean hasVerboseListeners() {
      return AlertManagerImpl.AlertType.VERBOSE.hasListeners();
   }

   @Contract(
      pure = true
   )
   public boolean hasAlertListeners() {
      return AlertManagerImpl.AlertType.NORMAL.hasListeners();
   }

   private static enum AlertType {
      NORMAL,
      VERBOSE,
      BRAND;

      public String enableMessage;
      public String disableMessage;
      public final Set<PlatformPlayer> players = new CopyOnWriteArraySet();
      public boolean console;

      @Contract(
         pure = true
      )
      public boolean hasListeners() {
         return !this.players.isEmpty() || this.console;
      }

      @Contract(
         pure = true
      )
      public String getToggleMessage(boolean enabled) {
         return enabled ? this.enableMessage : this.disableMessage;
      }

      public Set<PlatformPlayer> send(Component component, @Nullable Set<PlatformPlayer> excluding) {
         HashSet<PlatformPlayer> listeners = new HashSet(this.players);
         if (excluding != null) {
            listeners.removeAll(excluding);
         }

         Iterator var4 = listeners.iterator();

         while(var4.hasNext()) {
            PlatformPlayer platformPlayer = (PlatformPlayer)var4.next();
            platformPlayer.sendMessage(component);
         }

         if (this.console && (excluding == null || !excluding.contains((Object)null))) {
            AlertManagerImpl.platformServer.getConsoleSender().sendMessage(component);
            listeners.add((Object)null);
         }

         return listeners;
      }

      // $FF: synthetic method
      private static AlertManagerImpl.AlertType[] $values() {
         return new AlertManagerImpl.AlertType[]{NORMAL, VERBOSE, BRAND};
      }
   }
}
