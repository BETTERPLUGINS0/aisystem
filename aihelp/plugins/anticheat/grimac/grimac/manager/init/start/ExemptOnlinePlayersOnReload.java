package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import java.util.Iterator;

public class ExemptOnlinePlayersOnReload implements StartableInitable {
   public void start() {
      Iterator var1 = GrimAPI.INSTANCE.getPlatformPlayerFactory().getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         PlatformPlayer player = (PlatformPlayer)var1.next();
         User user = PacketEvents.getAPI().getPlayerManager().getUser(player.getNative());
         GrimAPI.INSTANCE.getPlayerDataManager().exemptUsers.add(user);
      }

   }
}
