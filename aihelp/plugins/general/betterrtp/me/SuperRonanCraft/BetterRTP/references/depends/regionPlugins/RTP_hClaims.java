package me.SuperRonanCraft.BetterRTP.references.depends.regionPlugins;

import java.lang.reflect.Method;
import org.bukkit.Location;

public class RTP_hClaims implements RegionPluginCheck {
   public boolean check(Location loc) {
      boolean result = true;
      if (REGIONPLUGINS.HCLAIMS.isEnabled()) {
         try {
            Class<?> claimHandlerClass = Class.forName("com.hakan.claim.api.ClaimHandler");
            Method hasMethod = claimHandlerClass.getDeclaredMethod("has", Location.class);
            result = (Boolean)hasMethod.invoke((Object)null, loc);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      return result;
   }
}
