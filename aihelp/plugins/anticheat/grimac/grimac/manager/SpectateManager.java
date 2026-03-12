package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.ReloadableInitable;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.utils.math.Location;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpectateManager implements StartableInitable, ReloadableInitable {
   private final Map<UUID, SpectateManager.PreviousState> spectatingPlayers = new ConcurrentHashMap();
   private final Set<UUID> hiddenPlayers = ConcurrentHashMap.newKeySet();
   private final Set<String> allowedWorlds = ConcurrentHashMap.newKeySet();
   private boolean checkWorld = false;

   public void start() {
      this.reload();
   }

   public void reload() {
      this.allowedWorlds.clear();
      this.allowedWorlds.addAll(GrimAPI.INSTANCE.getConfigManager().getConfig().getStringListElse("spectators.allowed-worlds", new ArrayList()));
      this.checkWorld = !this.allowedWorlds.isEmpty() && !((String)(new ArrayList(this.allowedWorlds)).get(0)).isEmpty();
   }

   public boolean isSpectating(UUID uuid) {
      return this.spectatingPlayers.containsKey(uuid);
   }

   public boolean shouldHidePlayer(GrimPlayer receiver, WrapperPlayServerPlayerInfo.PlayerData playerData) {
      return playerData.getUser() != null && playerData.getUser().getUUID() != null && this.shouldHidePlayer(receiver, playerData.getUser().getUUID());
   }

   public boolean shouldHidePlayer(GrimPlayer receiver, UUID uuid) {
      return !Objects.equals(uuid, receiver.uuid) && (this.spectatingPlayers.containsKey(uuid) || this.hiddenPlayers.contains(uuid)) && (receiver.uuid == null || !this.spectatingPlayers.containsKey(receiver.uuid) && !this.hiddenPlayers.contains(receiver.uuid)) && (!this.checkWorld || receiver.platformPlayer != null && this.allowedWorlds.contains(receiver.platformPlayer.getWorld().getName()));
   }

   public boolean enable(PlatformPlayer platformPlayer) {
      if (this.spectatingPlayers.containsKey(platformPlayer.getUniqueId())) {
         return false;
      } else {
         this.spectatingPlayers.put(platformPlayer.getUniqueId(), new SpectateManager.PreviousState(platformPlayer.getGameMode(), platformPlayer.getLocation()));
         return true;
      }
   }

   public void onLogin(UUID uuid) {
      this.hiddenPlayers.add(uuid);
   }

   public void onQuit(UUID uuid) {
      this.hiddenPlayers.remove(uuid);
      this.handlePlayerStopSpectating(uuid);
   }

   public void disable(@NotNull PlatformPlayer platformPlayer, boolean teleportBack) {
      SpectateManager.PreviousState previousState = (SpectateManager.PreviousState)this.spectatingPlayers.get(platformPlayer.getUniqueId());
      if (previousState != null) {
         if (teleportBack && previousState.location.isWorldLoaded()) {
            platformPlayer.teleportAsync(previousState.location).thenAccept((bool) -> {
               if (bool) {
                  this.onDisable(previousState, platformPlayer);
               } else {
                  platformPlayer.sendMessage((Component)Component.text("Teleport failed, please try again.", (TextColor)NamedTextColor.RED));
               }

            });
         } else {
            this.onDisable(previousState, platformPlayer);
         }
      }

   }

   private void onDisable(SpectateManager.PreviousState previousState, PlatformPlayer platformPlayer) {
      platformPlayer.setGameMode(previousState.gameMode);
      this.handlePlayerStopSpectating(platformPlayer.getUniqueId());
   }

   public void handlePlayerStopSpectating(UUID uuid) {
      this.spectatingPlayers.remove(uuid);
   }

   private static record PreviousState(GameMode gameMode, Location location) {
      private PreviousState(GameMode gameMode, Location location) {
         this.gameMode = gameMode;
         this.location = location;
      }

      public GameMode gameMode() {
         return this.gameMode;
      }

      public Location location() {
         return this.location;
      }
   }
}
