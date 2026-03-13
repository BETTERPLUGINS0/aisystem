package github.nighter.smartspawner.hooks.protections.api;

import com.google.common.eventbus.Subscribe;
import com.plotsquared.core.events.PlotDeleteEvent;
import com.plotsquared.core.events.Result;
import com.plotsquared.core.plot.Plot;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerPlaceEvent;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlotSquared implements Listener {
   private HashMap<Plot, HashSet<Location>> spawnersData = new HashMap();

   public static boolean canInteract(@NotNull Player player, @NotNull Location location) {
      Plot plot = Plot.getPlot(com.plotsquared.core.location.Location.at(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
      return plot == null ? true : plot.isAdded(player.getUniqueId());
   }

   @Subscribe
   public void onPlotDelete(PlotDeleteEvent event) {
      if (event.getEventResult() != Result.DENY) {
         Plot plot = event.getPlot();
         if (this.spawnersData.containsKey(plot)) {
            Iterator var3 = ((HashSet)this.spawnersData.get(plot)).iterator();

            while(var3.hasNext()) {
               Location loc = (Location)var3.next();
               if (loc == null) {
                  return;
               }

               SpawnerData spawner = SmartSpawner.getInstance().getSpawnerManager().getSpawnerByLocation(loc);
               if (spawner != null) {
                  SmartSpawner.getInstance().getSpawnerManager().removeGhostSpawner(spawner.getSpawnerId());
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   private void onSpawnerPlace(SpawnerPlaceEvent e) {
      Location location = e.getLocation();
      if (location != null) {
         Plot plot = Plot.getPlot(com.plotsquared.core.location.Location.at(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
         if (plot != null) {
            if (!this.spawnersData.containsKey(plot)) {
               this.spawnersData.put(plot, new HashSet());
            }

            ((HashSet)this.spawnersData.get(plot)).add(location);
         }
      }
   }
}
