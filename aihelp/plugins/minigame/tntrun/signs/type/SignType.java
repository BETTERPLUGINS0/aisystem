package tntrun.signs.type;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface SignType {
   void handleCreation(SignChangeEvent var1);

   void handleClick(PlayerInteractEvent var1);

   void handleDestroy(BlockBreakEvent var1);
}
