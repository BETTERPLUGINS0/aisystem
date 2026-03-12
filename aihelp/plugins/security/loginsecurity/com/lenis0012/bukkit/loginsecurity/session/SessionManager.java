package com.lenis0012.bukkit.loginsecurity.session;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import com.lenis0012.bukkit.loginsecurity.util.ProfileUtil;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SessionManager {
   private final Map<UUID, PlayerSession> activeSessions = Maps.newConcurrentMap();
   private final LoadingCache<UUID, PlayerSession> preloadCache;

   public SessionManager() {
      this.preloadCache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build(new CacheLoader<UUID, PlayerSession>() {
         public PlayerSession load(UUID uuid) throws Exception {
            return SessionManager.this.newSession(uuid);
         }
      });
   }

   public PlayerSession preloadSession(String playerName, UUID playerUUID) {
      UUID profileId = ProfileUtil.getUUID(playerName, playerUUID);
      return (PlayerSession)this.preloadCache.getUnchecked(profileId);
   }

   public final PlayerSession getPlayerSession(Player player) {
      UUID userId = ProfileUtil.getUUID(player);
      PlayerSession session;
      if (this.activeSessions.containsKey(userId)) {
         session = (PlayerSession)this.activeSessions.get(userId);
      } else {
         session = (PlayerSession)this.preloadCache.getUnchecked(userId);
         if (player.isOnline()) {
            this.activeSessions.put(userId, session);
            this.preloadCache.invalidate(userId);
         }
      }

      return session;
   }

   public final PlayerSession getOfflineSession(UUID profileId) {
      return this.newSession(profileId);
   }

   public final PlayerSession getOfflineSession(String playerName) {
      try {
         PlayerProfile profile = LoginSecurity.getDatastore().getProfileRepository().findByLastNameBlocking(playerName);
         if (profile == null) {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(playerName);
            return offline != null && offline.getUniqueId() != null ? this.getOfflineSession(ProfileUtil.getUUID(playerName, offline.getUniqueId())) : null;
         } else {
            return new PlayerSession(profile, AuthMode.UNAUTHENTICATED);
         }
      } catch (SQLException var4) {
         LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to load profile", var4);
         return null;
      }
   }

   public void onPlayerLogout(Player player) {
      UUID userId = ProfileUtil.getUUID(player);
      this.activeSessions.remove(userId);
   }

   private final PlayerSession newSession(UUID playerId) {
      try {
         PlayerProfile profile = LoginSecurity.getDatastore().getProfileRepository().findByUniqueUserIdBlocking(playerId);
         AuthMode authMode = AuthMode.UNAUTHENTICATED;
         if (profile == null) {
            profile = this.createBlankProfile(playerId);
            authMode = LoginSecurity.getConfiguration().isPasswordRequired() ? AuthMode.UNREGISTERED : AuthMode.AUTHENTICATED;
         }

         return new PlayerSession(profile, authMode);
      } catch (SQLException var4) {
         LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to load profile", var4);
         return null;
      }
   }

   protected final PlayerProfile createBlankProfile(UUID playerId) {
      PlayerProfile profile = new PlayerProfile();
      profile.setUniqueUserId(playerId.toString());
      profile.setUniqueIdMode(ProfileUtil.getUserIdMode());
      return profile;
   }
}
