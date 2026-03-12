package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;
import pl.minecodes.plots.api.plot.PlotApi;
import pl.minecodes.plots.api.plot.PlotServiceApi;

public class RTP_MinePlots implements RegionPluginCheck {
   private PlotServiceApi plotServiceApi;

   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.MINEPLOTS.isEnabled()) {
         try {
            RegisteredServiceProvider<PlotServiceApi> serviceProvider = Bukkit.getServicesManager().getRegistration(PlotServiceApi.class);
            Objects.requireNonNull(serviceProvider, "[MinePlots Respect] Service provider is null.");
            this.plotServiceApi = (PlotServiceApi)serviceProvider.getProvider();
            this.plotServiceApi = (PlotServiceApi)serviceProvider.getProvider();
            PlotApi plot = this.plotServiceApi.getPlot(loc);
            if (plot != null) {
               result = false;
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return result;
   }
}
