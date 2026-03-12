package fr.xephi.authme.listener;

import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DoubleLoginFixListener implements Listener {
   @Inject
   private CommonService service;

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Collection<? extends Player> PlayerList = Bukkit.getServer().getOnlinePlayers();
      HashSet<String> PlayerSet = new HashSet();
      Iterator var4 = PlayerList.iterator();

      while(var4.hasNext()) {
         Player ep = (Player)var4.next();
         if (PlayerSet.contains(ep.getName().toLowerCase())) {
            ep.kickPlayer(this.service.retrieveSingleMessage(ep.getPlayer(), MessageKey.DOUBLE_LOGIN_FIX));
            break;
         }

         PlayerSet.add(ep.getName().toLowerCase());
      }

   }
}
