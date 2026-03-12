package com.lenis0012.bukkit.loginsecurity.modules.threading;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.LoginSecurityConfig;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.BypassAction;
import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import com.lenis0012.bukkit.loginsecurity.util.ProfileUtil;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ThreadingModule extends Module<LoginSecurity> implements Listener {
   private Cache<UUID, Long> sessionCache;
   private TimeoutTask timeout;
   private MessageTask message;

   public ThreadingModule(LoginSecurity plugin) {
      super(plugin);
   }

   public void enable() {
      this.reload();
      (this.timeout = new TimeoutTask((LoginSecurity)this.plugin)).runTaskTimer(this.plugin, 20L, 20L);
      (this.message = new MessageTask((LoginSecurity)this.plugin)).runTaskTimer(this.plugin, 20L, 20L);
      this.register(this);
      Bukkit.getOnlinePlayers().stream().filter(OfflinePlayer::isOnline).forEach((player) -> {
         MetaData.set(player, "ls_login_time", System.currentTimeMillis());
      });
   }

   public void disable() {
   }

   public void reload() {
      LoginSecurityConfig config = ((LoginSecurity)this.plugin).config();
      int sessionTimeout = config.getSessionTimeout();
      this.sessionCache = CacheBuilder.newBuilder().expireAfterWrite((long)Math.max(1, sessionTimeout), TimeUnit.SECONDS).build();
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
      MetaData.unset(player, "ls_last_message");
      MetaData.unset(player, "ls_login_time");
      if (session.isLoggedIn()) {
         this.sessionCache.put(ProfileUtil.getUUID(player), System.currentTimeMillis());
      }

   }

   @EventHandler(
      priority = EventPriority.LOW
   )
   public void onPlayerLogin(PlayerLoginEvent event) {
      Player player = event.getPlayer();
      UUID profileId = ProfileUtil.getUUID(player);
      Long sessionTime = (Long)this.sessionCache.getIfPresent(profileId);
      MetaData.set(player, "ls_login_time", System.currentTimeMillis());
      if (sessionTime != null) {
         if (event.getAddress() != null) {
            String ipAddress = event.getAddress().toString();
            PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
            if (ipAddress.equals(session.getProfile().getIpAddress())) {
               int seconds = (int)((System.currentTimeMillis() - sessionTime) / 1000L);
               session.performAction(new BypassAction(AuthService.SESSION, (LoginSecurity)this.plugin));
               player.sendMessage(LoginSecurity.translate(LanguageKeys.SESSION_CONTINUE).param("sec", seconds).toString());
            }
         }
      }
   }
}
