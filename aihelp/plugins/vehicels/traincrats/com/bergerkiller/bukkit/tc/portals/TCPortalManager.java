package com.bergerkiller.bukkit.tc.portals;

import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.World;

public class TCPortalManager {
   private static final HashMap<String, PortalProvider> portalProviders = new HashMap();

   public static void addPortalSupport(String pluginName, PortalProvider provider) {
      portalProviders.put(pluginName, provider);
   }

   public static void removePortalSupport(String pluginName) {
      portalProviders.remove(pluginName);
   }

   public static boolean isAvailable(String pluginName) {
      return portalProviders.containsKey(pluginName);
   }

   public static PortalDestination getPortalDestination(World world, String portalName) {
      PortalDestination dest = null;
      Iterator var3 = portalProviders.values().iterator();

      while(var3.hasNext()) {
         PortalProvider provider = (PortalProvider)var3.next();
         dest = provider.getPortalDestination(world, portalName);
         if (dest != null) {
            break;
         }
      }

      return dest;
   }
}
