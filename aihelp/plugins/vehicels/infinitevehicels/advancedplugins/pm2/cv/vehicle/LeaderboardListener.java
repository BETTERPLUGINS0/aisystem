package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.util.LeaderboardUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LeaderboardListener implements Listener {
   @EventHandler
   public void onPlayerKill(PlayerDeathEvent event) {
      Player var2 = var1.getEntity().getKiller();
      if (var2 != null) {
         Vehicle var3 = InfiniteVehicles.getVehicleHandler().getVehicleByOperator(var2);
         if (var3 != null) {
            LeaderboardUtil.addKills(var2, var3, 1);
         }
      }
   }
}
