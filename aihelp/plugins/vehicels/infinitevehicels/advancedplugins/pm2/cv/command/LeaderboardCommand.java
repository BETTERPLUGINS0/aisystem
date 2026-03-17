package advancedplugins.pm2.cv.command;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.vehicle.LeaderboardMenu;
import me.PM2.infinitevehicles.commands.BaseCommand;
import me.PM2.infinitevehicles.commands.annotation.CatchUnknown;
import me.PM2.infinitevehicles.commands.annotation.CommandAlias;
import me.PM2.infinitevehicles.commands.annotation.CommandCompletion;
import me.PM2.infinitevehicles.commands.annotation.CommandPermission;
import me.PM2.infinitevehicles.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("leaderboard|lb")
@CommandPermission("pm2.command.leaderboard")
public class LeaderboardCommand extends BaseCommand {
   @Default
   @CatchUnknown
   @CommandCompletion("@vehicles_lb")
   @CommandPermission("pm2.command.leaderboard")
   public void leaderboard(Player player, String vehicleID) {
      Vehicle var3 = (Vehicle)InfiniteVehicles.getVehicleHandler().getRegisteredVehicles().stream().filter((var1x) -> {
         return var1x.getConfiguration().getId().equals(var2);
      }).findFirst().orElse((Object)null);
      String var10001;
      if (var3 == null) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.COMMAND_UNKNOWN_VEHICLE.value());
      } else if (!var3.getConfiguration().isLeaderboard()) {
         var10001 = LangConfiguration.PREFIX.value();
         var1.sendMessage(var10001 + LangConfiguration.LEADERBOARD_NOT_ENABLED.value());
      } else {
         (new LeaderboardMenu(var3, 1)).open(var1);
      }
   }
}
