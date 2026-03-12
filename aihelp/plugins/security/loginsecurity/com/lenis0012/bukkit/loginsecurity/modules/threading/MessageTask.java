package com.lenis0012.bukkit.loginsecurity.modules.threading;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageTask extends BukkitRunnable {
   private final LoginSecurity plugin;
   private long messageDelay;

   public MessageTask(LoginSecurity plugin) {
      this.plugin = plugin;
      this.reload();
   }

   public void run() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         if (player.isOnline()) {
            PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
            AuthMode authMode = session.getAuthMode();
            LanguageKeys message = authMode.getAuthMessage();
            if (message != null) {
               long lastMessage = (Long)MetaData.get(player, "ls_last_message", (Object)0L);
               if (lastMessage + this.messageDelay <= System.currentTimeMillis()) {
                  player.sendMessage(ChatColor.RED + LoginSecurity.translate(message).toString());
                  MetaData.set(player, "ls_last_message", System.currentTimeMillis());
               }
            }
         }
      }

   }

   public void reload() {
      this.messageDelay = (long)this.plugin.config().getLoginMessageDelay() * 1000L;
   }
}
