package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import java.lang.reflect.Method;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class RTP_UltimateClaims implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.ULTIMATECLAIMS.isEnabled()) {
         try {
            JavaPlugin ultimateClaimsInstance = JavaPlugin.getPlugin(this.getPluginMainClass());
            Method getClaimManagerMethod = ultimateClaimsInstance.getClass().getMethod("getClaimManager");
            Object claimManager = getClaimManagerMethod.invoke(ultimateClaimsInstance);
            Method hasClaimMethod = claimManager.getClass().getMethod("hasClaim", Chunk.class);
            return Boolean.FALSE.equals(hasClaimMethod.invoke(claimManager, loc.getChunk()));
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

      return result;
   }

   private Class<?> getPluginMainClass() throws ClassNotFoundException {
      try {
         return Class.forName("com.craftaro.ultimateclaims.UltimateClaims");
      } catch (ClassNotFoundException var2) {
         return Class.forName("com.songoda.ultimateclaims.UltimateClaims");
      }
   }
}
