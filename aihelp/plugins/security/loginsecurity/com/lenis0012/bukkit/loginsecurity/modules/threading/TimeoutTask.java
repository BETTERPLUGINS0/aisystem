package com.lenis0012.bukkit.loginsecurity.modules.threading;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeoutTask extends BukkitRunnable {
   private final LoginSecurity plugin;
   private long loginTimeout;

   public TimeoutTask(LoginSecurity plugin) {
      this.plugin = plugin;
      this.reload();
   }

   public void run() {
      if (this.loginTimeout >= 0L) {
         Iterator var1 = Bukkit.getOnlinePlayers().iterator();

         while(var1.hasNext()) {
            Player player = (Player)var1.next();
            if (player.isOnline()) {
               PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
               if (!session.isAuthorized()) {
                  Long loginTime = (Long)MetaData.get(player, "ls_login_time", Long.class);
                  if (loginTime != null && loginTime + this.loginTimeout < System.currentTimeMillis()) {
                     Bukkit.getScheduler().runTask(this.plugin, () -> {
                        player.kickPlayer(LoginSecurity.translate(LanguageKeys.KICK_TIME_OUT).toString());
                     });
                  }
               }
            }
         }

      }
   }

   public void reload() {
      this.loginTimeout = (long)this.plugin.config().getLoginTimeout() * 1000L;
   }
}
