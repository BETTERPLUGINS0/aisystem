package github.nighter.smartspawner.hooks.protections.api;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Towny {
   public static boolean canPlayerInteractSpawner(@NotNull Player player, @NotNull Location location) {
      Town town = null;

      try {
         town = TownyAPI.getInstance().getTownBlock(location).getTown();
      } catch (NullPointerException | NotRegisteredException var4) {
         return true;
      }

      try {
         Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
         return town.hasResident(resident) || town.hasTrustedResident(resident);
      } catch (Exception var5) {
         return true;
      }
   }
}
