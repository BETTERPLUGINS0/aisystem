package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import java.util.Iterator;

public class PacketLimiter implements StartableInitable {
   public void start() {
      GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runAtFixedRate(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
         Iterator var0 = GrimAPI.INSTANCE.getPlayerDataManager().getEntries().iterator();

         while(var0.hasNext()) {
            GrimPlayer player = (GrimPlayer)var0.next();
            player.cancelledPackets.set(0);
         }

      }, 1L, 20L);
   }
}
